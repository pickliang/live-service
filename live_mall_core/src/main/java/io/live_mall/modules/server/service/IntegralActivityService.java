package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.IntegralActivityEntity;

import java.util.Map;

/**
 * @author yewl
 * @date 2023/1/30 16:40
 * @description
 */
public interface IntegralActivityService extends IService<IntegralActivityEntity> {
    PageUtils integralActivityPages(Map<String, Object> params);
}
