package io.live_mall.modules.server.dao;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.live_mall.modules.server.entity.LivePlaybackEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author yewl
 * @date 2023/2/25 9:52
 * @description
 */
@Mapper
public interface LivePlaybackDao extends BaseMapper<LivePlaybackEntity> {
    IPage<JSONObject> pages(IPage<JSONObject> pages);

    Integer updateIsShow(@Param("id") String id, @Param("isShow") Integer isShow);
}
