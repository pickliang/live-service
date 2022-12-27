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
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;

import io.live_mall.modules.server.entity.ProductLabelEntity;
import io.live_mall.modules.server.service.ProductLabelService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.R;
import io.live_mall.common.utils.ShiroUtils;



/**
 * 产品分类标签
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-11-27 12:13:38
 */
@RestController
@RequestMapping("server/productlabel")
public class ProductLabelController {
    @Autowired
    private ProductLabelService productLabelService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("server:productlabel:list")
    public R list(@RequestBody  JSONObject params){
        PageUtils page = productLabelService.queryPage(params);
        return R.ok().put("page", page);
    }
    
    @RequestMapping("/getProductLabel")
    @RequiresPermissions("server:productclass:list")
    public R getProductLabel(@RequestParam String onetype){
        return R.ok().put("data", productLabelService.list(new QueryWrapper<ProductLabelEntity>().eq("onetype", onetype)));
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("server:productlabel:info")
    public R info(@PathVariable("id") String id){
		ProductLabelEntity productLabel = productLabelService.getById(id);

        return R.ok().put("productLabel", productLabel);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("server:productlabel:save")
    public R save(@RequestBody ProductLabelEntity productLabel){
    	productLabel.setCreateBy(ShiroUtils.getUserEntity().getRealname());
    	productLabel.setCreateDate(new Date());
    	productLabel.setUptBy(ShiroUtils.getUserEntity().getRealname());
    	productLabel.setUptDate(new Date());
		productLabelService.save(productLabel);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("server:productlabel:update")
    public R update(@RequestBody ProductLabelEntity productLabel){
    	productLabel.setUptBy(ShiroUtils.getUserEntity().getRealname());
    	productLabel.setUptDate(new Date());
    	
		productLabelService.updateById(productLabel);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("server:productlabel:delete")
    public R delete(@RequestBody String[] ids){
		productLabelService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
