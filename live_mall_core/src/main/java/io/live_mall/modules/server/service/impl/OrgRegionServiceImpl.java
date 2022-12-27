package io.live_mall.modules.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.modules.server.dao.OrgRegionDao;
import io.live_mall.modules.server.entity.OrgRegionEntity;
import io.live_mall.modules.server.service.OrgRegionService;
import org.springframework.stereotype.Service;

/**
 * @author yewl
 * @date 2022/12/22 16:03
 * @description
 */
@Service("orgRegionService")
public class OrgRegionServiceImpl extends ServiceImpl<OrgRegionDao, OrgRegionEntity> implements OrgRegionService {
}
