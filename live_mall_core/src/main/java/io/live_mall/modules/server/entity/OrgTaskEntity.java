package io.live_mall.modules.server.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 
 * 
 * @author daitao
 * @email 867278141@qq.com
 * @date 2021-04-18 17:42:50
 */
@Data
@TableName("p_org_task")
public class OrgTaskEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 时间
	 */
	private String year;
	
	private int status;
	
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
	private List<OrgTaskDetailEntity> orgTaskDetailList; 

	@TableField(exist = false)
	private LinkedHashMap<String,List<OrgTaskDetailEntity>>  orgTaskDetailMap=new LinkedHashMap<String, List<OrgTaskDetailEntity>>();
}
