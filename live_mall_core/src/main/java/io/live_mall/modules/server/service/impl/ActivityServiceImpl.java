package io.live_mall.modules.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.ActivityDao;
import io.live_mall.modules.server.entity.ActivityEntity;
import io.live_mall.modules.server.model.ActivityModel;
import io.live_mall.modules.server.service.ActivityService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author yewl
 * @date 2023/1/4 14:02
 * @description
 */
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityDao, ActivityEntity> implements ActivityService {
    @Override
    public PageUtils activityPages(Map<String, Object> params) {
        return new PageUtils(this.baseMapper.activityPages(new Query<ActivityModel>().getPage(params), params));
    }

    @Override
    public PageUtils customerActivityPages(Map<String, Object> params) {
        return new PageUtils(this.baseMapper.customerActivityPages(new Query<ActivityModel>().getPage(params), params));
    }
}
