package io.live_mall.modules.server.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.OrderDao;
import io.live_mall.modules.server.dao.OrderPayDao;
import io.live_mall.modules.server.entity.OrderEntity;
import io.live_mall.modules.server.entity.OrderPayEntity;
import io.live_mall.modules.server.service.OrderPayService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Service("orderPayService")
@AllArgsConstructor
public class OrderPayServiceImpl extends ServiceImpl<OrderPayDao, OrderPayEntity> implements OrderPayService {

    private final OrderDao orderDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderPayEntity> page = this.page(
                new Query<OrderPayEntity>().getPage(params),
                new QueryWrapper<OrderPayEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void updatePayDate(List<JSONObject> params) {
        JSONObject json = params.get(0);
        String orderPayId = json.getString("id");
        OrderPayEntity payEntity = this.baseMapper.selectById(orderPayId);
        // 成立时间
        String establishTime = this.baseMapper.getEstablishTime(payEntity.getOrderId());
        // 订单
        OrderEntity order = orderDao.selectById(payEntity.getOrderId());
        params.forEach(pay -> {
            String id = pay.getString("id");
            // 利息支付日
            String payDate = pay.getString("payDate");
            // 利息=认购金额*年化收益率/365*（利息支付日-成立日）
            BigDecimal payMoney = new BigDecimal(order.getAppointMoney() * 10000 * payEntity.getRate().doubleValue() / 100 / 365 * DateUtil.betweenDay(DateUtil.parseDate(establishTime), DateUtil.parseDate(payDate), true));
            this.baseMapper.updateOrderPay(id, payDate, payMoney);
        });
    }

}