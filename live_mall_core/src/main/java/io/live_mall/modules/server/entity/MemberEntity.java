package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.live_mall.modules.sys.entity.SysUserEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-12-09 22:21:50
 */
@Data
@TableName("p_member")
public class MemberEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String memberNo;
	/**
	 * 客户名称；
	 */
	private String custName;
	/**
	 * 性别
	 */
	private String sex;
	/**
	 * 证件号
	 */
	private String cardNum;
	/**
	 * 证件类型
	 */
	private String cardType;
	
	
	/**
	 * 证件正面照
	 */
	private String cardPhotoR;
	/**
	 * 证件反面照
	 */
	private String cardPhotoL;
	/**
	 * 证件有效期
	 */
	private String cardTime;
	
	private String cardTimeType;
	
	/**
	 * 银行卡号
	 */
	private String bankNo;
	/**
	 * 开户行
	 */
	private String openBank;
	/**
	 * 分行支行
	 */
	private String branch;
	/**
	 * 银行卡正面
	 */
	private String bankCardFront;
	/**
	 * 银行卡反面
	 */
	private String bankCardBack;
	
	private String assetsPro;
	
	/**
	 * 电子邮箱
	 */
	private String email;
	
	private String assetsDetail;
	
	
	private String phone;
	/**
	 * 销售员
	 */
	private String saleId;
//	private String salename;

	private String birthday;
	
	private String lable;
	
	private String memberType;
	
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
	private String lastDate; //最新投资时间
	@TableField(exist = false)
	private Integer totalMoeny;//总额度
	
	@TableField(exist = false)
	private SysUserEntity salesUser;
	/**
	 * 是否已登录小程序
	 */
	@TableField(exist = false)
	private Boolean isLoginApplet;
}
