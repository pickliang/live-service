package io.live_mall.modules.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.modules.server.dao.MmsSmsLogDao;
import io.live_mall.modules.server.entity.MmsSmsLogEntity;
import io.live_mall.modules.server.service.MmsSmsLogService;
import org.springframework.stereotype.Service;

/**
 * @author yewl
 * @date 2023/2/25 15:58
 * @description
 */
@Service("mmsSmsLogService")
public class MmsSmsLogServiceImpl extends ServiceImpl<MmsSmsLogDao, MmsSmsLogEntity> implements MmsSmsLogService {
}
