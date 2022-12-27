package io.live_mall.modules.oss.controller;

import com.alibaba.fastjson.JSONObject;
import io.live_mall.common.exception.RRException;
import io.live_mall.common.utils.R;
import io.live_mall.common.utils.ShiroUtils;
import io.live_mall.modules.oss.cloud.OSSFactory;
import io.live_mall.modules.oss.entity.SysOssEntity;
import io.live_mall.modules.oss.service.SysOssService;
import io.live_mall.modules.server.entity.SysTxAiEntity;
import io.live_mall.txai.OCR;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author yewl
 * @date 2022/12/19 16:02
 * @description
 */
@RestController
@RequestMapping("upload")
@Slf4j
public class UploadController {

    @Autowired
    private OCR ocr;
    @Autowired
    private SysOssService sysOssService;

    @PostMapping("/ocr-card")
    public R upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
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

        String data = request.getParameter("data");
        if(StringUtils.isNotBlank(data)) {
            JSONObject parseObject = JSONObject.parseObject(data);
            SysTxAiEntity sysTxAiEntity=null;
            if("card".equals(parseObject.getString("ocr"))){
                sysTxAiEntity = ocr.card(url, file.getName(), ShiroUtils.getUserEntity().getPhone());
            }
            if("bank".equals(parseObject.getString("ocr"))){
                sysTxAiEntity = ocr.bank(url, file.getName(), ShiroUtils.getUserEntity().getPhone());
            }
            return R.ok().put("url", url).put("ocr", sysTxAiEntity);
        }else {
            return R.ok().put("url", url);
        }

    }
}
