/**
 * Copyright (c) 2016-2019 分销峰开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.live_mall.modules.oss.cloud;

import com.aliyun.oss.OSSClient;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 阿里云存储
 *
 * @author Mark sunlightcs@gmail.com
 */
@Slf4j
public class AliyunCloudStorageService extends CloudStorageService {
    private OSSClient client;

    public AliyunCloudStorageService(CloudStorageConfig config){
        this.config = config;

        //初始化
        init();
    }

    private void init(){
        client = new OSSClient(config.getAliyunEndPoint(), config.getAliyunAccessKeyId(),
                config.getAliyunAccessKeySecret());
    }

    @Override
    public String upload(byte[] data, String path) {
        return upload(new ByteArrayInputStream(data), path);
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        // ocr识别需要文件上传成功拿到地址才可以使用，异步上传建议放到具体的业务逻辑上面
        client.putObject(config.getAliyunBucketName(), path, inputStream);
        // try {
        //     new Thread(new Runnable() {
		// 		@Override
		// 		public void run() {
		// 			// TODO Auto-generated method stub
		//         	client.putObject(config.getAliyunBucketName(), path, inputStream);
		// 		}
		// 	}).start();;
        // } catch (Exception e){
        // 	log.error("上传文件失败，请检查配置信息",e);
        //     throw new RRException("上传文件失败，请检查配置信息", e);
        // }

        return config.getAliyunDomain() + "/" + path;
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, getPath(config.getAliyunPrefix(), suffix));
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath(config.getAliyunPrefix(), suffix));
    }
}
