package io.live_mall.modules.server.dao;

import io.live_mall.modules.server.entity.ProductCashEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author daitao
 * @email 867278141@qq.com
 * @date 2021-02-01 00:48:56
 */
@Mapper
public interface ProductCashDao extends BaseMapper<ProductCashEntity> {
	List<ProductCashEntity> getProductCashList(String productId);
}
