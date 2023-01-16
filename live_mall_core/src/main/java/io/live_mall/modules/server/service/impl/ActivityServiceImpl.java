package io.live_mall.modules.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.common.utils.R;
import io.live_mall.modules.server.dao.ActivityDao;
import io.live_mall.modules.server.dao.ActivityUserDao;
import io.live_mall.modules.server.entity.ActivityEntity;
import io.live_mall.modules.server.entity.ActivityUserEntity;
import io.live_mall.modules.server.model.ActivityModel;
import io.live_mall.modules.server.service.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author yewl
 * @date 2023/1/4 14:02
 * @description
 */
@Service
@AllArgsConstructor
public class ActivityServiceImpl extends ServiceImpl<ActivityDao, ActivityEntity> implements ActivityService {
    private final ActivityUserDao activityUserDao;
    @Override
    public PageUtils activityPages(Map<String, Object> params) {
        return new PageUtils(this.baseMapper.activityPages(new Query<ActivityModel>().getPage(params), params));
    }

    @Override
    public PageUtils customerActivityPages(Map<String, Object> params) {
        return new PageUtils(this.baseMapper.customerActivityPages(new Query<ActivityModel>().getPage(params), params));
    }

    @Override
    public R activitySubscribe(String activityId, String userId) {
        ActivityEntity activity = this.baseMapper.selectOne(Wrappers.lambdaQuery(ActivityEntity.class)
                .eq(ActivityEntity::getStatus, 1).eq(ActivityEntity::getDelFlag, 0)
                .eq(ActivityEntity::getId, activityId).last("LIMIT 1"));
        if (Objects.isNull(activity)) {
            return R.error("活动不存在");
        }
        Date dateTime = activity.getDateTime();
        Date now = new Date();
        if (dateTime.compareTo(now) < 0) {
            return R.error("活动已结束");
        }
        ActivityUserEntity activityUser = activityUserDao.selectOne(Wrappers.lambdaQuery(ActivityUserEntity.class)
                .eq(ActivityUserEntity::getUserId, userId).eq(ActivityUserEntity::getStatus, 1));
        if (Objects.nonNull(activityUser)) {
            return R.error("已预约，请勿重复预约");
        }
        ActivityUserEntity entity = new ActivityUserEntity();
        entity.setUserId(userId);
        entity.setActivityId(activityId);
        entity.setStatus(1);
        entity.setCreateTime(now);
        int save = activityUserDao.insert(entity);
        return save > 0 ? R.ok() : R.error();
    }

    @Override
    public JSONObject mySubscribeActivity(String userId) {
        return this.baseMapper.mySubscribeActivity(userId);
    }
}
