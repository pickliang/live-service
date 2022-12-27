package io.live_mall.modules.server.model;

/**
 * @author yewl
 * @date 2022/12/26 16:54
 * @description
 */

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 导入兑付名单实体
 */
@Data
public class DuiFuExcelImporter {
    /**
     * 产品名称
     */
    @ExcelProperty(index = 0)
    private String productName;
    /**
     * 第几期
     */
    @ExcelProperty(index = 1)
    private String stage;
    /**
     * 客户姓名
     */
    @ExcelProperty(index = 2)
    private String customerName;
    /**
     * 区域
     */
    @ExcelProperty(index = 3)
    private String region;
    /**
     * 理财师
     */
    @ExcelProperty(index = 4)
    private String saleName;
    /**
     * 起息日
     */
    @ExcelProperty(index = 5)
    private String valueDate;
    /**
     * 到期日
     */
    @ExcelProperty(index = 6)
    private String dueDate;
    /**
     * 期限
     */
    @ExcelProperty(index = 7)
    private String term;
    /**
     * 预期收益率
     */
    @ExcelProperty(index = 8)
    private String yield;
    /**
     * 身份证
     */
    @ExcelProperty(index = 9)
    private String cardNum;
    /**
     * 认购金额/元
     */
    @ExcelProperty(index = 10)
    private String subscriptionAmount;
    /**
     * 银行账号
     */
    @ExcelProperty(index = 11)
    private String bankNum;
    /**
     * 开户行
     */
    @ExcelProperty(index = 12)
    private String bankName;
    /**
     * 分行
     */
    @ExcelProperty(index = 13)
    private String branchBank;
    /**
     * 第一次付息日
     */
    @ExcelProperty(index = 14)
    private String firstPayMoneyDate;
    /**
     * 第一次付息金额
     */
    @ExcelProperty(index = 15)
    private String firstPayMoney;
    /**
     * 第二次付息日
     */
    @ExcelProperty(index = 16)
    private String secondPayMoneyDate;
    /**
     * 第二次付息金额
     */
    @ExcelProperty(index = 17)
    private String secondPayMoney;
    /**
     * 第三次付息日
     */
    @ExcelProperty(index = 18)
    private String thirdPayMoneyDate;
    /**
     * 第三次付息金额
     */
    @ExcelProperty(index = 19)
    private String thirdPayMoney;
    /**
     * 第四次付息日
     */
    @ExcelProperty(index = 20)
    private String fourthPayMoneyDate;
    /**
     * 第四次付息金额
     */
    @ExcelProperty(index = 21)
    private String fourthPayMoney;
    /**
     * 到期日
     */
    @ExcelProperty(index = 22)
    private String endDate;
    /**
     * 到期还款本息
     */
    @ExcelProperty(index = 23)
    private String principalAndInterest;
    /**
     * 本息合计
     */
    @ExcelProperty(index = 24)
    private String totalPrincipalAndInterest;
    /**
     * 备注
     */
    @ExcelProperty(index = 25)
    private String remarks;
    /**
     * 产品类别
     */
    @ExcelProperty(index = 26)
    private String productCategory;
    /**
     * 贴息金额
     */
    @ExcelProperty(index = 27)
    private String discountAmount;
    /**
     * 转化情况
     */
    @ExcelProperty(index = 28)
    private String conversion;
    /**
     * 到期年份
     */
    @ExcelProperty(index = 29)
    private String expiryYear;
    /**
     * 认购年份
     */
    @ExcelProperty(index = 30)
    private String subscriptionYear;
}
