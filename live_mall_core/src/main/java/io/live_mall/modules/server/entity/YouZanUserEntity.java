package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yewl
 * @date 2023/2/19 10:57
 * @description
 */
@Data
@TableName("t_youzan_user")
public class YouZanUserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 有赞用户id，用户在有赞的唯一id。推荐使用
     */
    @Id
    @TableId(value = "yz_open_id", type = IdType.INPUT)
    private String yzOpenId;
    /**
     * 最后购买时间
     */
    private Date lastTradeAt;
    /**
     * 礼品卡余额，单位：分
     */
    private Long giftcardBalance;
    /**
     * 礼品卡余额，单位：分
     */
    private String tags;
    /**
     * 礼品卡余额，单位：分
     */
    private Date createdAt;
    /**
     * 更新时间
     */
    private Date updatedAt;
    /**
     * 更新时间
     */
    private String cards;
    /**
     * 微信头像
     */
    private String wxAvatar;
    /**
     * 成为会员来源渠道 0:其他；100:微信（如不是101，102细分渠道可以统一使用100）;101:微信公众号;102:微信小程序;
     * 103:微信朋友圈;104:微信聊天;105:有赞精选小程序;200:支付宝;300:微博；400:QQ;401:QQ购物号;500:今日头条;600:浏览器;
     * 900:有赞云开放平台;1000:线下门店;1100-后台录入;1200:支付宝小程序;1300:QQ小程序
     */
    private Integer memberSourceChannel;
    /**
     * 会员等级
     */
    private Integer level;
    /**
     * 客户标签ids
     */
    private String tagIds;
    /**
     * 区/县
     */
    private String countyName;
    /**
     * 购买总金额，单位：分
     */
    private Long totalTradeAmount;
    /**
     * 购买次数
     */
    private Integer tradeCount;
    /**
     * 成为会员当时的门店Id
     */
    private Long memberSourceKdtId;
    /**
     * 储值余额，单位：分
     */
    private Long prepaidBalance;
    /**
     * 客户权益
     */
    private String rights;
    /**
     * 市
     */
    private String cityName;
    /**
     * 退款次数
     */
    private Long refundCount;
    /**
     * 统一地区码
     */
    private Long areaCode;
    /**
     * 生日，年-月-日，后面的时分秒无意义
     */
    private String birthday;
    /**
     * 自定义信息项
     */
    private String customerAttrInfos;
    /**
     * 客户联系手机号
     */
    private String mobile;
    /**
     * 头像，和B端店铺后台客户详情头像一致，推荐使用
     */
    private String latestAvatar;
    /**
     * 微信昵称
     */
    private String wxNickname;
    /**
     * 用户C端个人中心优惠券数量（有效期内+已失效的总和）
     */
    private Integer couponNum;
    /**
     * 成为客户来源渠道 0:"其他";1:"未知来源";100:"微信";101:"微信公众号";102:"微信小程序";103:"微信朋友圈";104:"微信聊天";
     * 105:"有赞精选小程序";106:"微信";107:"微信广告";108:"爱逛小程序";200:"支付宝";300:"微博";400:"QQ";401:"QQ购物号";
     * 500:"今日头条";600:"浏览器";700:"APP";800:"其它APP";900:"有赞云开放平台";905:"成为家长创建客户(教育店铺)";
     * 1000:"线下门店";1100:"百度小程序";1200:"支付宝小程序";1300:"QQ小程序";1400:"陌陌";1500:"知乎";1600:"快手";1700:"虎牙";
     * 1800:"斗鱼";1900:"企业微信";2000:"三方门店";2100:"旺小店";2200:"小红书小程序";2300:"抖音poi";2400:"抖音";
     */
    private Integer sourceChannel;
    /**
     * 展示姓名(包含展示逻辑：客户姓名=>手机号=>微信昵称=>匿名用户)
     */
    private String showName;
    /**
     * 客户备注
     */
    private String remark;
    /**
     * 客户性别， 1:男，2:女
     */
    private Integer gender;
    /**
     * 客户姓名
     */
    private String name;
    /**
     * 客户归属店铺，微商城单店返回单店店铺id，连锁店铺返回网店id
     */
    private Long ascriptionKdtId;
    /**
     * 客户积分
     */
    private Long points;
    /**
     * 省
     */
    private String provinceName;
    /**
     * 退款总金额，单位：分
     */
    private Long refundTradeAmount;
    /**
     * 微信号
     */
    private String weiXin;
}
