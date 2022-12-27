package io.live_mall.modules.server.entity;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
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
 * @date 2020-11-27 12:21:45
 */
@Data
@TableName("sys_org_user")
public class SysOrgUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String orgId;
	
	@TableId(type = IdType.INPUT)
	private Long userId;
	
	private String orgids;
	
	@TableField(exist = false)
	private JSONObject orgView;
	
	@TableField(exist = false)
	private String[] orgIdArray;
	
	@TableField(exist = false)
	SysOrgGroupEntity  sysOrgGroup;
	
	public  void setOrgids(String orgids) {
		this.orgids=orgids;
		if(StringUtils.isNotBlank(orgids)){
			this.orgIdArray=orgids.split(",");
		}
	}

}
