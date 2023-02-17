package io.live_mall.modules.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.modules.server.dao.SysUserArchivesDao;
import io.live_mall.modules.server.entity.SysUserArchivesEntity;
import io.live_mall.modules.server.service.SysUserArchivesService;
import org.springframework.stereotype.Service;

/**
 * @author yewl
 * @date 2023/2/17 16:52
 * @description
 */
@Service("sysUserArchivesService")
public class SysUserArchivesServiceImpl extends ServiceImpl<SysUserArchivesDao, SysUserArchivesEntity> implements SysUserArchivesService {
}
