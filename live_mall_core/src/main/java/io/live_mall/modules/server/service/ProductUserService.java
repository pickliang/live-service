package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.ProductUserEntity;
import io.live_mall.modules.server.entity.RaiseEntity;

import java.util.Map;

/**
 * 
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2021-04-24 22:47:28
 */
public interface ProductUserService extends IService<ProductUserEntity> {

    PageUtils queryPage(Map<String, Object> params);

	void createReiseUser(RaiseEntity raise);
}

