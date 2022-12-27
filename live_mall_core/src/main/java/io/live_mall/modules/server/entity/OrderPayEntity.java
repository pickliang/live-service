package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 结算信息
 * 
 * @author daitao
 * @email 867278141@qq.com
 * @date 2021-02-18 23:16:37
 */
@Data
@TableName("p_order_pay")
public class OrderPayEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String id;
	/**
	 * 订单号
	 */
	private String orderId;
	/**
	 * 支付金额
	 */
	private BigDecimal payMoney;
	/**
	 * 支付时间
	 */
	private String payDate;
	/**
	 * 名称
	 */
	private String name;
	
	private BigDecimal rate;
	
	private Integer num;
	
}
