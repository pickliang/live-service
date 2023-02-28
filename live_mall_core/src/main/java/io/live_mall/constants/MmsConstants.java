package io.live_mall.constants;

/**
 * @author yewl
 * @date 2023/2/18 21:36
 * @description
 */
public class MmsConstants {

    public static final String USERNAME = "44422-76854854";

    public static final String PASSWORD = "95a37f0f76484d728e883e3fa5382f12";

    private static final String BASE_URL = "http://112.33.33.244:9000/api/v1/";
    /**
     * 获取token
     */
    public static final String TOKEN = BASE_URL + "user/token";

    /**
     * 创建变量 5G 多媒体消息（标准版）
     */
    public static final String VARIABLE_SAVE = BASE_URL + "saveMms/variable/save";

    /**
     * 下发短信任务
     */
    public static final String VARIABLE_SEND = BASE_URL + "sendTask/variableMms/send";
    /**
     *  获取创建短信模板的审核状态
     */
    public static final String MMS_STATUS = BASE_URL + "saveMms/selectStatus";

    /**
     * 短信验证码模板
     * TYPE 1-理财师小程序更新订单验证手机号
     */
    public static final String MMS_SMS_CODE = "1749018";

    /**
     * 内部兑付完成通知
     */
    public static final String CASHING_COMPLETE_MMS_CONTENT = "兑付完成通知：同事您好${Text1}，您名下客户${Text2}认购的${Text3}产品，已完成兑付。兑付金额为：${Text4}元，请您及时做好客户跟进。";
    /**
     * 内部兑付预警通知
     */
    public static final String CASHING_EARLY_WARING_MMS_CONTENT = "兑付预警：同事您好${Text1}，您名下客户${Text2}认购的${Text3}产品，将于${Text4}到期，认购金额为${Text5}元。兑付金额为：${Text6}元，请您及时做好客户跟进。";
    /**
     * 内部付息完成通知
     */
    public static final String PAYMENT_COMPLETE_MMS_CONTENT = "付息完成通知：同事您好${Text1}，您名下客户${Text2}认购的${Text3}产品（认购金额为${Text4}元）付息已完成。付息金额为：${Text5}元，请您及时做好客户跟进。";
    /**
     * 内部付息预警通知
     */
    public static final String PAYMENT_EARLY_WARNING_MMS_CONTENT = "付息预警：同事您好${Text1}，您名下客户${Text2}认购的${Text3}产品，${Text4}利息将于${Text5}进行付息，认购金额为${Text6}元。付息金额为：${Text7}元，请您及时做好客户跟进。";
    /**
     * 积分发放通知
     */
    public static final String INTEGRAL_MMS_CONTENT = "尊敬的${Text1}先生/女士，${Text2}月是您的生日月，道享汇商城祝您生日快乐！特为您奉上生日礼券，您可在道享汇商城中生日专区查看并免费领取商品。感谢您一直以来对五道集团的支持";


}
