package io.live_mall.modules.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.DateUtils;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.MmsLogItemDao;
import io.live_mall.modules.server.dao.MmsTemplateDao;
import io.live_mall.modules.server.dao.OrderDao;
import io.live_mall.modules.server.entity.MmsLogItemEntity;
import io.live_mall.modules.server.entity.MmsTemplateEntity;
import io.live_mall.modules.server.model.DuiFuNoticeModel;
import io.live_mall.modules.server.service.MmsLogItemService;
import io.live_mall.modules.sys.dao.SysUserDao;
import io.live_mall.modules.sys.entity.SysUserEntity;
import io.live_mall.sms.mms.MmsClient;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author yewl
 * @date 2023/2/20 21:07
 * @description
 */
@Service("mmsLogItemService")
@AllArgsConstructor
public class MmsLogItemServiceImpl extends ServiceImpl<MmsLogItemDao, MmsLogItemEntity> implements MmsLogItemService {
    private final MmsTemplateDao mmsTemplateDao;
    private final OrderDao orderDao;
    private final SysUserDao sysUserDao;
    @Override
    public PageUtils pages(Map<String, Object> params) {
        return new PageUtils(this.baseMapper.pages(new Query<MmsLogItemEntity>().getPage(params), params));
    }

    @Override
    public boolean sendDuifuMms(String token, List<DuiFuNoticeModel> list, String logId, Long userId) {
        MmsTemplateEntity mmsTemplateEntity = mmsTemplateDao.selectOne(Wrappers.lambdaQuery(MmsTemplateEntity.class)
                .eq(MmsTemplateEntity::getType, 1).orderByDesc(MmsTemplateEntity::getCreateTime).last("LIMIT 1"));
        if (Objects.nonNull(mmsTemplateEntity)) {
            List<MmsLogItemEntity> entities = new ArrayList<>();
            // 理财师姓名|客户姓名|产品名称|到期还本付息
            String text = "Text1|Text2|Text3|Text4";
            list.forEach(record -> {
                JSONObject order = orderDao.getOrderById(record.getId());
                SysUserEntity userEntity = sysUserDao.selectById(order.getLong("saleId"));

                String mobile = userEntity.getMobile();
                JSONObject result = null;
                if (StringUtils.isNotBlank(mobile)) {
                    try {
                        StringBuilder sb = new StringBuilder();
                        sb.append(record.getRealname()).append("|").append(record.getCustomerName()).append(("|"))
                                .append(record.getProductName()).append("|").append(record.getSumMoney());
                        result = MmsClient.send(token, text, mobile, sb.toString(), mmsTemplateEntity.getMmsId());
                    } catch (Exception e) {
                        log.error("e-->{}", e);
                    }
                }
                // 保存发送明细
                MmsLogItemEntity entity = new MmsLogItemEntity();
                entity.setMmsLogId(logId);
                entity.setOrderId(record.getId());
                entity.setAppointMoney(record.getAppointMoney() / 10000);
                entity.setCustomerName(record.getCustomerName());
                entity.setCustomerPhone(order.getString("phone"));
                entity.setSaleName(userEntity.getRealname());
                entity.setSaleMobile(mobile);
                entity.setEndDate(record.getDate());
                entity.setType(1);
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

    /**
     * 理财师兑付预警短信通知
     * @return boolean
     */
    @Override
    public boolean sendDuiFuEarlyWarning(String token, List<JSONObject> list) {
        MmsTemplateEntity mmsTemplate = mmsTemplateDao.selectOne(Wrappers.lambdaQuery(MmsTemplateEntity.class)
                .eq(MmsTemplateEntity::getType, 2).orderByDesc(MmsTemplateEntity::getCreateTime).last("LIMIT 1"));
        if (Objects.nonNull(mmsTemplate)) {
            List<MmsLogItemEntity> entities = new ArrayList<>();
            // 理财师姓名|客户姓名|产品名称|到日期|认购金额|到期还本付息元|小程序链接
            String text = "Text1|Text2|Text3|Text4|Text5|Text6";
            list.forEach(record -> {
                String mobile = record.getString("mobile");
                JSONObject result = null;
                if (StringUtils.isNotBlank(mobile)) {
                    try {
                        StringBuilder sb = new StringBuilder();
                        sb.append(record.getString("realname")).append("|").append(record.getString("customer_name")).append(("|"))
                                .append(record.getString("product_name")).append("|").append(record.getString("date")).append("|")
                                .append(record.getInteger("appoint_money")).append("|").append(record.getLong("sum_money"));
                        result = MmsClient.send(token, text, mobile, sb.toString(), mmsTemplate.getMmsId());
                    } catch (Exception e) {
                        log.error("e-->{}", e);
                    }
                }
                // 保存发送明细
                MmsLogItemEntity entity = new MmsLogItemEntity();
                entity.setOrderId(record.getString("order_id"));
                entity.setType(2);
                entity.setAppointMoney(record.getInteger("appoint_money"));
                entity.setCustomerName(record.getString("customer_name"));
                entity.setCustomerPhone(record.getString("phone"));
                entity.setSaleName(record.getString("realname"));
                entity.setSaleMobile(mobile);
                entity.setEndDate(DateUtils.stringToDate(record.getString("date"), DateUtils.DATE_PATTERN));
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
