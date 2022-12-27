package io.live_mall.modules.server.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 产品列表
 * 
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-11-27 12:13:38
 */
@Data
@TableName("p_product")
public class ProductEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String id;
	/**
	 * 产品名称
	 */
	private String productName;
	/**
	 * 备案号
	 */
	private String recordNo;
	
	private String onetype;
	/**
	 * 产品类别的
	 */
	private String productType;
	/**
	 * 产品分类
	 */
	private String productClass;
	/**
	 * 产品标签
	 */
	private String productLabel;
	/**
	 * 产品简称
	 */
	private String productBrif;
	/**
	 * 融资方
	 */
	private String financingParty;
	/**
	 * 管理人
	 */
	private String managerPeople;
	/**
	 * 托管人
	 */
	private String trustee;
	/**
	 * 货币类型
	 */
	private String currencyType;
	/**
	 * 
	 * 产品规模(发行规模)
	 */
	private String productScale;
	/**
	 * 投资领域
	 */
	private String investmentField;
	/**
	 * 投资方式(投资方向)
	 */
	private String investmentMode;
	/**
	 * 风险等级
	 */
	private String riskLevel;
	/**
	 * 人数限制
	 */
	private String peopleLimit;
	/**
	 * 产品收益 0:业绩比较基准 1：净值
	 */
	private String productRevenue;
	/**
	 * 产品期限 
	 */
	private String productTerm;
	/**
	 * 产品期限分类 0:从首次成立日起算，1：从首次成立日起算最长 2：固定到某个日期截止 3：运作期+退出期+延期退出 4：无固定存续时间
	 */
	private Integer productTermType;
	/**
	 * 期限描述
	 */
	private String termDetail;
	/**
	 * 无固定存蓄时间
	 */
	private String storageTime;
	/**
	 * 开放日内容可能是时间1~时间2 可能是时间
	 */
	private String termTwo;
	/**
	 * 开放日描述
	 */
	private String openHouse;
	/**
	 * App 端开放日展示设定 0:无开放日 1：固定到某个日期截止 2：特点开放日 3：无固定开放日
	 */
	private String openHouseType;
	/**
	 * 投资标的
	 */
	private String investmentTarget;
	/**
	 * 资金用途
	 */
	private String useFound;
	/**
	 * 还款来源
	 */
	private String sourcePay;
	/**
	 * 风控措施
	 */
	private String riskMeasures;
	/**
	 * 产品亮点
	 */
	private String productHighlights;
	/**
	 * 融资方介绍
	 */
	private String introductionDetail;
	/**
	 * 担保方介绍
	 */
	private String guarantorDetail;
	/**
	 * 投资顾问（主承诺商）
	 */
	private String investmentConsultant;
	/**
	 * 基金经理
	 */
	private String fundManager;
	/**
	 * 基金经理介绍
	 */
	private String managerDetail;
	/**
	 * 投资顾介绍（主承诺商介绍）
	 */
	private String consultantDetail;
	/**
	 * 投资组合
	 */
	private String investmentPortfolio;
	/**
	 * 披露频次
	 */
	private String frequency;
	/**
	 * 产品简版(产品附件)
	 */
	private String productEdition;
	/**
	 * 短信简版
	 */
	private String noteEdition;
	/**
	 * 电子合同
	 */
	private String electroniceContract;
	/**
	 * 招募说明书 
	 */
	private String instructions;
	/**
	 * 公式表
	 */
	private String formualTable;
	/**
	 * 其他资料
	 */
	private String otherData;
	/**
	 * 募集账户名称
	 */
	private String raiseBank;
	/**
	 * 募集银行
	 */
	private String raiseCount;
	/**
	 * 募集账号
	 */
	private String raiseAccount;
	/**
	 * 打款备注
	 */
	private String payRemark;
	/**
	 * 认购起点(起投金额)
	 */
	private String startingPoint;
	/**
	 * 递增金额
	 */
	private String incrementalAmount;
	/**
	 * 认购费汇率
	 */
	private String buyRate;
	/**
	 * 认购费描述
	 */
	private String rateDetail;
	/**
	 * 其他汇率
	 */
	private String otherRate;
	/**
	 * 0：未提交  1：可发行 2：发行中  3：已结束  4 已下线
	 */
	private String status;
	/**
	 * 重点产品
	 */
	private String leadproduct;
	/**
	 * 总额度
	 */
	private Integer totalmoney;
	/**
	 * 起投金额
	 */
	private Integer beginmoney;
	/**
	 * 产品成立日
	 */
	private Date productDate;
	
	private Date productEndDate;
	
	private Long dayNum;
	
	/**
	 * 清盘日
	 */
	private Date endDate;
	
	/**
	 * App 端开放日展示设定 的对应值
	 */
	private String houseDetail;
	/**
	 * 认购协议
	 */
	private String agreementDetatil;
	/**
	 * (信托计划说明书)
	 */
	private String prospectus;
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
	
	
	private String gdesc;
	/**
	 * 
	 */
	private Date delDate;
	/**
	 * 删除状态(1删除  0未删除)
	 */
	private Integer delFlag;
	@TableField(exist = false)
	private String mangerName;
	//订单总额			
	@TableField(exist = false)
	private  Integer orderMoenyTotal;
	//订单总数
	@TableField(exist = false)
	private Integer orderNum;
	//当日订单总额
	@TableField(exist = false)
	private  Integer todayOrderMoenyTotal;
	//订单总数
	@TableField(exist = false)
	private Integer todayOrderNum;
	
	
	//#总预约 (万): 
	@TableField(exist = false)
	private Integer zyyje;
	//#总预约订单 (笔)
	@TableField(exist = false)
	private Integer zyyds;
	//# 今日预约 (万)
	@TableField(exist = false)
	private Integer jryyje;
	//#今日预约订单 (笔)
	@TableField(exist = false)
	private Integer jryyds;
	
	//机构
	@TableField(exist = false)
	private Integer xsjgs;
	//客户
	@TableField(exist = false)
	private Integer khs;
	
	/**
	 * 计划募资金额
	 */
	@TableField(exist = false)
	private Integer jhmzje;
	
	@TableField(exist = false)
	List<RaiseEntity> raiseList=new ArrayList<RaiseEntity>();
	
	@TableField(exist = false)
	RaiseEntity raiseRun;
	
	@TableField(exist = false)
	ProductUnitEntity productUnit;
	
}
