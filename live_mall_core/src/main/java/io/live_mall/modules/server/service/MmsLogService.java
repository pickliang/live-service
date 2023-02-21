package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.MmsLogEntity;

import java.util.Map;

/**
 * @author yewl
 * @date 2023/2/20 21:02
 * @description
 */
public interface MmsLogService extends IService<MmsLogEntity> {
    PageUtils pages(Map<String, Object> params);
}
