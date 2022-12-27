package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 通知
 * 
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-12-02 02:07:09
 */
@Data
@TableName("p_notice_form")
public class NoticeFormEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@TableId
	private Integer id;
	/**
	 * 通知标题
	 */
	private String noticeTitle;
	/**
	 * 通知类型 (0代表业务通知 , 1代表系统通知)
	 */
	private String noticeType;
	/**
	 * 摘要
	 */
	private String abstractDetail;
	/**
	 * 通知内容
	 */
	private String noticeContent;
	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * 产品id
	 */
	private String productId;
	/**
	 * 关联产品
	 */
	private Integer relationProduct;
	/**
	 * 关联产品描述
	 */
	private String productDetail;
	/**
	 * 用户id
	 */
	private Integer userId;
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
	/**
	 * 文件信息
	 */
	private String fileList;

}
