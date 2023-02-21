package io.live_mall.modules.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.live_mall.modules.server.entity.MmsLogItemEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * @author yewl
 * @date 2023/2/20 21:05
 * @description
 */
@Mapper
public interface MmsLogItemDao extends BaseMapper<MmsLogItemEntity> {
    IPage<MmsLogItemEntity> pages(IPage<MmsLogItemEntity> pages, Map<String, Object> params);
}
