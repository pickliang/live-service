package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @date 2021-04-18 17:42:50
 */
@Data
@TableName("p_org_bounty")
public class OrgBountyEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String id;
	/**
	 * 
	 */
	private String name;
	/**
	 * 开始时间
	 */
	
	private String startDate;
	/**
	 * 结束时间
	 */
	private String endDate;
	/**
	 * 区域总完成系数
	 */
	private BigDecimal org2Suc;
	/**
	 * 区域总未成系数
	 */
	private BigDecimal org2Fail;
	/**
	 * 区域副总完成系数
	 */
	private BigDecimal org3Suc;
	/**
	 * 区域福总未成系数
	 */
	private BigDecimal org3Fail;
	/**
	 * 城市总完成系数
	 */
	private BigDecimal org4Suc;
	/**
	 * 城市总未成系数
	 */
	private BigDecimal org4Fail;
	/**
	 * 城市副总完成系数
	 */
	private BigDecimal org5Suc;
	/**
	 * 城市副总未成系数
	 */
	private BigDecimal org5Fail;
	/**
	 * 城市副总完成系数
	 */
	private BigDecimal org6Suc;
	/**
	 * 城市副总未成系数
	 */
	private BigDecimal org6Fail;
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

}
