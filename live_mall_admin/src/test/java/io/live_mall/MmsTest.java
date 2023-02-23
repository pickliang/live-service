package io.live_mall;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;
import io.live_mall.common.utils.Md5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author yewl
 * @date 2023/2/7 10:05
 * @description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MmsTest {

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJlY0lkIjoiNDE2NDIiLCJ0aW1lIjoxNjc2OTgyNDU2OTk3LCJzaWduIjoiWkNUZlJkaG1PTmJkb0JJYUp1YlAzUSUzRCUzRCIsInJlc291cmNlIjoiW1wic2F2ZU1tc1wiLFwic2VuZFRhc2tcIixcInVzZXJPcmRlclwiLFwiU3luY1Jlc291cmNlXCIsXCJoZWxsb1wiLFwiY21jY1wiXSIsInN1YiI6IjQ0NDIyLTc2ODU0ODU0IiwiZXhwIjoxNjc3MDY4ODU2fQ.ffTpdZHBG_vllR2TNqkzuaZqQHC90tjupWAVitjpeNo";

    @Test
    public void token() throws Exception {
        TreeMap<String, Object> params = Maps.newTreeMap();

        JSONObject content = new JSONObject();
        content.put("username", "44422-76854854");
        content.put("password", "95a37f0f76484d728e883e3fa5382f12");
        params.put("content", content);

        JSONObject option = new JSONObject();
        option.put("reqtime", System.currentTimeMillis());
        params.put("option", option);
        log.error("params-->{}", params);
        String sign = Md5Util.getMd5Base64(JSON.toJSONString(params));
        params.put("sign", sign);
        log.error("sign-->{}", sign);

        String tokenUrl = "http://112.33.33.244:9000/api/v1/user/token";
        HttpEntity httpEntity = this.post(tokenUrl, params, null);
        JSONObject result = JSONObject.parseObject(EntityUtils.toString(httpEntity, Charset.defaultCharset()));
        log.error("result-->{}", result);
        JSONObject contentResult = result.getJSONObject("content");
        JSONObject jsonObject = contentResult.getJSONObject("result");
        // 注：token 过期时间为 24 小时，24 小时过期后需重新获取
        String token = jsonObject.getString("token");
        log.error("token-->{}", token);
    }

    /**
     * ：创建变量 5G 多媒体消息（标准版）
     * @throws Exception
     */
    @Test
    public void createVariableMsg() throws Exception {
        // 新建变量 5G 多媒体消息（标准版）接口
        String url = "http://112.33.33.244:9000/api/v1/saveMms/variable/save";
        TreeMap<String, Object> params = new TreeMap<>();

        Map<String, Object> content = Maps.newHashMap();

        TreeMap<String, Object> mmsContent = new TreeMap<>();
        mmsContent.put("subject", "短信验证码");
        List<Map<String, Object>> frames = new ArrayList<>();
        Map<String, Object> frame = Maps.newHashMap();
        frame.put("index", 0);
        List<Map<String, Object>> attachments = new ArrayList<>();
        Map<String, Object> attachment = Maps.newHashMap();
        attachment.put("index", 0);
        attachment.put("fileName", "codeTemplate.txt");
        attachments.add(attachment);
        frame.put("attachments", attachments);
        frames.add(frame);
        mmsContent.put("frames", frames);
        content.put("mms", JSONObject.toJSONString(mmsContent, SerializerFeature.SortField));
        // cashingComplete "mmsId":"1715788"
        // cashingEarlyWarning "mmsId":"1715792"
        // codeTemplate "mmsId":"1715804"
        // paymentComplete "mmsId":"1715794"
        // paymentEarlyWarning "mmsId":"1715796"
        File file = new File("E:\\live_mall\\sms\\codeTemplate.zip");
        String mmsFile = Hex.encodeHexString(FileUtils.readFileToByteArray(file));
        content.put("mmsFile", mmsFile);
        params.put("content", content);
        Map<String, Object> id = Maps.newHashMap();
        id.put("productId", "320412");
        params.put("id", id);
        Map<String, Object> option = Maps.newHashMap();
        option.put("reqtime", System.currentTimeMillis());
        params.put("option", option);

        log.error("params-->{}", JSON.toJSONString(params));
        String sign = Md5Util.getMd5Base64(JSON.toJSONString(params));

        params.put("sign", sign);
        log.error("sign-->{}", sign);
        log.error("params-->{}", JSON.toJSONString(params));
        HttpEntity httpEntity = this.post(url, params, token);
        JSONObject result = JSONObject.parseObject(EntityUtils.toString(httpEntity, Charset.defaultCharset()));
        log.error("result-->{}", result);

    }

    /**
     * 下发短信任务
     * @throws Exception
     */
    @Test
    public void send() throws Exception {
        String url = "http://112.33.33.244:9000/api/v1/sendTask/variableMms/send";
        TreeMap<String, Object> params = new TreeMap<>();

        Map<String, Object> content = Maps.newHashMap();
        TreeMap<String,Object> varUserElement = new TreeMap<>();
        varUserElement.put("titles", "Text1|Text2");
        List<Map<String, Object>> varUsers = new ArrayList<>();
        Map<String, Object> varUser = Maps.newHashMap();
        varUser.put("mobile", "15039491933");
        varUser.put("varElements", "叶先生|123456");
        varUsers.add(varUser);
        varUserElement.put("varUsers", varUsers);
        content.put("userInfos", JSONObject.toJSONString(varUserElement, SerializerFeature.SortField));
        params.put("content", content);

        TreeMap<String, Object> id = new TreeMap<>();
        id.put("mmsId", "1697800");
        id.put("productId", "320412");
        params.put("id", id);

        TreeMap<String, Object> option = new TreeMap<>();
        option.put("reqtime", System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString();
        log.error("uuid-->{}", uuid);
        option.put("taskNum", uuid);
        option.put("taskType", "0");
        params.put("option", option);

        String sign = Md5Util.getMd5Base64(JSON.toJSONString(params));
        params.put("sign", sign);
        log.error("sign-->{}", sign);
        log.error("params-->{}", JSON.toJSONString(params));
        HttpEntity httpEntity = this.post(url, params, token);
        JSONObject result = JSONObject.parseObject(EntityUtils.toString(httpEntity, Charset.defaultCharset()));
        log.error("result-->{}", result);
    }

    /**
     * 获取创建短信模板的审核状态 可以根据下面的链接看到，不用调用此接口
     * http://v-mas.cn:9000/zmms_cms/index.do?ticket=ST-355-f59WXE9QKSgqtGQr16ShjtmMJBkhYNWsNUq-20
     * @throws Exception
     */
    @Test
    public void getStatus() throws Exception {
        String url = "http://112.33.33.244:9000/api/v1/saveMms/selectStatus";
        TreeMap<String, Object> params = Maps.newTreeMap();
        Map<String, Object> content = Maps.newHashMap();
        params.put("content", content);

        TreeMap<String, Object> id = Maps.newTreeMap();
        id.put("mmsId", "1715792");
        id.put("productId", "320412");
        params.put("id", id);

        Map<String, Object> option = Maps.newHashMap();
        option.put("reqtime", System.currentTimeMillis());
        params.put("option", option);

        String sign = Md5Util.getMd5Base64(JSON.toJSONString(params));
        params.put("sign", sign);
        log.error("sign-->{}", sign);
        log.error("params-->{}", JSON.toJSONString(params));

        HttpEntity httpEntity = this.post(url, params, token);
        JSONObject result = JSONObject.parseObject(EntityUtils.toString(httpEntity, Charset.defaultCharset()));
        log.error("result-->{}", result);
    }



    public static HttpEntity post(String url, Map<String, Object> params, String token) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        try {
            String charset = "UTF-8";
            StringEntity entity = new StringEntity(JSON.toJSONString(params), Charset.defaultCharset());
            entity.setContentEncoding(charset);
            httpPost.setEntity(entity);
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("Authorization", token);
            CloseableHttpResponse response = client.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            log.error("code-->{}", statusCode);
            if (statusCode == HttpStatus.SC_OK) {
                return response.getEntity();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
