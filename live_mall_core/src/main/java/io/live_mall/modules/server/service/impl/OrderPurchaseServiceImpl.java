package io.live_mall.modules.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.modules.server.dao.OrderPurchaseDao;
import io.live_mall.modules.server.entity.OrderPurchaseEntity;
import io.live_mall.modules.server.model.BondOrderModel;
import io.live_mall.modules.server.service.OrderPurchaseService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yewl
 * @date 2023/3/11 20:47
 * @description
 */
@Service("orderPurchaseService")
public class OrderPurchaseServiceImpl extends ServiceImpl<OrderPurchaseDao, OrderPurchaseEntity> implements OrderPurchaseService {

    @Override
    public List<JSONObject> purchaseList(String productId, String cardNum) {
        return this.baseMapper.purchaseList(productId, cardNum);
    }

    @Override
    public List<BondOrderModel> customerBonds(String orderId) {
        return this.baseMapper.customerBonds(orderId);
    }
}
