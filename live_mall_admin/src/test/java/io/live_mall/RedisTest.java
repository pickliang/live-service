package io.live_mall;

import java.io.File;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import io.live_mall.common.utils.RedisUtils;
import io.live_mall.modules.sys.entity.SysUserEntity;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {
	@Autowired
	private RedisUtils redisUtils;
	
	
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	
	
	@Test
	public void contextLoads() {
		SysUserEntity user = new SysUserEntity();
		user.setEmail("qqq@qq.com");
		redisUtils.set("user", user);
		System.out.println(ToStringBuilder.reflectionToString(redisUtils.get("user", SysUserEntity.class)));
	}
	

	
	
	public static void main(String[] args) {
	    // upload_url为上传文件的接口调用地址
		HttpPost httpPost = new HttpPost("http://192.168.190.87:8888/fileManager/upload_one");
		// 使用try resource进行httpClient的关闭
	    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			 // 使用表单形式提交参数
	         MultipartEntityBuilder fileBuilder = MultipartEntityBuilder.create();
	         fileBuilder.setMode(HttpMultipartMode.RFC6532);
	         // file为提交参数名,stream为要上传的文件的文件流 inputStream，最后一个参数为上传文件名称
	         
	         File file = new File("E:\\file\\live_mall.sql");
	         fileBuilder.addBinaryBody("file", file, ContentType.create("multipart/form-data"), "live_mall.sql");
	         HttpEntity fileEntity = fileBuilder.build();
	         httpPost.setEntity(fileEntity);

	         HttpResponse uploadResponse = httpClient.execute(httpPost);
			 StatusLine statusLine = uploadResponse.getStatusLine();
	         // 响应码
	         int statusCode = statusLine.getStatusCode();
	         System.out.println("返回结果为 " + EntityUtils.toString(uploadResponse.getEntity()));
	         // 断开链接
	   } catch (Exception ex) {
	   } finally {
	        if (httpPost != null) {
	              httpPost.releaseConnection();
	       }
	   }

		
	}

	

	
	

}
