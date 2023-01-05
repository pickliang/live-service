package io.live_mall.modules.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.modules.server.dao.ActivityUserDao;
import io.live_mall.modules.server.entity.ActivityUserEntity;
import io.live_mall.modules.server.service.ActivityUserService;
import org.springframework.stereotype.Service;

/**
 * @author yewl
 * @date 2023/1/5 16:57
 * @description
 */
@Service("activityUserService")
public class ActivityUserServiceImpl extends ServiceImpl<ActivityUserDao, ActivityUserEntity> implements ActivityUserService {
}
