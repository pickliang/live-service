package io.live_mall.modules.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.MmsPaymentItemDao;
import io.live_mall.modules.server.dao.MmsTemplateDao;
import io.live_mall.modules.server.entity.MmsPaymentItemEntity;
import io.live_mall.modules.server.entity.MmsTemplateEntity;
import io.live_mall.modules.server.model.DuiFuNoticeModel;
import io.live_mall.modules.server.service.MmsPaymentItemService;
import io.live_mall.sms.mms.MmsClient;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author yewl
 * @date 2023/2/23 13:26
 * @description
 */
@Service("mmsPaymentItemService")
@AllArgsConstructor
public class MmsPaymentItemServiceImpl extends ServiceImpl<MmsPaymentItemDao, MmsPaymentItemEntity> implements MmsPaymentItemService {
    private final MmsTemplateDao mmsTemplateDao;
    @Override
    public PageUtils pages(Map<String, Object> params) {
        String mmsLogId = String.valueOf(params.get("mmsLogId"));
        return new PageUtils(this.baseMapper.pages(new Query<MmsPaymentItemEntity>().getPage(params), mmsLogId));
    }

    @Override
    public boolean sendSalePayment(String token, List<DuiFuNoticeModel> list, String logId, Long userId) {
        MmsTemplateEntity mmsTemplateEntity = mmsTemplateDao.selectOne(Wrappers.lambdaQuery(MmsTemplateEntity.class)
                .eq(MmsTemplateEntity::getType, 3).orderByDesc(MmsTemplateEntity::getCreateTime).last("LIMIT 1"));
        if (Objects.nonNull(mmsTemplateEntity)) {
            List<MmsPaymentItemEntity> entities = new ArrayList<>();
            // 理财师姓名|客户姓名|产品名称|认购金额|第N次付息金额
            String text = "Text1|Text2|Text3|Text4|Text5";
            list.forEach(record -> {
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
                entity.setMmsLogId(logId);
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
            });
            if (!entities.isEmpty()) {
                this.saveBatch(entities);
            }
        }

        return true;
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
            });
            if (!entities.isEmpty()) {
                this.saveBatch(entities);
            }
        }

        return true;
    }
}
