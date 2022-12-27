package io.live_mall.modules.server.entity;

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
 * @date 2020-11-27 12:21:45
 */
@Data
@TableName("sys_dic_detail")
public class SysDicDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private String id;
	/**
	 * 
	 */
	private String typeId;
	/**
	 * 
	 */
	private String name;
	/**
	 * 
	 */
	private String remark;

}
