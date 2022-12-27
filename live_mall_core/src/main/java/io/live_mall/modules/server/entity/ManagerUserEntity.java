package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 管理人管理
 * 
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-11-27 12:13:39
 */
@Data
@TableName("p_manager_user")
public class ManagerUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String id;
	/**
	 * 管理人名称
	 */
	private String custodianName;
	/**
	 * 登记编号
	 */
	private String registerNo;
	/**
	 * 管理类型
	 */
	private String custodianType;
	/**
	 * 旗下产品数量
	 */
	private Integer productsNum;
	//计划募集额度（¥）			
	@TableField(exist = false)
	private  Integer planRaiseMoenyRmb;
	//总募集额（¥）
	@TableField(exist = false)
	private Integer raiseMoneyTotalRmb;
	//计划募集额度（$）
	@TableField(exist = false)
	private  Integer planRaiseMoenyUsdt;
	//总募集额
	@TableField(exist = false)
	private Integer raiseMoneyTotalUsdt;
	
	/**
	 * 管理人介绍
	 */
	private String managerIntroduction;
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
