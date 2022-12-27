package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.ProductClassEntity;

import java.util.Map;

/**
 * 产品分类
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-11-27 12:13:38
 */
public interface ProductClassService extends IService<ProductClassEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

