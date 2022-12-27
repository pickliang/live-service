package io.live_mall.modules.server.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import io.live_mall.modules.server.entity.ProductEntity;

/**
 * 产品列表
 * 
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-11-27 12:13:38
 */
@Mapper
public interface ProductDao extends BaseMapper<ProductEntity> {


	IPage<ProductEntity> selectPageVo(IPage<ProductEntity> page,@Param("params") JSONObject params);
	
	IPage<ProductEntity> selectPageApp(IPage<ProductEntity> page,@Param("params") JSONObject params);
	
	Integer  getKhs(String porductId);
	
	Integer  getXsjgs(String porductId);
	
	JSONObject getTj(@Param("params")  JSONObject params);
}
