package io.live_mall.modules.server.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.live_mall.modules.sys.entity.SysUserEntity;
import lombok.Data;

/**
 * 机构组织管理
 * 
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-11-27 12:21:45
 */
@Data
@TableName("sys_org_group")
public class SysOrgGroupEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String id;
	/**
	 * 机构名称
	 */
	private String organizationName;
	/**
	 * 负责人姓名
	 */
	private String contactPerson;
	/**
	 * 状态(0启用中  1已停用 2是删除)
	 */
	private Integer status;
	/**
	 * 组织机构简称
	 */
	private String organizationAbbreviation;
	/**
	 * 人员数量
	 */
	private Integer peopleNum;
	/**
	 * 下级人员总数
	 */
	private Integer totalPeople;
	/**
	 * 负责人
	 */
	private Long personCharge;
	/**
	 * 所在地
	 */
	private String location;
	/**
	 * 职场所在地
	 */
	private String workLocation;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 父级id
	 */
	private String parentId;
	
	@TableField(exist = false)
	private String ids;
	/**
	 * 层次关系标签
	 */
	private Integer level;
	
	private Integer grade;
	
	private Integer gwdj;
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
	
	private String locationCode;
	
	/**
	 * 删除状态(1删除  0未删除)
	 */
	private Integer delFlag;
	
	@TableField(exist = false)
	private List<SysOrgGroupEntity> childrenList=new ArrayList<SysOrgGroupEntity>();
	
	/**
	 * 机构人员
	 */
	@TableField(exist = false)
	private List<SysUserEntity>   sysuserList=new ArrayList<SysUserEntity>();
	
	/**
	 * 
	 */
	@TableField(exist = false)
	private SysUserEntity  sysUserEntity;

	

}
