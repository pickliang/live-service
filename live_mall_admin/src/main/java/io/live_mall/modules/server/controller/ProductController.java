package io.live_mall.modules.server.controller;

import com.alibaba.fastjson.JSONObject;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.R;
import io.live_mall.common.utils.ShiroUtils;
import io.live_mall.modules.server.entity.FundNoticeEntity;
import io.live_mall.modules.server.entity.ProductEntity;
import io.live_mall.modules.server.entity.ProductPublishEntity;
import io.live_mall.modules.server.service.FundNoticeService;
import io.live_mall.modules.server.service.ProductPublishService;
import io.live_mall.modules.server.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 产品列表
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-11-27 12:13:38
 */
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("server/product")
public class ProductController {
	private final ProductService productService;
	private final ProductPublishService productPublishService;
	private final FundNoticeService fundNoticeService;


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

	@GetMapping(value = "/product-name-list")
	@RequiresPermissions("server:product:list")
	public R productNameList(String name, String productClass) {
		List<JSONObject> list = productService.productNameList(name, productClass);
		return R.ok().put("data", list);
	}

	/**
	 * 净值披露列表
	 * @param params
	 * @return
	 */
	@GetMapping(value = "/publish-list")
	public R productPublish(@RequestParam Map<String, Object> params) {
		PageUtils pages = productPublishService.pages(params);
		return R.ok().put("data", pages);
	}

	/**
	 * 净值披露保存或更新
	 * @param entity
	 * @return
	 */
	@PostMapping(value = "/publish")
	public R savePublish(@RequestBody ProductPublishEntity entity) {
		if (StringUtils.isNotBlank(entity.getId())) {
			entity.setUpdateTime(new Date());
			entity.setUpdateUser(ShiroUtils.getUserId());
		}else {
			entity.setCreateTime(new Date());
			entity.setCreateUser(ShiroUtils.getUserId());
		}
		boolean saveOrUpdate = productPublishService.saveOrUpdate(entity);
		return saveOrUpdate ? R.ok() : R.error();
	}

	/**
	 *  净值披露详情
	 * @param id 主键id
	 * @return
	 */
	@GetMapping(value = "/publish-info/{id}")
	public R publishInfo(@PathVariable("id") String id) {
		ProductPublishEntity publish = productPublishService.getById(id);
		if (Objects.nonNull(publish)) {
			ProductEntity product = productService.getById(publish.getProductId());
			if (Objects.nonNull(product)) {
				publish.setProductName(product.getProductName());
			}
		}
		return R.ok().put("data", publish);
	}

	/**
	 * 基金公告列表
	 * @param params
	 * @return
	 */
	@GetMapping(value = "/fund-notice-list")
	public R fundNoticeList(@RequestParam Map<String, Object> params) {
		PageUtils pages = fundNoticeService.pages(params);
		return R.ok().put("data", pages);
	}

	/**
	 * 基金公告保存
	 * @param entity
	 * @return
	 */
	@PostMapping(value = "/fund-notice")
	public R fundNotice(@RequestBody FundNoticeEntity entity) {
		entity.setCreateTime(new Date());
		entity.setCreateUser(ShiroUtils.getUserId());
		boolean save = fundNoticeService.save(entity);
		return save ? R.ok() : R.error();
	}

	/**
	 * 基金公告详情
	 * @param id 主键id
	 * @return
	 */
	@GetMapping(value = "/fund-notice-info/{id}")
	public R fundNoticeInfo(@PathVariable("id") String id) {
		FundNoticeEntity fundNotice = fundNoticeService.getById(id);
		if (Objects.nonNull(fundNotice)) {
			ProductEntity product = productService.getById(fundNotice.getProductId());
			if (Objects.nonNull(product)) {
				fundNotice.setProductName(product.getProductName());
			}
		}
		return R.ok().put("data", fundNotice);
	}
}
