package io.live_mall.modules.server.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderModel {//身份证鉴真 成立日 投资期限 收益类型 交易类型
    //编号
    private String id;
    //产品分类
    private String classifyName;
    //产品名称
    private String productName;
    //状态
    private Integer status;
    //产品单元
    private String unitName;
    //募集期
    private String raiseName;
    //客户姓名
    private String customerName;
    //产品期限
    private String productTerm;
    //产品id
    private String productId;
    //募集期id
    private String raiseId;
    //预约金额
    private Integer appointMoney;
    //业绩比较基准
    private String incomeData;
    //销售机构
    private String orgName;
    //销售人员
    private String saleName;
    //预约时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date appointTime;
    //审核时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date aduitTime;

    private Date productDate;
    //审核人员
    private String aduitName;
    //业绩系数
    private String perCoefficient;
    //订单来源
    private String reasourse;
    //订单编号
    private String orderNo;

    //身份证鉴真
    private Integer isIdCard;
    //成立日
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date establishTime;

    private String cardType;
    private String cardNum;
    private String cardPhotoR;
    private String cardPhotoL;
    private String cardTime;
    private String phone;
    private String email;
    private String agreementNo;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String signDate;
    private String bankNo;
    private String openBank;
    private String branch;
    private String bankCardBack;
    private String bankCardFront;
    private String openAccount;
    private String subAgree;
    private String paymentSlip;
    private String assetsPro;
    private String otherFile;
    private String remark;
    private Integer aduitId;
    private String aduitResult;
    private Integer isCard;
    private String applicationForm;
    private String buyDetail;
    private String paymentDetail;
    private String assetsDetail;
    private String otherDetail;
    private String productTermtype;
    //交易类型
    private String tranType;

}
