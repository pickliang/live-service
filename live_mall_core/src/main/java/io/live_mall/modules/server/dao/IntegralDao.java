package io.live_mall.modules.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.live_mall.modules.server.entity.IntegralEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yewl
 * @date 2023/1/30 16:37
 * @description
 */
@Mapper
public interface IntegralDao extends BaseMapper<IntegralEntity> {
    IPage<IntegralEntity> integralPages(IPage<IntegralEntity> pages);
}
