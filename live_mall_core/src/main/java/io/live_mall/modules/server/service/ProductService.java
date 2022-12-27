package io.live_mall.modules.server.service;

import java.util.List;  

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;

import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.ProductEntity;

/**
 * 产品列表
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-11-27 12:13:38
 */
public interface ProductService extends IService<ProductEntity> {

    PageUtils queryPage(JSONObject params);

	List<ProductEntity> getProductList(JSONObject params);

	PageUtils selectPageApp(JSONObject params);

	JSONObject getTj(JSONObject params);

	void uptStatus(String productId);
}

