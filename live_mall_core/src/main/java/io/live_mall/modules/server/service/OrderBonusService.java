package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.OrderBonusEntity;
import io.live_mall.modules.server.model.BondOrderModel;

import java.util.List;
import java.util.Map;

/**
 * @author yewl
 * @date 2023/3/10 22:22
 * @description
 */
public interface OrderBonusService extends IService<OrderBonusEntity> {
    PageUtils pages(Map<String, Object> params);

    PageUtils customerPages(Map<String, Object> params, String cardNum);

    List<BondOrderModel> customerBonds(String orderId);

}
