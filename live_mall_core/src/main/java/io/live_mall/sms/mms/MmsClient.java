package io.live_mall.sms.mms;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;
import io.live_mall.common.utils.HttpRequestUtils;
import io.live_mall.common.utils.Md5Util;
import io.live_mall.constants.MmsConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author yewl
 * @date 2023/2/18 21:45
 * @description
 */
@Slf4j
public class MmsClient {

    /**
     * 获取短信的token
     * @return
     * @throws Exception
     */
    public static String getToken() throws Exception {
        TreeMap<String, Object> params = Maps.newTreeMap();

        JSONObject content = new JSONObject();
        content.put("username", MmsConstants.USERNAME);
        content.put("password", MmsConstants.PASSWORD);
        params.put("content", content);

        JSONObject option = new JSONObject();
        option.put("reqtime", System.currentTimeMillis());
        params.put("option", option);
        log.error("params-->{}", params);
        String sign = Md5Util.getMd5Base64(JSON.toJSONString(params));
        params.put("sign", sign);

        HttpEntity httpEntity = HttpRequestUtils.post(MmsConstants.TOKEN, params, null);
        JSONObject result = JSONObject.parseObject(EntityUtils.toString(httpEntity, Charset.defaultCharset()));
        log.error("result-->{}", result);
        JSONObject contentResult = result.getJSONObject("content");
        JSONObject jsonObject = contentResult.getJSONObject("result");
        // 注：token 过期时间为 24 小时，24 小时过期后需重新获取
        String token = jsonObject.getString("token");
        log.error("token-->{}", token);
        return token;
    }



    /**
     * 创建变量文本消息模板 此方法只适合创建文本消息模板
     * @param token token
     * @param subject 短信标题
     * @param fileName 文件名称 xxx.txt
     * @param path 绝对文件路径 "E:\\live_mall\\sms\\xxx.zip" 和 fileName文件名称保持一致
     * @throws Exception
     */
    public static void createVariableMsg(String token, String subject, String fileName, String path) throws Exception {
        // 新建变量 5G 多媒体消息（标准版）接口
        TreeMap<String, Object> params = new TreeMap<>();

        Map<String, Object> content = Maps.newHashMap();

        TreeMap<String, Object> mmsContent = new TreeMap<>();
        mmsContent.put("subject", subject);
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
        File file = new File(path);
        String mmsFile = Hex.encodeHexString(FileUtils.readFileToByteArray(file));
        content.put("mmsFile", mmsFile);
        params.put("content", content);
        Map<String, Object> id = Maps.newHashMap();
        id.put("productId", "320412");
        params.put("id", id);
        Map<String, Object> option = Maps.newHashMap();
        option.put("reqtime", System.currentTimeMillis());
        params.put("option", option);

        String sign = Md5Util.getMd5Base64(JSON.toJSONString(params));

        params.put("sign", sign);
        log.error("params-->{}", JSON.toJSONString(params));
        HttpEntity httpEntity = HttpRequestUtils.post(MmsConstants.VARIABLE_SAVE, params, token);
        JSONObject result = JSONObject.parseObject(EntityUtils.toString(httpEntity, Charset.defaultCharset()));
        log.error("result-->{}", result);
    }

    /**
     * 下发短信
     * @param token token
     * @param text 文本变量 Text1|Text2...格式
     * @param mobile 手机号
     * @param value 变量值 和text变量对应 xxx|xxx...格式
     * @param mmsId 短信模板id 创建模板返回
     * @return
     * @throws Exception
     */
    public static JSONObject send(String token, String text, String mobile, String value, String mmsId) throws Exception {
        TreeMap<String, Object> params = new TreeMap<>();

        Map<String, Object> content = Maps.newHashMap();
        TreeMap<String,Object> varUserElement = new TreeMap<>();
        varUserElement.put("titles", text);
        List<Map<String, Object>> varUsers = new ArrayList<>();
        Map<String, Object> varUser = Maps.newHashMap();
        varUser.put("mobile", mobile);
        varUser.put("varElements", value);
        varUsers.add(varUser);
        varUserElement.put("varUsers", varUsers);
        content.put("userInfos", JSONObject.toJSONString(varUserElement, SerializerFeature.SortField));
        params.put("content", content);

        TreeMap<String, Object> id = new TreeMap<>();
        id.put("mmsId", mmsId);
        id.put("productId", "320412");
        params.put("id", id);

        TreeMap<String, Object> option = new TreeMap<>();
        option.put("reqtime", System.currentTimeMillis());
        option.put("taskNum", UUID.randomUUID().toString());
        option.put("taskType", "0");
        params.put("option", option);

        String sign = Md5Util.getMd5Base64(JSON.toJSONString(params));
        params.put("sign", sign);
        log.error("params-->{}", JSON.toJSONString(params));
        HttpEntity httpEntity = HttpRequestUtils.post(MmsConstants.VARIABLE_SEND, params, token);
        JSONObject result = JSONObject.parseObject(EntityUtils.toString(httpEntity, Charset.defaultCharset()));
        log.error("result-->{}", result);
        return result;
    }
}
