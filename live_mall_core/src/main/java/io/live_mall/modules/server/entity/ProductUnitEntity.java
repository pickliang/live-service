package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 产品单元
 * 
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-11-27 12:13:38
 */
@Data
@TableName("p_product_unit")
public class ProductUnitEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String id;
	/**
	 * 产品单元名称
	 */
	private String unitName;
	/**
	 * 收益分配描述
	 */
	private String incomeDesc;
	/**
	 * 0:展示业绩比较基准，如固定收益 1:展示业绩比较基准及超额收益分配比例，如固定+浮动 2:浮动收益
	 */
	private Integer incomeType;
	/**
	 * 数据
	 */
	private String incomeData;
	
	@TableField(exist = false)
	private String incomeDataMinMax;
	/**
	 * 期限描述
	 */
	private String deadlineDesc;
	/**
	 * App端产品单元的期限展示设定 0:与产品端一致 1：从成立日起算 2：从成立日起算最长 3：固定到某个日期截止 4：无固定投资期限
	 */
	private Integer deadlineSetType;
	/**
	 * 期限数据
	 */
	private String deadlineData;
	/**
	 * 产品id
	 */
	private String productId;
	/**
	 * 封闭期描述
	 */
	private String closedDetail;
	/**
	 * 0:与投资期限一致 ;1从成立日/生效日起算(认购/申购);2;封闭到某个日期;3无封闭期
	 */
	private Integer closeType;
	/**
	 * 滚动续存描述
	 */
	private String rollDetail;
	/**
	 * 0:不可滚动续存 ; 1可滚动续存（收益率根据新一期产品单元设定）;2;可滚动续存（在上一期收益率基础上浮）
	 */
	private Integer rollType;
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
	
	@TableField(exist = false)
	private Integer syje;
	@TableField(exist = false)
	private ProductEntity product;

}
