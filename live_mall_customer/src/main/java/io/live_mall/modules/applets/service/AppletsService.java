package io.live_mall.modules.applets.service;

import com.alibaba.fastjson.JSONObject;
import io.live_mall.common.utils.HttpRequestUtils;
import io.live_mall.common.utils.RedisUtils;
import io.live_mall.modules.redis.RedisKeyConstants;
import io.live_mall.wxpay.config.WxAppletsProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * @author yewl
 * @date 2022/12/28 13:04
 * @description
 */
@Service
@AllArgsConstructor
@Slf4j
public class AppletsService {
    private static final String GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET";
    private static final String GET_PHONE_NUMBER_URL = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=ACCESS_TOKEN";
    private static final String CODE_UN_LIMIT = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=ACCESS_TOKEN";
    private final RedisUtils redisUtils;
    private final RestTemplate restTemplate;
    private final WxAppletsProperties wxAppletsProperties;

    public String getPhoneNumber(String code) throws IOException {
        String accessToken = getAccessToken();
        String url = GET_PHONE_NUMBER_URL.replace("ACCESS_TOKEN", accessToken);
        JSONObject json = new JSONObject();
        json.put("code", code);
        HttpEntity httpEntity= HttpRequestUtils.post(url, json);
        JSONObject result = JSONObject.parseObject(EntityUtils.toString(httpEntity, "UTF-8"));
        JSONObject phoneInfo = result.getJSONObject("phone_info");
        return phoneInfo.getString("purePhoneNumber");
    }

    public byte[] getUnlimitedQRCode(String scene, String page, Boolean checkPath, String  envVersion) throws IOException {
        String accessToken = getAccessToken();
        String url = CODE_UN_LIMIT.replace("ACCESS_TOKEN", accessToken);
        JSONObject params = new JSONObject();
        params.put("scene", scene);
        params.put("page", page);
        params.put("check_path", checkPath);
        params.put("env_version", envVersion);
        HttpEntity httpEntity = HttpRequestUtils.post(url, params);
        return EntityUtils.toByteArray(httpEntity);
    }

    // public byte[] getUnlimitedQRCode(String scene, String page, Boolean checkPath, String  envVersion) {
    //     CloseableHttpClient client = HttpClients.createDefault();
    //     String accessToken = getAccessToken();
    //     String url = CODE_UN_LIMIT.replace("ACCESS_TOKEN", accessToken);
    //     HttpPost httpPost = new HttpPost(url);
    //     JSONObject params = new JSONObject();
    //     params.put("scene", scene);
    //     params.put("page", page);
    //     params.put("check_path", checkPath);
    //     params.put("env_version", envVersion);
    //     try {
    //         String charset = "UTF-8";
    //         StringEntity entity = new StringEntity(JSON.toJSONString(page));
    //         entity.setContentEncoding(charset);
    //         httpPost.setEntity(entity);
    //         httpPost.setHeader("Content-Type", "application/json");
    //         CloseableHttpResponse response = client.execute(httpPost);
    //         int statusCode = response.getStatusLine().getStatusCode();
    //         if (statusCode == HttpStatus.SC_OK) {
    //             return EntityUtils.toByteArray(response.getEntity());
    //         }
    //     }catch (Exception e) {
    //         e.printStackTrace();
    //     }finally {
    //         httpPost.releaseConnection();
    //     }
    //     return null;
    // }

    public String getAccessToken() {
        String accessToken = redisUtils.get(RedisKeyConstants.APPLETS_ACCESS_TOKEN);
        if (StringUtils.isBlank(accessToken)) {
            String appid = wxAppletsProperties.getAppid();
            String secret = wxAppletsProperties.getSecret();
            String url = GET_ACCESS_TOKEN_URL.replace("APPID", appid).replace("SECRET", secret);
            ResponseEntity<JSONObject> response = restTemplate.getForEntity(url, JSONObject.class);
            JSONObject body = response.getBody();
            log.error("token-->{}", body);
            accessToken = body.getString("access_token");
            Integer expiresIn = body.getInteger("expires_in");
            if (StringUtils.isNotBlank(accessToken)) {
                redisUtils.set(RedisKeyConstants.APPLETS_ACCESS_TOKEN, accessToken, expiresIn);
            }
        }
        return accessToken;
    }



}
