package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 募集期
 * 
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-11-30 22:19:57
 */
@Data
@TableName("p_raise")
public class RaiseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String id;
	/**
	 * 是否在销售端App可见 0：可见 1:不可见
	 */
	private Integer isShow;
	/**
	 * 募集期名称
	 */
	private String raiseName;
	/**
	 * 计划募集额度
	 */
	private Integer raiseMoney;
	/**
	 * 成立规模
	 */
	private String establishScale;
	/**
	 * 投资人名额
	 */
	private Integer investorNum;
	/**
	 * 开始募集时间
	 */
	private Date beginDate;
	/**
	 * 结束募集时间
	 */
	private Date endDate;
	/**
	 * 报名截止时间
	 */
	private String deadlineTime;
	
	
	/**
	 * 报名截止时间类型
	 */
	private String deadlineTimeType;
	/**
	 * 订单标准
	 */
	private String orderStandard;
	/**
	 * 小额订单数
	 */
	private Integer smallOrderNum;
	/**
	 * 大单金额
	 */
	private BigDecimal bigOrderMoney;
	/**
	 * 绑定产品单元
	 */
	private String productUnitId;
	/**
	 * 产品id
	 */
	private String productId;
	/**
	 * 0：年化（按日），1：年化（按月）2：费年化
	 */
	private Integer commissionCalculationMethod;
	/**
	 * 是否是自有销售人员
	 */
	private Integer isMyselfPeople;
	/**
	 * 佣金费率
	 */
	private String commission;
	/**
	 * 业绩系数
	 */
	private String performanceCoefficient;
	/**
	 * 费率说明
	 */
	private String rateDesc;
	/**
	 * -1 草稿 0：待发行 1：发行中 2：已叫停 3：已封账 4：已成立 5：已结束
	 */
	private Integer status;
	
	/**
	 * 成立时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date establishTime;
	/**
	 * 交易类型
	 */
	private String tranType;
	/**
	 * 0：需要上传 1：不需要上传
	 */
	private Integer updateImage;
	/**
	 * 
	 */
	private String createBy;
	/**
	 * 
	 */
	private Date createDate;
	/**
	 * 
	 */
	private String uptBy;
	/**
	 * 
	 */
	private Date uptDate;
	/**
	 * 
	 */
	private String delBy;
	/**
	 * 
	 */
	private Date delDate;
	/**
	 * 删除状态(1删除  0未删除)
	 */
	private Integer delFlag;
	
	
	
	/**
	 * 剩余天数
	 */
	@TableField(exist = false)
	private String syts;
	
	@TableField(exist = false)
	private String bzsysj;

	/**
	 * 剩余金额
	 */
	@TableField(exist = false)
	private Integer syje=0;
	
	
	/**
	 * 完成
	 */
	@TableField(exist = false)
	private Integer wcje=0;
	
	/**
	 * 剩余人数
	 */
	@TableField(exist = false)
	private Integer syrs;
	
	@TableField(exist = false)
	private String uptType;
	/**
	 * 总募集金额
	 */
	@TableField(exist = false)
	private Integer orderNum=0;

	@TableField(exist = false)
	private Integer raiseOrder=0;

	@TableField(exist = false)
	private Integer raiseOrderAll=0;

     @TableField(exist = false)
     private Boolean couldFz;
	
     
 	private String productEndDate;
	

     @TableField(exist = false)
     private ProductEntity product;
     
 	
 	private String method;
 	
 	//按季付息(以每季末月固定日作为付息日)每季度末
 	private Integer dayNum;
 	
 	//按半年付息(以半年末月固定日作为付息日)每半年末月
 	private Integer dayYearNum;
 	
 	private String dfType;
 	
 	
}
