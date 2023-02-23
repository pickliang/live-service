package io.live_mall.modules.server.model;

import lombok.Data;

import java.util.Date;

/**
 * @author yewl
 * @date 2023/2/22 20:44
 * @description
 */
@Data
public class YouZanUserModel {
    private String yzOpenId;
    /**
     * 成为会员来源渠道 0:其他；100:微信（如不是101，102细分渠道可以统一使用100）;101:微信公众号;102:微信小程序;
     * 103:微信朋友圈;104:微信聊天;105:有赞精选小程序;200:支付宝;300:微博；400:QQ;401:QQ购物号;500:今日头条;600:浏览器;
     * 900:有赞云开放平台;1000:线下门店;1100-后台录入;1200:支付宝小程序;1300:QQ小程序
     */
    private Integer memberSourceChannel;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 客户联系手机号
     */
    private String mobile;
    /**
     * 生日，年-月-日，后面的时分秒无意义
     */
    private String birthday;
    /**
     * 客户性别， 1:男，2:女
     */
    private Integer gender;
    /**
     * 省
     */
    private String provinceName;
    /**
     * 市
     */
    private String cityName;
    /**
     * 区/县
     */
    private String countyName;
    /**
     * 客户标签
     */
    private String tags;
    /**
     * 客户积分
     */
    private Long points;

    /**
     * 累计消费金额
     */
    private double consumerAmount;
    /**
     * 累计消费订单数
     */
    private long consumerOrderNum;
    /**
     * 最近下单时间
     */
    private Date recentlyOderTime;
    /**
     * 累计退款金额
     */
    // private double refundAmount;
    /**
     * 累计退款单数
     */
    // private long refundOrderNum;
}
