package io.live_mall.modules.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.modules.server.dao.InformationDao;
import io.live_mall.modules.server.entity.InformationEntity;
import io.live_mall.modules.server.service.InformationService;
import org.springframework.stereotype.Service;

/**
 * @author yewl
 * @date 2023/1/3 16:20
 * @description
 */
@Service("informationService")
public class InformationServiceImpl extends ServiceImpl<InformationDao, InformationEntity> implements InformationService {
}
