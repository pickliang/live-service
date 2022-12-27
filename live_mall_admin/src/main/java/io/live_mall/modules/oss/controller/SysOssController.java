/**
 * Copyright (c) 2016-2019 分销峰开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.live_mall.modules.oss.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import io.live_mall.common.exception.RRException;
import io.live_mall.common.utils.ConfigConstant;
import io.live_mall.common.utils.Constant;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.R;
import io.live_mall.common.utils.ShiroUtils;
import io.live_mall.common.validator.ValidatorUtils;
import io.live_mall.common.validator.group.AliyunGroup;
import io.live_mall.common.validator.group.QcloudGroup;
import io.live_mall.common.validator.group.QiniuGroup;
import io.live_mall.modules.oss.cloud.CloudStorageConfig;
import io.live_mall.modules.oss.cloud.OSSFactory;
import io.live_mall.modules.oss.entity.SysOssEntity;
import io.live_mall.modules.oss.service.SysOssService;
import io.live_mall.modules.server.entity.SysTxAiEntity;
import io.live_mall.modules.sys.service.SysConfigService;
import io.live_mall.txai.OCR;

/**
 * 文件上传
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("sys/oss")
public class SysOssController {
	
	
	
	
	@Autowired
	private SysOssService sysOssService;
    @Autowired
    private SysConfigService sysConfigService;
    
    @Autowired
    private OCR ocr;

    private final static String KEY = ConfigConstant.CLOUD_STORAGE_CONFIG_KEY;
	
	/**
	 * 列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:oss:all")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = sysOssService.queryPage(params);

		return R.ok().put("page", page);
	}


    /**
     * 云存储配置信息
     */
    @GetMapping("/config")
    @RequiresPermissions("sys:oss:all")
    public R config(){
        CloudStorageConfig config = sysConfigService.getConfigObject(KEY, CloudStorageConfig.class);

        return R.ok().put("config", config);
    }


	/**
	 * 保存云存储配置信息
	 */
	@PostMapping("/saveConfig")
	@RequiresPermissions("sys:oss:all")
	public R saveConfig(@RequestBody CloudStorageConfig config){
		//校验类型
		ValidatorUtils.validateEntity(config);

		if(config.getType() == Constant.CloudService.QINIU.getValue()){
			//校验七牛数据
			ValidatorUtils.validateEntity(config, QiniuGroup.class);
		}else if(config.getType() == Constant.CloudService.ALIYUN.getValue()){
			//校验阿里云数据
			ValidatorUtils.validateEntity(config, AliyunGroup.class);
		}else if(config.getType() == Constant.CloudService.QCLOUD.getValue()){
			//校验腾讯云数据
			ValidatorUtils.validateEntity(config, QcloudGroup.class);
		}

        sysConfigService.updateValueByKey(KEY, new Gson().toJson(config));

		return R.ok();
	}
	

	/**
	 * 上传文件
	 */
	@PostMapping("/upload")
	@RequiresPermissions("sys:oss:all")
	public R upload(@RequestParam("file") MultipartFile file,HttpServletRequest reqest) throws Exception {
		if (file.isEmpty()) {
			throw new RRException("上传文件不能为空");
		}
		//上传文件
		String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		String url = OSSFactory.build().uploadSuffix(file.getBytes(), suffix);
		//保存文件信息
		SysOssEntity ossEntity = new SysOssEntity();
		ossEntity.setUrl(url);
		ossEntity.setCreateDate(new Date());
		sysOssService.save(ossEntity);
		
		String data = reqest.getParameter("data");
		if(StringUtils.isNotBlank(data)) {
			JSONObject parseObject = JSONObject.parseObject(data);
			SysTxAiEntity sysTxAiEntity=null;
			if("card".equals(parseObject.getString("ocr"))){
				sysTxAiEntity = ocr.card(url, file.getName(), ShiroUtils.getUserEntity().getRealname());
			}
			if("bank".equals(parseObject.getString("ocr"))){
				sysTxAiEntity = ocr.bank(url, file.getName(), ShiroUtils.getUserEntity().getRealname());
			}
			return R.ok().put("url", url).put("ocr", sysTxAiEntity);
		}else {
			return R.ok().put("url", url);
		}
		
	}
	
	
	

	private static ByteArrayOutputStream cloneInputStream(InputStream input) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len;
			while ((len = input.read(buffer)) > -1) {
				baos.write(buffer, 0, len);
			}
			baos.flush();
			return baos;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 删除
	 */
	@PostMapping("/delete")
	@RequiresPermissions("sys:oss:all")
	public R delete(@RequestBody Long[] ids){
		sysOssService.removeByIds(Arrays.asList(ids));
		return R.ok();
	}

	
	
	
	
}



















