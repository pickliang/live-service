package io.live_mall.txai;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

import com.tencentcloudapi.ocr.v20181119.OcrClient;
import com.tencentcloudapi.ocr.v20181119.models.*;

import io.live_mall.modules.server.entity.SysTxAiEntity;
import io.live_mall.modules.server.service.SysTxAiService;
import lombok.extern.slf4j.Slf4j;;

@Slf4j
@Component
public class OCR {
	
	@Value("${txai.secretId}")
	private String secretId;
	
	@Value("${txai.secretKey}")
	private String secretKey;
	
	@Autowired
	SysTxAiService sysTxAiService;
	
	/**
	 * 身份识别接口
	 * @param imgUrl
	 * @return
	 */
    public  SysTxAiEntity card(String imgUrl,String fileName,String createName) {
        try{
        	SysTxAiEntity sysTxAiEntity = sysTxAiService.getOne(new QueryWrapper<SysTxAiEntity>().eq("file_url", imgUrl));
        	if(sysTxAiEntity != null ){
        		return sysTxAiEntity;
        	}
            Credential cred = new Credential(secretId, secretKey);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("ocr.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            OcrClient client = new OcrClient(cred, "ap-beijing", clientProfile);

            IDCardOCRRequest req = new IDCardOCRRequest();
            req.setImageUrl(imgUrl);
            IDCardOCRResponse resp = client.IDCardOCR(req);
            sysTxAiEntity = new SysTxAiEntity();
            sysTxAiEntity.setCreateBy(createName);
            sysTxAiEntity.setFileUrl(imgUrl);
            sysTxAiEntity.setFileName(fileName);
            sysTxAiEntity.setType(AiConstants.IDCRAD);
            sysTxAiEntity.setData(IDCardOCRResponse.toJsonString(resp));
            sysTxAiService.save(sysTxAiEntity);
            return sysTxAiEntity;
        } catch (TencentCloudSDKException  e) {
            log.error("身份证识别异常",e);   
        }
		return null;
    }
    
    
    public SysTxAiEntity bank(String imgUrl,String fileName,String createName) {
        try{
            Credential cred = new Credential(secretId, secretKey);
            SysTxAiEntity sysTxAiEntity = sysTxAiService.getOne(new QueryWrapper<SysTxAiEntity>().eq("file_url", imgUrl));
        	if(sysTxAiEntity != null ){
        		return sysTxAiEntity;
        	}
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("ocr.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            OcrClient client = new OcrClient(cred, "ap-beijing", clientProfile);

            BankCardOCRRequest req = new BankCardOCRRequest();
            req.setImageUrl(imgUrl);
            BankCardOCRResponse resp = client.BankCardOCR(req);
            sysTxAiEntity = new SysTxAiEntity();
            sysTxAiEntity.setCreateBy(createName);
            sysTxAiEntity.setFileUrl(imgUrl);
            sysTxAiEntity.setFileName(fileName);
            sysTxAiEntity.setType(AiConstants.BANKCRAD);
            sysTxAiEntity.setData(IDCardOCRResponse.toJsonString(resp));
            sysTxAiService.save(sysTxAiEntity);
            return sysTxAiEntity;
        } catch (TencentCloudSDKException e) {
        	  log.error("银行卡识别异常",e);   
        }
		return null;

    }

    
    
    
    
    
    
    
}