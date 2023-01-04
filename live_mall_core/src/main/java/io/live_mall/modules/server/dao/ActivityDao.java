package io.live_mall.modules.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.live_mall.modules.server.entity.ActivityEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yewl
 * @date 2023/1/4 14:01
 * @description
 */
@Mapper
public interface ActivityDao extends BaseMapper<ActivityEntity> {
}
