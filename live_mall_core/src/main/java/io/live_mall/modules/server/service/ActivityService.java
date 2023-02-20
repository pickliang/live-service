package io.live_mall.modules.server.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.R;
import io.live_mall.modules.server.entity.ActivityEntity;

import java.util.Map;

/**
 * @author yewl
 * @date 2023/1/4 14:02
 * @description
 */
public interface ActivityService extends IService<ActivityEntity> {
    PageUtils activityPages(Map<String, Object> params);

    PageUtils customerActivityPages(Map<String, Object> params);

    R activitySubscribe(Long activityId, String userId);

    JSONObject mySubscribeActivity(String userId);
}
