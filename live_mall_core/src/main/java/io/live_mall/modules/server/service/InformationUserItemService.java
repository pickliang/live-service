package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.InformationUserItemEntity;

import java.util.Map;

/**
 * @author yewl
 * @date 2023/2/27 11:26
 * @description
 */
public interface InformationUserItemService extends IService<InformationUserItemEntity> {
    PageUtils pages(Map<String, Object> params, Long userId);
}
