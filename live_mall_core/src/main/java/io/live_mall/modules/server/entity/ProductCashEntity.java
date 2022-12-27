package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author daitao
 * @email 867278141@qq.com
 * @date 2021-02-01 00:48:56
 */
@Data
@TableName("p_product_cash")
public class ProductCashEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private String id;
	/**
	 * 
	 */
	private String orderNo;
	/**
	 * 产品名称
	 */
	private String productName;
	/**
	 * 期数
	 */
	private String raiseName;
	/**
	 * 客户名称
	 */
	private String customerName;
	/**
	 * 区域
	 */
	private String org;
	/**
	 * 理财师
	 */
	private String realname;
	/**
	 * 身份证号
	 */
	private String cardNum;
	/**
	 * 认购金额/元
	 */
	private Integer appointMoney;
	/**
	 * 银行账号
	 */
	private String bankNo;
	/**
	 * 开户行
	 */
	private String openBank;
	/**
	 * 分行
	 */
	private String branch;
	/**
	 * 起息日
	 */
	private Date startDate;
	/**
	 * 到期日
	 */
	private Date endDate;
	/**
	 * 期限
	 */
	private Integer qsNum;
	/**
	 * 预期收益率
	 */
	private BigDecimal syRate;
	/**
	 * 到期还款本息
	 */
	private BigDecimal endBackMoeny;
	/**
	 * 本息合计
	 */
	private BigDecimal totalMoney;
	/**
	 * 贴息金额
	 */
	private BigDecimal btMoney;
	/**
	 * 付息数据
	 */
	private String data;
	
	@TableField(exist = false)
	private OrderEntity order;

}
