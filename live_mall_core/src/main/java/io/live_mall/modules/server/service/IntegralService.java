package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.IntegralEntity;

import java.util.Map;

/**
 * @author yewl
 * @date 2023/1/30 16:39
 * @description
 */
public interface IntegralService extends IService<IntegralEntity> {
    PageUtils integralPages(Map<String, Object> params);
}
