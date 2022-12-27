package io.live_mall.modules.server.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.live_mall.modules.server.entity.ProductEntity;
import io.live_mall.modules.server.service.ProductService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.R;
import io.live_mall.common.utils.ShiroUtils;

/**
 * 产品列表
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-11-27 12:13:38
 */
@RestController
@RequestMapping("server/product")
public class ProductController {
	@Autowired
	private ProductService productService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("server:product:list")
	public R list(@RequestBody JSONObject params) {
		PageUtils page = productService.queryPage(params);
		return R.ok().put("page", page);
	}
	
	@RequestMapping("/getTj")
	@RequiresPermissions("server:product:list")
	public R getTj(@RequestBody JSONObject params) {
		return R.ok().put("data", productService.getTj(params));
	}


	@RequestMapping("/listApp")
	@RequiresPermissions("server:order:list")
	public R listApp(@RequestBody JSONObject params) {
		params.put("app", "app");
		PageUtils page = productService.selectPageApp(params);
		return R.ok().put("page", page);
	}

	@RequestMapping("/getProductList")
	@RequiresPermissions("server:product:list")
	public R getProductList(@RequestBody JSONObject params) {
		return R.ok().put("data", productService.getProductList(params));
	}

	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("server:product:info")
	public R info(@PathVariable("id") String id) {
		ProductEntity product = productService.getById(id);
		return R.ok().put("product", product);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("server:product:save")
	public R save(@RequestBody ProductEntity product) {
		product.setCreateBy(ShiroUtils.getUserEntity().getRealname());
		product.setCreateDate(new Date());
		product.setUptBy(ShiroUtils.getUserEntity().getRealname());
		product.setUptDate(new Date());
		productService.save(product);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("server:product:update")
	public R update(@RequestBody ProductEntity product) {
		product.setUptBy(ShiroUtils.getUserEntity().getRealname());
		product.setUptDate(new Date());
		productService.updateById(product);
		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("server:product:delete")
	public R delete(@RequestBody String[] ids) {
		productService.removeByIds(Arrays.asList(ids));
		return R.ok();
	}

}
