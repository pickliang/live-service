package io.live_mall.modules.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.live_mall.modules.server.entity.OrderRedeemEntity;
import io.live_mall.modules.server.model.BondOrderModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yewl
 * @date 2023/3/11 20:37
 * @description
 */
@Mapper
public interface OrderRedeemDao extends BaseMapper<OrderRedeemEntity> {
    Integer sumPortion(@Param("productId") String productId, @Param("cardNum") String cardNum);

    List<BondOrderModel> customerBonds(@Param("orderId") String orderId);

    /**
     * 赎回收益
     * @param cardNum 身份证
     * @return double
     */
    double sumIncome(@Param("cardNum") String cardNum);
}
