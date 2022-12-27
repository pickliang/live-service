package io.live_mall.modules.server.dao;

import io.live_mall.modules.server.entity.OrderPayEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 结算信息
 * 
 * @author daitao
 * @email 867278141@qq.com
 * @date 2021-02-18 23:16:37
 */
@Mapper
public interface OrderPayDao extends BaseMapper<OrderPayEntity> {
	
}
