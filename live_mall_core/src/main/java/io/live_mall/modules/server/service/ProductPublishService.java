package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.ProductPublishEntity;

import java.util.Map;

/**
 * @author yewl
 * @date 2023/3/12 11:16
 * @description
 */
public interface ProductPublishService extends IService<ProductPublishEntity> {
    PageUtils pages(Map<String, Object> params);
}
