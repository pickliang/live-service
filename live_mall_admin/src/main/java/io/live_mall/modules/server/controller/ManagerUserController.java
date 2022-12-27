package io.live_mall.modules.server.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.live_mall.modules.server.entity.ManagerUserEntity;
import io.live_mall.modules.server.service.ManagerUserService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.R;
import io.live_mall.common.utils.ShiroUtils;



/**
 * 管理人管理
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-11-27 12:13:39
 */
@RestController
@RequestMapping("server/manageruser")
public class ManagerUserController {
    @Autowired
    private ManagerUserService managerUserService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("server:manageruser:list")
    public R list(@RequestBody JSONObject params){
        PageUtils page = managerUserService.queryPage(params);
        return R.ok().put("page", page);
    }
    
    
    @RequestMapping("/getManageruser")
    @RequiresPermissions("server:productlabel:list")
    public R getManageruser(@RequestParam String custodianType){
        return R.ok().put("data", managerUserService.list(new QueryWrapper<ManagerUserEntity>().eq("custodian_type",custodianType)));
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("server:manageruser:info")
    public R info(@PathVariable("id") String id){
		ManagerUserEntity managerUser = managerUserService.getById(id);

        return R.ok().put("managerUser", managerUser);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("server:manageruser:save")
    public R save(@RequestBody ManagerUserEntity managerUser){
    	
    	managerUser.setCreateBy(ShiroUtils.getUserEntity().getRealname());
    	managerUser.setCreateDate(new Date());
    	managerUser.setUptBy(ShiroUtils.getUserEntity().getRealname());
    	managerUser.setUptDate(new Date());
    	
		managerUserService.save(managerUser);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("server:manageruser:update")
    public R update(@RequestBody ManagerUserEntity managerUser){
		managerUserService.updateById(managerUser);
		managerUser.setUptBy(ShiroUtils.getUserEntity().getRealname());
    	managerUser.setUptDate(new Date());
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("server:manageruser:delete")
    public R delete(@RequestBody String[] ids){
		managerUserService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
