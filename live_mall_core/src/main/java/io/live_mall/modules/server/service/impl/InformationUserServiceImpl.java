package io.live_mall.modules.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.modules.server.dao.InformationUserDao;
import io.live_mall.modules.server.entity.InformationUserEntity;
import io.live_mall.modules.server.service.InformationUserService;
import org.springframework.stereotype.Service;

/**
 * @author yewl
 * @date 2023/2/27 11:25
 * @description
 */
@Service("informationUserService")
public class InformationUserServiceImpl extends ServiceImpl<InformationUserDao, InformationUserEntity> implements InformationUserService {
}
