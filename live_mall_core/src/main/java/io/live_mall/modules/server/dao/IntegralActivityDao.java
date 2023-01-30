package io.live_mall.modules.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.live_mall.modules.server.entity.IntegralActivityEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yewl
 * @date 2023/1/30 16:40
 * @description
 */
@Mapper
public interface IntegralActivityDao extends BaseMapper<IntegralActivityEntity> {
    IPage<IntegralActivityEntity> integralActivityPages(IPage<IntegralActivityEntity> pages);
}
