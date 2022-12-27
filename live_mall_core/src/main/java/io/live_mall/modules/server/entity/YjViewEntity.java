package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @date 2021-04-20 01:15:19
 */
@Data
@TableName("p_yj_view")
public class YjViewEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String id;
	
	
	
	private String bountyId;
	/**
	 * 
	 */
	private String userName;
	
	@TableField(exist = false)
	private String nickName;
	
	private BigDecimal task;
	/**
	 * 
	 */
	private Long userId;
	
	private String planDate;
	
	private String grade;
	
	private String level;
	
	
	private BigDecimal zbyj;
	/**
	 * 
	 */
	private BigDecimal wcl;
	/**
	 * 
	 */
	private BigDecimal jqxs;
	/**
	 * 
	 */
	private BigDecimal nhyj;
	/**
	 * 
	 */
	private BigDecimal jqje;
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
