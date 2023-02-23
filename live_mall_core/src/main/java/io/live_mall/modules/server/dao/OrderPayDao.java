package io.live_mall.modules.server.dao;

import io.live_mall.modules.server.entity.OrderPayEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 结算信息
 * 
 * @author daitao
 * @email 867278141@qq.com
 * @date 2021-02-18 23:16:37
 */
@Mapper
public interface OrderPayDao extends BaseMapper<OrderPayEntity> {
    /**
     * 获取成立日期
     * @param orderId 订单id
     * @return String
     */
	String getEstablishTime(String orderId);

    /**
     * 更新付息日
     * @param id id
     * @param payDate 付息日期
     * @param payMoney 付息金额
     * @return int
     */
    int updateOrderPay(@Param("id") String id, @Param("payDate") String payDate, @Param("payMoney") BigDecimal payMoney);

    /**
     * 付息计划列表
     * @param orderIds 订单id
     * @param startDate yyyy-MM-dd
     * @param endDate yyyy-MM-dd
     * @return
     */
    List<OrderPayEntity> orderPayList(@Param("orderIds") List<String> orderIds, @Param("startDate") String startDate, @Param("endDate") String endDate);


}
