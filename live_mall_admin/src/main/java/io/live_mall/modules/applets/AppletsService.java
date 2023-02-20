package io.live_mall.modules.applets;

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
    private static final String URL_LINK = "https://api.weixin.qq.com/wxa/generate_urllink?access_token=ACCESS_TOKEN";
    private final RedisUtils redisUtils;
    private final WxAppletsProperties wxAppletsProperties;


    /**
     * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/qrcode-link/url-link/generateUrlLink.html
     * 获取 URL Link
     * @param envVersion
     * @return String
     * @throws IOException
     */
    public String getUrlLink(String envVersion) throws IOException {
        String accessToken = getAccessToken();
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
