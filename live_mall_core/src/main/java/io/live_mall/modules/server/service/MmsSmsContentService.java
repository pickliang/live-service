package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.MmsSmsContentEntity;

import java.util.Map;

/**
 * @author yewl
 * @date 2023/2/26 14:26
 * @description
 */
public interface MmsSmsContentService extends IService<MmsSmsContentEntity> {
    PageUtils pages(Map<String, Object> params, Long userId);
}
