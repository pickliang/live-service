package io.live_mall.modules.server.dao;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.live_mall.modules.server.entity.OrderPurchaseEntity;
import io.live_mall.modules.server.model.BondOrderModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yewl
 * @date 2023/3/11 20:46
 * @description
 */
@Mapper
public interface OrderPurchaseDao extends BaseMapper<OrderPurchaseEntity> {
    Integer sumPortion(@Param("productId") String productId, @Param("cardNum") String cardNum);

    List<JSONObject> purchaseList(@Param("productId") String productId, @Param("cardNum") String cardNum);

    List<BondOrderModel> customerBonds(@Param("orderId") String orderId);
}
