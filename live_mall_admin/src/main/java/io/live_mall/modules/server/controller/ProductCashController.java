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

import io.live_mall.modules.server.entity.ProductCashEntity;
import io.live_mall.modules.server.service.ProductCashService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.R;

/**
 * 
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2021-02-01 00:48:56
 */
@RestController
@RequestMapping("server/productcash")
public class ProductCashController {
    @Autowired
    private ProductCashService productCashService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("server:productcash:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = productCashService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("server:productcash:info")
    public R info(@PathVariable("id") String id){
		ProductCashEntity productCash = productCashService.getById(id);
        return R.ok().put("productCash", productCash);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("server:productcash:save")
    public R save(@RequestBody ProductCashEntity productCash){
		productCashService.save(productCash);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("server:productcash:update")
    public R update(@RequestBody ProductCashEntity productCash){
		productCashService.updateById(productCash);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("server:productcash:delete")
    public R delete(@RequestBody String[] ids){
		productCashService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
