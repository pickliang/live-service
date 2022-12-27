package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.ProductCashEntity;

import java.io.Serializable;
import java.util.Map;

/**
 * 
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2021-02-01 00:48:56
 */
public interface ProductCashService extends IService<ProductCashEntity> {

    PageUtils queryPage(Map<String, Object> params);

	void crateProductCashAll( String prodcut);
}

