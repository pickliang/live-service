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

import io.live_mall.modules.server.entity.InfoTypeEntity;
import io.live_mall.modules.server.service.InfoTypeService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.R;



/**
 * 这是文件 文件组父子类型表
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-12-02 02:07:10
 */
@RestController
@RequestMapping("server/infotype")
public class InfoTypeController {
    @Autowired
    private InfoTypeService infoTypeService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
    	QueryWrapper<InfoTypeEntity> queryWrapper = new QueryWrapper<InfoTypeEntity>();
    	if(params.get("pid") !=null) {
    		queryWrapper.eq("pid", params.get("pid"));
    	}
        return R.ok().put("data", infoTypeService.list(queryWrapper));
    }
    
    
    

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("server:infotype:info")
    public R info(@PathVariable("id") Integer id){
		InfoTypeEntity infoType = infoTypeService.getById(id);

        return R.ok().put("infoType", infoType);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("server:infotype:save")
    public R save(@RequestBody InfoTypeEntity infoType){
		infoTypeService.save(infoType);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("server:infotype:update")
    public R update(@RequestBody InfoTypeEntity infoType){
		infoTypeService.updateById(infoType);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("server:infotype:delete")
    public R delete(@RequestBody Integer[] ids){
		infoTypeService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
