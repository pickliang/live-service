package io.live_mall.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * @author yewl
 * @date 2023/1/16 11:16
 * @description
 */
@Slf4j
public class HttpRequestUtils {
    public static HttpEntity post(String url, JSONObject params) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        try {
            String charset = "UTF-8";
            StringEntity entity = new StringEntity(JSON.toJSONString(params));
            entity.setContentEncoding(charset);
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-Type", "application/json");
            CloseableHttpResponse response = client.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                return response.getEntity();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
