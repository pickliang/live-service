package io.live_mall.modules.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.modules.server.dao.ActivityDao;
import io.live_mall.modules.server.entity.ActivityEntity;
import io.live_mall.modules.server.service.ActivityService;
import org.springframework.stereotype.Service;

/**
 * @author yewl
 * @date 2023/1/4 14:02
 * @description
 */
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityDao, ActivityEntity> implements ActivityService {
}
