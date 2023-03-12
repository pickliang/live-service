package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.modules.server.entity.OrderRedeemEntity;
import io.live_mall.modules.server.model.BondOrderModel;

import java.util.List;

/**
 * @author yewl
 * @date 2023/3/11 20:37
 * @description
 */
public interface OrderRedeemService extends IService<OrderRedeemEntity> {
    List<BondOrderModel> customerBonds(String orderId);


}
