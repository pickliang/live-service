package io.live_mall.modules.server.dao;

import io.live_mall.modules.server.entity.ProductClassEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 产品分类
 * 
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-11-27 12:13:38
 */
@Mapper
public interface ProductClassDao extends BaseMapper<ProductClassEntity> {
	
}
