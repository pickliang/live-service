package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.ProductUnitEntity;

import java.util.Map;

/**
 * δΊ§εεε
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-11-27 12:13:38
 */
public interface ProductUnitService extends IService<ProductUnitEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

