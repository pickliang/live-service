package io.live_mall.modules.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.modules.server.dao.InformationLabelDao;
import io.live_mall.modules.server.entity.InformationLabelEntity;
import io.live_mall.modules.server.service.InformationLabelService;
import org.springframework.stereotype.Service;

/**
 * @author yewl
 * @date 2023/1/3 16:19
 * @description
 */
@Service("informationLabelService")
public class InformationLabelServiceImpl extends ServiceImpl<InformationLabelDao, InformationLabelEntity> implements InformationLabelService {
}
