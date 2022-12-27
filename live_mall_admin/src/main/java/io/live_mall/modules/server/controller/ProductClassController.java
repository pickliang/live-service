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

import io.live_mall.modules.server.entity.ProductClassEntity;
import io.live_mall.modules.server.service.ProductClassService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.R;
import io.live_mall.common.utils.ShiroUtils;



/**
 * 产品分类
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-11-27 12:13:38
 */
@RestController
@RequestMapping("server/productclass")
public class ProductClassController {
    @Autowired
    private ProductClassService productClassService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("server:productclass:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = productClassService.queryPage(params);
        return R.ok().put("page", page);
    }
    
    
    @RequestMapping("/getProductClass")
    @RequiresPermissions("server:productclass:list")
    public R getProductClass(){
        return R.ok().put("data", productClassService.list());
    }
    


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("server:productclass:info")
    public R info(@PathVariable("id") String id){
		ProductClassEntity productClass = productClassService.getById(id);
		
        return R.ok().put("productClass", productClass);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("server:productclass:save")
    public R save(@RequestBody ProductClassEntity productClass){
    	productClass.setCreateBy(ShiroUtils.getUserEntity().getRealname());
    	productClass.setCreateDate(new Date());
    	productClass.setUptBy(ShiroUtils.getUserEntity().getRealname());
    	productClass.setUptDate(new Date());
		productClassService.save(productClass);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("server:productclass:update")
    public R update(@RequestBody ProductClassEntity productClass){
      	productClass.setUptBy(ShiroUtils.getUserEntity().getRealname());
    	productClass.setUptDate(new Date());
		productClassService.updateById(productClass);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("server:productclass:delete")
    public R delete(@RequestBody String[] ids){
		productClassService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
