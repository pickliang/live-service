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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.live_mall.modules.server.entity.ProductUnitEntity;
import io.live_mall.modules.server.service.ProductUnitService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.R;
import io.live_mall.common.utils.ShiroUtils;



/**
 * 产品单元
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-11-27 12:13:38
 */
@RestController
@RequestMapping("server/productunit")
public class ProductUnitController {
    @Autowired
    private ProductUnitService productUnitService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("server:productunit:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = productUnitService.queryPage(params);

        return R.ok().put("page", page);
    }
    
    
    @RequestMapping("/getProductUnit")
    @RequiresPermissions("server:productunit:list")
    public R getProductUnit(@RequestParam String productId){
        return R.ok().put("data", productUnitService.getOne(new QueryWrapper<ProductUnitEntity>().eq("product_id",productId)));
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("server:productunit:info")
    public R info(@PathVariable("id") String id){
    	
		ProductUnitEntity productUnit = productUnitService.getById(id);
        return R.ok().put("productUnit", productUnit);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("server:productunit:save")
    public R save(@RequestBody ProductUnitEntity productUnit){
    	productUnit.setCreateBy(ShiroUtils.getUserEntity().getRealname());
    	productUnit.setCreateDate(new Date());
    	productUnit.setUptBy(ShiroUtils.getUserEntity().getRealname());
    	productUnit.setUptDate(new Date());
    	
		productUnitService.save(productUnit);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("server:productunit:update")
    public R update(@RequestBody ProductUnitEntity productUnit){
    	productUnit.setUptBy(ShiroUtils.getUserEntity().getRealname());
    	productUnit.setUptDate(new Date());
		productUnitService.updateById(productUnit);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("server:productunit:delete")
    public R delete(@RequestBody String[] ids){
		productUnitService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
