package io.live_mall.modules.server.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.modules.server.entity.OrderPurchaseEntity;
import io.live_mall.modules.server.model.BondOrderModel;

import java.util.List;

/**
 * @author yewl
 * @date 2023/3/11 20:47
 * @description
 */
public interface OrderPurchaseService extends IService<OrderPurchaseEntity> {
    List<JSONObject> purchaseList(String productId, String cardNum);

    List<BondOrderModel> customerBonds(String orderId);
}
