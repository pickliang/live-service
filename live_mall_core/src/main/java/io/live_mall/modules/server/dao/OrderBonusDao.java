package io.live_mall.modules.server.dao;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.live_mall.modules.server.entity.OrderBonusEntity;
import io.live_mall.modules.server.model.BondOrderModel;
import io.live_mall.modules.server.model.OrderBonusModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author yewl
 * @date 2023/3/10 22:19
 * @description
 */
@Mapper
public interface OrderBonusDao extends BaseMapper<OrderBonusEntity> {
    IPage<OrderBonusModel> pages(@Param("pages") IPage<OrderBonusModel> pages, @Param("params") Map<String, Object> params);

    IPage<JSONObject> customerPages(@Param("pages") IPage<JSONObject> pages, @Param("type") Integer type, @Param("cardNum") String cardNum);

    List<BondOrderModel> customerBonds(@Param("orderId") String orderId);

    /**
     * 分红收益
     * @param cardNum 身份证
     * @return
     */
    double sumMoney(@Param("cardNum") String cardNum);

    /**
     * 订单转换分红
     * @param orderId
     * @param newOrderId
     * @param cardNum
     */
    void transferStockRight(@Param("orderId") String orderId, @Param("newOrderId") String newOrderId, @Param("cardNum") String cardNum);
    void transferBond(@Param("newOrderId") String newOrderId, @Param("cardNum") String cardNum, @Param("productId") String productId, @Param("oldCardNum") String oldCardNum);
}
