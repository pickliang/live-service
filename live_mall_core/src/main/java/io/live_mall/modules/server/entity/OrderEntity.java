package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.live_mall.modules.sys.entity.SysUserEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单管理
 * 
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-11-30 22:19:57
 */
@Data
@TableName("p_order")
public class OrderEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId(type =IdType.ASSIGN_ID)
	private String id;
	/**
	 * 产品id
	 */
	private String productId;
	/**
	 * 产品单元id
	 */
	private String productUnitId;
	/**
	 * 募集期id
	 */
	private String raiseId;
	/**
	 * 组织机构id
	 */
	private String orgId;
	/**
	 * 销售人员(用户表的id)
	 */
	private Long saleId;
	/**
	 * 客户姓名
	 */
	private String customerName;
	
	private String  custId;
	/**
	 * 预约金额
	 */
	private Integer appointMoney;
	/**
	 * 预约时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date appointTime;
	/**
	 * 证件类型
	 */
	private String cardType;
	/**
	 * 证件号码
	 */
	private String cardNum;
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
	 * 联系电话
	 */
	private String phone;
	/**
	 * 电子邮箱
	 */
	private String email;
	/**
	 * 协议编号
	 */
	private String agreementNo;
	/**
	 * 签约日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private String signDate;
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
	/**
	 * 开户申请表
	 */
	private String openAccount;
	/**
	 * 认购协议
	 */
	private String subAgree;
	/**
	 * 打款凭条
	 */
	private String paymentSlip;
	/**
	 * 打款时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private String payDate;
	
	/**
	 * 资产证明
	 */
	private String assetsPro;
	/**
	 * 其他附件
	 */
	private String otherFile;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 审核时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date aduitTime;
	/**
	 * 审核人
	 */
	private Long aduitId;
	/**
	 * 驳回理由
	 * 
	 */
	private String aduitResult;
	/**
	 *  -1待报备
	 *  0:待审核  
	 *   1:已驳回
	 *    2：已过审
	 *    3 ：已退款
	 *     4：已成立
	 *      5：已结束
	 *       6：已退回 
	 */
	private Integer status;
	/**
	 * 来源 APP或者PC
	 */
	private String reasourse;
	/**
	 * 订单编号
	 */
	private String orderNo;
	/**
	 * 身份证鉴真 0:非身份证 1：身份证鉴真成功 2：身份证鉴真失败
	 */
	private Integer isCard;
	/**
	 * 开户申请表
	 */
	private String applicationForm;
	/**
	 * 认购协议
	 */
	private String buyDetail;
	/**
	 * 打款凭条
	 */
	private String paymentDetail;
	/**
	 * 资产证明
	 */
	private String assetsDetail;
	/**
	 * 其他附件
	 */
	private String otherDetail;
	/**
	 * 订单排序时间
	 */
	private Date orderDate;
	/**
	 * 
	 */
	private String createBy;
	/**
	 * 
	 */
	private Date createDate;
	
	private String foundedDate;
	
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
	
	
	private String orderStup;
	
	/**
	 * 募集期
	 */
	@TableField(exist = false)
	private RaiseEntity raise;
	
	/**
	 * 产品
	 */
	@TableField(exist = false)
	private ProductEntity product;
	
	/**
	 * 产品单元
	 */
	@TableField(exist = false)
	private ProductUnitEntity productUnit;
	
	
	@TableField(exist = false)
	private SysUserEntity saleUser;
	
	
	@TableField(exist = false)
	private SysUserEntity aduitUser;
	
	@TableField(exist = false)
	SysOrgGroupEntity  sysOrgGroup;
	
	//pass
	@TableField(exist = false)
	private String uptType;
	
	/*@TableField(exist = false)
	private String bhRemark;*/
	
	private BigDecimal rate;
	
	private Integer dateNum;
	
	private String dateUint;
	
	private BigDecimal raseRate;
	
	private BigDecimal yongjinRate;
	
	private Date dateEnd;

	/**
	 * 双录视频地址1
	 */
	private String video;
	/**
	 * 双录视频地址2
	 */
	private String videoUrl;

	/**
	 * 份额
	 */
	private Integer portion;

	/**
	 * 短信验证码
	 */
	@TableField(exist = false)
	private Integer smscode;

	
}
