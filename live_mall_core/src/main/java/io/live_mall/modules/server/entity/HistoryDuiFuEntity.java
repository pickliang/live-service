package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yewl
 * @date 2022/12/27 11:13
 * @description
 */
@Data
@TableName("history_duifu")
public class HistoryDuiFuEntity {

    @TableId(type = IdType.INPUT)
    private String id;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 第几期
     */
    private String stage;
    /**
     * 客户姓名
     */
    private String customerName;
    /**
     * 区域
     */
    private String region;
    /**
     * 理财师
     */
    private String saleName;
    /**
     * 起息日
     */
    private String valueDate;
    /**
     * 到期日
     */
    private String dueDate;
    /**
     * 期限
     */
    private String term;
    /**
     * 预期收益率
     */
    // private String yield;
    /**
     * 身份证
     */
    private String cardNum;
    /**
     * 认购金额/元
     */
    private BigDecimal appointMoney;
    /**
     * 银行账号
     */
    private String bankNum;
    /**
     * 开户行
     */
    private String bankName;
    /**
     * 分行
     */
    private String branchBank;
    /**
     * 第一次付息日
     */
    // private String firstPayMoneyDate;
    /**
     * 第一次付息金额
     */
    // private String firstPayMoney;
    /**
     * 第二次付息日
     */
    // private String secondPayMoneyDate;
    /**
     * 第二次付息金额
     */
    // private String secondPayMoney;
    /**
     * 第三次付息日
     */
    // private String thirdPayMoneyDate;
    /**
     * 第三次付息金额
     */
    // private String thirdPayMoney;
    /**
     * 第四次付息日
     */
    // private String fourthPayMoneyDate;
    /**
     * 第四次付息金额
     */
    // private String fourthPayMoney;
    /**
     * 到期日
     */
    private String endDate;
    /**
     * 到期还款本息
     */
    private BigDecimal principalInterest;
    /**
     * 本息合计
     */
    private BigDecimal totalPrincipalInterest;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 产品类别
     */
    private String productCategory;
    /**
     * 贴息金额
     */
    private String discountAmount;
    /**
     * 转化情况
     */
    private String conversion;
    /**
     * 到期年份
     */
    private String expiryYear;
    /**
     * 认购年份
     */
    private String subscriptionYear;

    private Date createTime;
}
