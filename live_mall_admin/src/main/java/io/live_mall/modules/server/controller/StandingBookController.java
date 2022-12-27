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

import io.live_mall.modules.server.entity.StandingBookEntity;
import io.live_mall.modules.server.service.StandingBookService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.R;



/**
 * 台账
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-12-02 02:07:10
 */
@RestController
@RequestMapping("server/standingbook")
public class StandingBookController {
    @Autowired
    private StandingBookService standingBookService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("server:standingbook:list")
    public R list(@RequestParam Map<String, Object> params){
        return R.ok().put("data", standingBookService.list(new QueryWrapper<StandingBookEntity>().orderByDesc("id")));
    }
    
    
    @RequestMapping("/getStandingbookList")
    @RequiresPermissions("server:standingbook:list")
    public R getStandingbookList(@RequestParam Map<String, Object> params){
        return R.ok().put("data", standingBookService.list(new QueryWrapper<StandingBookEntity>().eq("status","1")));
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("server:standingbook:info")
    public R info(@PathVariable("id") Integer id){
		StandingBookEntity standingBook = standingBookService.getById(id);
        return R.ok().put("standingBook", standingBook);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("server:standingbook:save")
    public R save(@RequestBody StandingBookEntity standingBook){
		standingBookService.save(standingBook);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("server:standingbook:update")
    public R update(@RequestBody StandingBookEntity standingBook){
		standingBookService.updateById(standingBook);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("server:standingbook:delete")
    public R delete(@RequestBody Integer[] ids){
		standingBookService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
