package io.live_mall.tripartite;

import com.alibaba.fastjson.JSONObject;
import io.live_mall.common.utils.HttpRequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author yewl
 * @date 2023/2/23 14:50
 * @description 小鹅通
 */
@Slf4j
public class TouchClients {
    private static final String APP_ID = "appdsEAvHJL1154";
    private static final String CLIENT_ID = "xop9xappWDB3626";
    private static final String SECRET_KEY = "yG5wtowpfSGu7J6H431kpVU9QUdmg29w";
    private static final String ACCESS_TOKEN_URL = "https://api.xiaoe-tech.com/token?app_id=APP_ID&client_id=CLIENT_ID&secret_key=SECRET_KEY&grant_type=client_credential";
    private static final String USER_INFO = "https://api.xiaoe-tech.com/xe.user.info.get/1.0.0";
    private static final String USER_BATCH = "https://api.xiaoe-tech.com/xe.user.batch.get/2.0.0";
    private static final String LEARNING_RECORDS = "https://api.xiaoe-tech.com/xe.user.learning.records.get/1.0.0";
    private static final String LEARNING_RECORDS_DAILY = "https://api.xiaoe-tech.com/xe.learning_records.daily.get/1.0.0";

    public static JSONObject getToken() throws IOException {
        String url = ACCESS_TOKEN_URL.replace("APP_ID", APP_ID).replace("CLIENT_ID", CLIENT_ID).replace("SECRET_KEY", SECRET_KEY);
        HttpEntity httpEntity = HttpRequestUtils.get(url);
        JSONObject result = JSONObject.parseObject(EntityUtils.toString(httpEntity, Charset.defaultCharset()));
        log.error("result-->{}", result);
        Integer code = result.getInteger("code");
        if (0 == code) {
            JSONObject data = result.getJSONObject("data");
            return data;
        }
        return null;
    }

    /**
     * 用户详细信息
     * @param token
     * @param userId
     * @return
     * @throws IOException
     */
    public static JSONObject userInfo(String token, String userId) throws IOException {
        JSONObject params = new JSONObject();
        params.put("access_token", token);
        params.put("user_id", userId);

        JSONObject data = new JSONObject();
        data.put("field_list", Arrays.asList("wx_union_id", "wx_open_id", "wx_app_open_id", "wx_email", "nickname",
                "name", "avatar", "gender", "city","province", "country", "phone", "birth", "address", "company",
                "is_seal", "job", "wx_account", "phone_collect", "sdk_user_id", "created_at"));
        params.put("data", data);
        log.error("params-->{}", params);
        HttpEntity httpEntity = HttpRequestUtils.post(USER_INFO, params);
        JSONObject result = JSONObject.parseObject(EntityUtils.toString(httpEntity, Charset.defaultCharset()));
        log.error("result-->{}", result);
        return result;
    }

    /**
     * 用户列表 获取user_id
     * @param token
     * @param phone
     * @return
     * @throws IOException
     */
    public static JSONObject userList(String token, String phone) throws IOException {
        JSONObject params = new JSONObject();
        params.put("access_token", token);
        params.put("page_size", "1");
        params.put("phone", phone);
        HttpEntity httpEntity = HttpRequestUtils.post(USER_BATCH, params);
        JSONObject result = JSONObject.parseObject(EntityUtils.toString(httpEntity, Charset.defaultCharset()));
        log.error("result-->{}", result);
        return result;
    }

    /**
     * 学习记录
     * @throws IOException
     */
    public static JSONObject learning(String token, String userId, Integer page, Integer pageSize) throws IOException {
        JSONObject params = new JSONObject();
        params.put("access_token", token);
        params.put("user_id", userId);
        JSONObject data = new JSONObject();
        data.put("page", page);
        data.put("page_size", pageSize);
        params.put("data", data);
        HttpEntity httpEntity = HttpRequestUtils.post(LEARNING_RECORDS, params);
        JSONObject result = JSONObject.parseObject(EntityUtils.toString(httpEntity, Charset.defaultCharset()));
        log.error("result-->{}", result);
        return result;
    }

    /**
     * 获取每日学习记录
     * @param token
     * @param userId
     * @return
     * @throws IOException
     */
    public static JSONObject learningDaily(String token, String userId) throws IOException {
        String yesterday = LocalDate.now().plusDays(-1).format(DateTimeFormatter.ISO_DATE);
        JSONObject params = new JSONObject();
        params.put("access_token", token);
        params.put("date", yesterday);
        params.put("user_id_list", Collections.singletonList(userId));
        params.put("page", 1);
        params.put("page_size", 50);
        HttpEntity httpEntity = HttpRequestUtils.post(LEARNING_RECORDS_DAILY, params);
        JSONObject result = JSONObject.parseObject(EntityUtils.toString(httpEntity, Charset.defaultCharset()));
        log.error("result-->{}", result);
        return result;
    }

}
