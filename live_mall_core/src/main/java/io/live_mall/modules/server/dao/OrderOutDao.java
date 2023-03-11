package io.live_mall.modules.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.live_mall.modules.server.entity.OrderOutEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yewl
 * @date 2023/3/10 22:33
 * @description
 */
@Mapper
public interface OrderOutDao extends BaseMapper<OrderOutEntity> {
}
