package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 台账
 * 
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-12-02 02:07:10
 */
@Data
@TableName("p_standing_book")
public class StandingBookEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@TableId
	private Integer id;
	/**
	 * 文件名称
	 */
	private String name;
	/**
	 * 状态(0是不启用,1是启用)
	 */
	private Integer status;
	/**
	 * 文件路径
	 */
	private String nameUrl;
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
