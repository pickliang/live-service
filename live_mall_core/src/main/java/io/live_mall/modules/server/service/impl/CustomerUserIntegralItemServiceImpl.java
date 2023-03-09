package io.live_mall.modules.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.CustomerUserIntegralItemDao;
import io.live_mall.modules.server.dao.IntegralDao;
import io.live_mall.modules.server.dao.OrderDao;
import io.live_mall.modules.server.entity.CustomerUserIntegralItemEntity;
import io.live_mall.modules.server.entity.IntegralEntity;
import io.live_mall.modules.server.entity.OrderEntity;
import io.live_mall.modules.server.service.CustomerUserIntegralItemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author yewl
 * @date 2023/2/2 10:38
 * @description
 */
@Service("customerUserIntegralItemService")
@AllArgsConstructor
public class CustomerUserIntegralItemServiceImpl extends ServiceImpl<CustomerUserIntegralItemDao, CustomerUserIntegralItemEntity> implements CustomerUserIntegralItemService {

    private final IntegralDao integralDao;
    private final OrderDao orderDao;

    @Override
    public String saveCustomerUserIntegralItem(String userId, String cardNum) {
        // 订单
        List<JSONObject> orders = orderDao.integralOrder(Collections.singletonList(cardNum));
        // 积分规则
        IntegralEntity integralEntity = integralDao.selectOne(Wrappers.lambdaQuery(IntegralEntity.class).orderByDesc(IntegralEntity::getCreateTime).last("LIMIT 1"));
        Integer integral = Objects.isNull(integralEntity) ? 0 : integralEntity.getIntegral();
        orders.forEach(order -> {
            String id = order.getString("id");
            String productId = order.getString("product_id");
            Integer appointMoney = order.getInteger("appoint_money");
            Integer dateNum = order.getInteger("date_num");
            String raiseId = order.getString("raise_id");
            CustomerUserIntegralItemEntity integralItem = new CustomerUserIntegralItemEntity();
            integralItem.setCustomerUserId(userId);
            integralItem.setOrderId(id);
            integralItem.setProductId(productId);
            integralItem.setRaiseId(raiseId);
            integralItem.setAppointMoney(appointMoney);
            // 积分规则
            // 1、产品期限 <= 12 个月，投资年化额每一万兑换商城积分数10积分（投资年化额=投资额*产品期限/12)
            // 2、产品期限> 12个月 投资额每一万兑换10积分
            Integer newIntegral = dateNum <= 12 ? (appointMoney * dateNum / 12 * integral) : (appointMoney * integral);
            integralItem.setIntegral(newIntegral);
            integralItem.setDescription("历史订单赠送");
            integralItem.setCreateTime(new Date());
            this.baseMapper.insert(integralItem);
        });
        return null;
    }

    @Override
    public PageUtils customerUserIntegral(Map<String, Object> params, String userId) {
        IPage<JSONObject> pages = this.baseMapper.customerUserIntegralPages(new Query<JSONObject>().getPage(params), userId);
        pages.getRecords().forEach(page -> {
            if (page.getInteger("type") == 2) {
                OrderEntity order = orderDao.selectById(page.getString("order_id"));
                if (Objects.nonNull(order)) {
                    page.put("customer_name", order.getCustomerName());
                }
            }
        });
        return new PageUtils(pages);
    }

    @Override
    public Long customerUserIntegralTotal(String userId) {
        return this.baseMapper.customerUserIntegralTotal(userId);
    }
}
