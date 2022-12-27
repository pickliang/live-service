/**
 * Copyright (c) 2016-2019 分销峰开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.live_mall.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.live_mall.common.validator.group.AddGroup;
import io.live_mall.common.validator.group.UpdateGroup;
import io.live_mall.modules.server.entity.SysOrgUserEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@TableName("sys_user")
public class SysUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@TableId
	private Long userId;

	/**
	 * 用户名
	 */
	@NotBlank(message = "用户名不能为空", groups = { AddGroup.class, UpdateGroup.class })
	private String username;

	/**
	 * 密码
	 */
	@NotBlank(message = "密码不能为空", groups = AddGroup.class)
	private String password;

	/**
	 * 盐
	 */
	private String salt;

	/**
	 * 邮箱
	 */
	private String email;

	private String otherName;
	/**
	 * 手机号
	 */
	private String mobile;

	/**
	 * 员工编号
	 */
	private String employeeid;

	private String wxId;

	/**
	 * 状态  0：禁用   1：正常
	 */
	private Integer status;

	/**
	 * 角色ID列表
	 */
	@TableField(exist = false)
	private List<Long> roleIdList;

	/**
	 * 创建者ID
	 */
	private Long createUserId;

	/**
	 * 创建时间
	 */
	private Date createTime;

	private String realname;

	private String headerUrl;

	private String gdesc;

	@TableField(exist = false)
	private SysOrgUserEntity org = new SysOrgUserEntity();

	@TableField(exist = false)
	private String orgName;

	/**
	 * 企业名称
	 */
	private String enterpriseName;
	/**
	 * 企业认证 已认证 未认证
	 */
	private String enterpriseRz;
	/**
	 * 办公地址
	 */
	private String address;
	/**
	 * 个人照片
	 */
	private String personPhotos;

	/**
	 * 性别    传汉字
	 */
	private String sexuality;
	/**
	 * 身份证号
	 */
	private Integer numberId;
	/**
	 * 生日
	 */
	private String birthday;
	/**
	 * 籍贯
	 */
	private String nativePlace;
	/**
	 * 所在城市
	 */
	private String location;
	/**
	 * 个人邮箱
	 */
	private String personEmaill;
	/**
	 * 毕业院校
	 */
	private String university;
	/**
	 * 最高学历传 汉字
	 */
	private String highestEducation;
	/**
	 * 入行时间
	 */
	private Date entryTime;
	/**
	 * 所持证书
	 */
	private String certificate;
	/**
	 * 擅长领域
	 */
	private String field;
	/**
	 * 奖励荣誉
	 */
	private String glory;
	/**
	 * 奖励生涯
	 */
	private String career;
	/**
	 * 证书名称
	 */
	private String certificateName;
	/**
	 * 证书编号
	 */
	private Integer certificateNo;
	/**
	 * 证书照片
	 */
	private String certificatePhotos;
	/**
	 * 组织机构id
	 */
	private Integer organizationId;
	/**
	 * 职位
	 */
	private String occupation;
	/**
	 * 工作邮箱
	 */
	private String workEmail;
	/**
	 * 工作手机号
	 */
	private String workPhone;

	private String serviceCode;

	@TableField(exist = false)
	private Integer custNum;

	@TableField(exist = false)
	private Integer achievement;
	@TableField(exist = false)
	private Integer gwdj;
	@TableField(exist = false)
	private Integer grade;

}
