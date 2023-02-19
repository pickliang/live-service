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


}
