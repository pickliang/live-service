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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;

import io.live_mall.modules.server.entity.SysDicDetailEntity;
import io.live_mall.modules.server.service.SysDicDetailService;
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
@RequestMapping("server/sysdicdetail")
public class SysDicDetailController {
    @Autowired
    private SysDicDetailService sysDicDetailService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("server:sysdicdetail:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = sysDicDetailService.queryPage(params);
        return R.ok().put("page", page);
    }
    
    
    @RequestMapping("/getDicList")
    @RequiresPermissions("server:sysdicdetail:list")
    public R getDicList(@RequestParam Map<String, Object> params){
    	QueryWrapper<SysDicDetailEntity> queryWrapper = new QueryWrapper<SysDicDetailEntity>();
    	queryWrapper.eq("type_id", params.get("typeId"));
        return R.ok().put("data", sysDicDetailService.list(queryWrapper));
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("server:sysdicdetail:info")
    public R info(@PathVariable("id") String id){
		SysDicDetailEntity sysDicDetail = sysDicDetailService.getById(id);

        return R.ok().put("sysDicDetail", sysDicDetail);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("server:sysdicdetail:save")
    public R save(@RequestBody SysDicDetailEntity sysDicDetail){
		sysDicDetailService.save(sysDicDetail);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("server:sysdicdetail:update")
    public R update(@RequestBody SysDicDetailEntity sysDicDetail){
		sysDicDetailService.updateById(sysDicDetail);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("server:sysdicdetail:delete")
    public R delete(@RequestBody String[] ids){
		sysDicDetailService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
