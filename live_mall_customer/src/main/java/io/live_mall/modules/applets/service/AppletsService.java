package io.live_mall.modules.applets.service;

import com.alibaba.fastjson.JSONObject;
import io.live_mall.common.utils.HttpRequestUtils;
import io.live_mall.common.utils.RedisUtils;
import io.live_mall.constants.RedisKeyConstants;
import io.live_mall.wxpay.config.WxAppletsProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

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
    private static final String URL_LINK = "https://api.weixin.qq.com/wxa/generate_urllink?access_token=ACCESS_TOKEN";
    private final RedisUtils redisUtils;
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

    /**
     * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/qrcode-link/url-link/generateUrlLink.html
     * 获取 URL Link
     * @param envVersion
     * @return String
     * @throws IOException
     */
    public String getUrlLink(String envVersion) throws IOException {
        // String accessToken = getAccessToken();
        String accessToken = "65_0IttUb-EeJkEeWaD1X4OtTNP8pHj-CmcQZdY_dA_dZ5lKuSo5__Vq8GJ6ZRCmxyyub17e21Dqi5YMy05dTwm5U0iHepAT_jjq-6yev0y8y53JFf_GjAn5flHTKoHSPcAIABMJ";
        String url = URL_LINK.replace("ACCESS_TOKEN", accessToken);
        JSONObject params = new JSONObject();
        params.put("is_expire", false);
        params.put("expire_type", 1);
        params.put("expire_interval", 30);
        params.put("env_version", envVersion);
        HttpEntity httpEntity = HttpRequestUtils.post(url, params);
        JSONObject result = JSONObject.parseObject(EntityUtils.toString(httpEntity, "UTF-8"));
        log.error("result-->{}", result);
        return result.getString("url_link");
    }

    public String getAccessToken() throws IOException {
        String accessToken = redisUtils.get(RedisKeyConstants.APPLETS_ACCESS_TOKEN);
        if (StringUtils.isBlank(accessToken)) {
            String appid = wxAppletsProperties.getAppid();
            String secret = wxAppletsProperties.getSecret();
            String url = GET_ACCESS_TOKEN_URL.replace("APPID", appid).replace("SECRET", secret);
            HttpEntity httpEntity = HttpRequestUtils.get(url);
            JSONObject result = JSONObject.parseObject(EntityUtils.toString(httpEntity, "UTF-8"));
            log.error("result-->{}", result);
            accessToken = result.getString("access_token");
            Integer expiresIn = result.getInteger("expires_in");
            if (StringUtils.isNotBlank(accessToken)) {
                redisUtils.set(RedisKeyConstants.APPLETS_ACCESS_TOKEN, accessToken, expiresIn);
            }
        }
        return accessToken;
    }



}
