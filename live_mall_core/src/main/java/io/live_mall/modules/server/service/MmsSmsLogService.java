package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.modules.server.entity.MmsSmsLogEntity;

/**
 * @author yewl
 * @date 2023/2/25 15:57
 * @description
 */
public interface MmsSmsLogService extends IService<MmsSmsLogEntity> {
    Integer sendCode(String token, String phone, Integer code, Long userId) throws Exception;
}
