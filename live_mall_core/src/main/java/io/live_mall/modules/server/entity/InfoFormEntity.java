package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 这是资料库父子文件表
 * 
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-12-02 02:07:10
 */
@Data
@TableName("p_info_form")
public class InfoFormEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 编码
	 */
	@TableId
	private Integer id;
	/**
	 * 文件名
	 */
	private String fileName;
	/**
	 * 摘要
	 */
	private String title;
	/**
	 * 文件路径
	 */
	private String fileUrlId;
	/**
	 * 文件组名称
	 */
	private String typeName;
	/**
	 * 产品资料库类型id
	 */
	private Integer pid;
	
	private Integer typeId;
	/**
	 * 操作人
	 */
	private String updateName;
	/**
	 * 状态(0 废弃  1启用 )
	 */
	private Integer status;
	/**
	 * 文件夹名称
	 */
	private String folderName;
	/**
	 * 0为分享 1不可分享
	 */
	private Integer formShare;
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
