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

import io.live_mall.modules.server.entity.SysDicTypeEntity;
import io.live_mall.modules.server.service.SysDicTypeService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.R;



/**
 * 
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-11-27 12:21:45
 */
@RestController
@RequestMapping("server/sysdictype")
public class SysDicTypeController {
    @Autowired
    private SysDicTypeService sysDicTypeService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("server:sysdictype:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = sysDicTypeService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("server:sysdictype:info")
    public R info(@PathVariable("id") String id){
		SysDicTypeEntity sysDicType = sysDicTypeService.getById(id);

        return R.ok().put("sysDicType", sysDicType);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("server:sysdictype:save")
    public R save(@RequestBody SysDicTypeEntity sysDicType){
		sysDicTypeService.save(sysDicType);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("server:sysdictype:update")
    public R update(@RequestBody SysDicTypeEntity sysDicType){
		sysDicTypeService.updateById(sysDicType);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("server:sysdictype:delete")
    public R delete(@RequestBody String[] ids){
		sysDicTypeService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
