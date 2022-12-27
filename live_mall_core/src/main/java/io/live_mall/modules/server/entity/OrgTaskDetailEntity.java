package io.live_mall.modules.server.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author daitao
 * @email 867278141@qq.com
 * @date 2021-04-22 21:38:06
 */
@Data
@TableName("p_org_task_detail")
public class OrgTaskDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String id;
	
	private String taskId;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 月份
	 */
	private String planDate;
	/**
	 * 级别
	 */
	private Integer level;
	
	/**
	 * 
	 */
	@TableField(exist = false)
	private JSONArray data;
	
	private String dataStr;
	
	
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
