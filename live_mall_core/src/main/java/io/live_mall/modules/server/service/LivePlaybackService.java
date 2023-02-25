package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.LivePlaybackEntity;

import java.util.Map;

/**
 * @author yewl
 * @date 2023/2/25 9:52
 * @description
 */
public interface LivePlaybackService extends IService<LivePlaybackEntity> {
    PageUtils pages(Map<String, Object> params);

    Integer updateIsShow(String id);
}
