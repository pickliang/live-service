package io.live_mall.modules.server.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.live_mall.common.utils.R;
import io.live_mall.common.utils.ShiroUtils;
import io.live_mall.modules.server.entity.MemberTemplateMsgEntity;
import io.live_mall.modules.server.service.MemberTemplateMsgService;
import io.live_mall.modules.sys.entity.SysUserEntity;



/**
 * 会员消息订阅表
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-09-23 22:54:07
 */
@RestController
@RequestMapping("server/membertemplatemsg")
public class MemberTemplateMsgController {
    @Autowired
    private MemberTemplateMsgService memberTemplateMsgService;

    /**
     *  订阅消息保存
     */
    @RequestMapping("/saveList")
    public R saveTemplateMsg( @RequestBody List<MemberTemplateMsgEntity> memberTemplateMsg){
    	SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
    	memberTemplateMsgService.saveTemplateMsg(sysUserEntity,memberTemplateMsg);
        return R.ok();
    }
    
    

    
    

   
}
