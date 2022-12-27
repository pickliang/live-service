package io.live_mall.modules.server.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import io.live_mall.modules.server.entity.YjViewEntity;
import io.live_mall.modules.server.service.YjViewService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.R;



/**
 * 
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2021-04-20 01:15:19
 */
@RestController
@RequestMapping("server/yjview")
public class YjViewController {
    @Autowired
    private YjViewService yjViewService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = yjViewService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("server:yjview:info")
    public R info(@PathVariable("id") String id){
		YjViewEntity yjView = yjViewService.getById(id);

        return R.ok().put("yjView", yjView);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("server:yjview:save")
    public R save(@RequestBody YjViewEntity yjView){
		yjViewService.save(yjView);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("server:yjview:update")
    public R update(@RequestBody YjViewEntity yjView){
		yjViewService.updateById(yjView);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("server:yjview:delete")
    public R delete(@RequestBody String[] ids){
		yjViewService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }
    
    @RequestMapping("/yjCreate")
    public R delete(@RequestBody JSONObject prams){
		yjViewService.yjCreate(prams);
        return R.ok();
    }
    

}
