package io.live_mall.modules.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.DateUtils;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.constants.MmsConstants;
import io.live_mall.modules.server.dao.*;
import io.live_mall.modules.server.entity.*;
import io.live_mall.modules.server.model.DuiFuNoticeModel;
import io.live_mall.modules.server.service.MmsPaymentItemService;
import io.live_mall.modules.sys.entity.SysUserEntity;
import io.live_mall.modules.sys.service.SysUserService;
import io.live_mall.sms.mms.MmsClient;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yewl
 * @date 2023/2/23 13:26
 * @description
 */
@Service("mmsPaymentItemService")
@AllArgsConstructor
public class MmsPaymentItemServiceImpl extends ServiceImpl<MmsPaymentItemDao, MmsPaymentItemEntity> implements MmsPaymentItemService {
    private final MmsTemplateDao mmsTemplateDao;
    private final MmsSmsContentDao mmsSmsContentDao;
    private final OrderDao orderDao;
    private final SysUserService sysUserService;
    private final OrderPayDao orderPayDao;
    private final MmsLogDao mmsLogDao;
    @Override
    public PageUtils pages(Map<String, Object> params) {
        String mmsLogId = String.valueOf(params.get("mmsLogId"));
        return new PageUtils(this.baseMapper.pages(new Query<MmsPaymentItemEntity>().getPage(params), mmsLogId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sendPaymentCompleted(String token, String startDate, String endDate, String ids, Long userId) {
        List<String> order_ids = Arrays.asList(ids.split(","));
        List<DuiFuNoticeModel> models = this.payMendData(startDate, endDate, order_ids);
        // 保存mms发送付息日志
        MmsLogEntity logEntity = new MmsLogEntity();
        logEntity.setStartDate(DateUtils.stringToDate(startDate, DateUtils.DATE_PATTERN));
        logEntity.setEndDate(DateUtils.stringToDate(endDate, DateUtils.DATE_PATTERN));
        logEntity.setRowNum(models.size());
        logEntity.setType(2);
        logEntity.setCreateTime(new Date());
        logEntity.setCreateUser(userId);
        mmsLogDao.insert(logEntity);
        MmsTemplateEntity mmsTemplateEntity = mmsTemplateDao.selectOne(Wrappers.lambdaQuery(MmsTemplateEntity.class)
                .eq(MmsTemplateEntity::getType, 3).orderByDesc(MmsTemplateEntity::getCreateTime).last("LIMIT 1"));
        if (Objects.nonNull(mmsTemplateEntity)) {
            List<MmsPaymentItemEntity> entities = new ArrayList<>();
            // 理财师姓名|客户姓名|产品名称|认购金额|第N次付息金额
            String text = "Text1|Text2|Text3|Text4|Text5";
            models.forEach(record -> {
                String mobile = record.getMobile();
                JSONObject result = null;
                if (StringUtils.isNotBlank(mobile)) {
                    try {
                        StringBuilder sb = new StringBuilder();
                        sb.append(record.getRealname()).append("|").append(record.getCustomerName()).append(("|"))
                                .append(record.getProductName()).append("|").append(record.getAppointMoney()).append("|")
                                .append(record.getName()).append(record.getPayMoney());
                        result = MmsClient.send(token, text, mobile, sb.toString(), mmsTemplateEntity.getMmsId());
                    } catch (Exception e) {
                        log.error("e-->{}", e);
                    }
                }
                // 保存发送明细
                MmsPaymentItemEntity entity = new MmsPaymentItemEntity();
                entity.setMmsLogId(logEntity.getId());
                entity.setType(1);
                entity.setOrderId(record.getId());
                entity.setProductName(record.getProductName());
                entity.setAppointMoney(record.getAppointMoney() / 10000);
                entity.setCustomerName(record.getCustomerName());
                entity.setCustomerPhone(record.getPhone());
                entity.setSaleId(record.getSaleId());
                entity.setSaleName(record.getRealname());
                entity.setSaleMobile(mobile);
                entity.setName(record.getName());
                entity.setPayDate(record.getDate());
                entity.setPayMoney(record.getPayMoney());
                entity.setCreateUser(userId);
                entity.setCreateTime(new Date());
                if (null != result) {
                    JSONObject content = result.getJSONObject("content");
                    entity.setCode(Integer.valueOf(content.getString("code")));
                    entity.setResult(result.toJSONString());
                }
                entities.add(entity);
                //保存发送内容
                MmsSmsContentEntity mmsSmsContentEntity = new MmsSmsContentEntity();
                mmsSmsContentEntity.setType(1);
                mmsSmsContentEntity.setReceiveId(String.valueOf(record.getSaleId()));
                mmsSmsContentEntity.setPhone(record.getMobile());
                String content = MmsConstants.PAYMENT_COMPLETE_MMS_CONTENT.replace("${Text1}", record.getRealname())
                        .replace("${Text2}", record.getCustomerName()).replace("${Text3}", record.getProductName())
                        .replace("${Text4}", String.valueOf(record.getSumMoney())).replace("${Text5}", String.valueOf(record.getPayMoney()));
                mmsSmsContentEntity.setContent(content);
                mmsSmsContentEntity.setCreateTime(new Date());
                mmsSmsContentEntity.setCreateUser(userId);
                mmsSmsContentDao.insert(mmsSmsContentEntity);
            });
            if (!entities.isEmpty()) {
                this.saveBatch(entities);
            }
        }

        return true;
    }

    private List<DuiFuNoticeModel> payMendData(String startDate, String endDate, List<String> ids) {
        List<OrderPayEntity> orderPayEntities = orderPayDao.orderPayList(ids, startDate, endDate);
        List<DuiFuNoticeModel> models = new ArrayList<>();
        if (!orderPayEntities.isEmpty()) {
            Set<String> orderIds = new HashSet<>();
            orderPayEntities.forEach(orderPayEntity -> orderIds.add(orderPayEntity.getOrderId()));
            List<DuiFuNoticeModel> list = orderDao.selectDuifuNoticeData(new ArrayList<>(orderIds));
            Map<String, DuiFuNoticeModel> noticeModelMap = list.stream().collect(Collectors.toMap(DuiFuNoticeModel::getId, model -> model));
            orderPayEntities.forEach(entity -> {
                DuiFuNoticeModel noticeModel = noticeModelMap.get(entity.getOrderId());
                OrderEntity orderEntity = orderDao.selectById(entity.getOrderId());
                SysUserEntity userEntity = sysUserService.getById(orderEntity.getSaleId());
                if (Objects.nonNull(userEntity)) {
                    noticeModel.setSaleId(userEntity.getUserId());
                    noticeModel.setRealname(userEntity.getRealname());
                    noticeModel.setMobile(userEntity.getMobile());
                }
                if (Objects.nonNull(noticeModel)) {
                    noticeModel.setName(entity.getName());
                    noticeModel.setPayDate(entity.getPayDate());
                }
                noticeModel.setPayMoney(entity.getPayMoney());
                models.add(noticeModel);
            });
        }
        return models;
    }

    @Override
    public boolean sendPaymentEarlyWarning(String token, List<DuiFuNoticeModel> list) {
        MmsTemplateEntity mmsTemplate = mmsTemplateDao.selectOne(Wrappers.lambdaQuery(MmsTemplateEntity.class)
                .eq(MmsTemplateEntity::getType, 4).orderByDesc(MmsTemplateEntity::getCreateTime).last("LIMIT 1"));
        if (Objects.nonNull(mmsTemplate)) {
            List<MmsPaymentItemEntity> entities = new ArrayList<>();
            // 理财师姓名|客户姓名|产品名称|付息次数|付息日|认购金额|第N次付息金额
            String text = "Text1|Text2|Text3|Text4|Text5|Text6|Text7";
            list.forEach(record -> {
                String mobile = record.getMobile();
                JSONObject result = null;
                if (StringUtils.isNotBlank(mobile)) {
                    try {
                        StringBuilder sb = new StringBuilder();
                        sb.append(record.getRealname()).append("|").append(record.getCustomerName()).append(("|"))
                                .append(record.getProductName()).append("|").append(record.getName()).append("|")
                                .append(record.getPayDate()).append("|").append(record.getAppointMoney()).append("|")
                                .append(record.getPayMoney());
                        result = MmsClient.send(token, text, mobile, sb.toString(), mmsTemplate.getMmsId());
                    } catch (Exception e) {
                        log.error("e-->{}", e);
                    }
                }
                // 保存发送明细
                MmsPaymentItemEntity entity = new MmsPaymentItemEntity();
                entity.setType(2);
                entity.setOrderId(record.getId());
                entity.setProductName(record.getProductName());
                entity.setAppointMoney(record.getAppointMoney() / 10000);
                entity.setCustomerName(record.getCustomerName());
                entity.setCustomerPhone(record.getPhone());
                entity.setSaleId(record.getSaleId());
                entity.setSaleName(record.getRealname());
                entity.setSaleMobile(mobile);
                entity.setName(record.getName());
                entity.setPayDate(record.getDate());
                entity.setPayMoney(record.getPayMoney());
                entity.setCreateTime(new Date());
                if (null != result) {
                    JSONObject content = result.getJSONObject("content");
                    entity.setCode(Integer.valueOf(content.getString("code")));
                    entity.setResult(result.toJSONString());
                }
                entities.add(entity);

                //保存发送内容
                MmsSmsContentEntity mmsSmsContentEntity = new MmsSmsContentEntity();
                mmsSmsContentEntity.setType(2);
                mmsSmsContentEntity.setReceiveId(String.valueOf(record.getSaleId()));
                mmsSmsContentEntity.setPhone(mobile);
                String content = MmsConstants.PAYMENT_EARLY_WARNING_MMS_CONTENT.replace("${Text1}", record.getRealname())
                        .replace("${Text2}", record.getCustomerName()).replace("${Text3}", record.getProductName())
                        .replace("${Text4}", record.getName()).replace("${Text5}", String.valueOf(record.getPayDate()))
                        .replace("${Text6}", String.valueOf(record.getAppointMoney())).replace("${Text7}", String.valueOf(record.getPayMoney()));
                mmsSmsContentEntity.setContent(content);
                mmsSmsContentEntity.setCreateTime(new Date());
                mmsSmsContentDao.insert(mmsSmsContentEntity);
            });
            if (!entities.isEmpty()) {
                this.saveBatch(entities);
            }
        }

        return true;
    }
}
