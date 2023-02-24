package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.MmsMemberEntity;

import java.util.Map;

/**
 * @author yewl
 * @date 2023/2/24 15:32
 * @description
 */
public interface MmsMemberService extends IService<MmsMemberEntity> {
    PageUtils pages(Map<String, Object> params);
}
