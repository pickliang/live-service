package io.live_mall.modules.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.live_mall.modules.server.entity.MmsLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author yewl
 * @date 2023/2/20 21:01
 * @description
 */
@Mapper
public interface MmsLogDao extends BaseMapper<MmsLogEntity> {
    IPage<MmsLogEntity> pages(@Param("pages") IPage<MmsLogEntity> pages, @Param("type") Integer type);
}
