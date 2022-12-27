package io.live_mall.modules.server.dao;

import io.live_mall.modules.server.entity.MemberEntity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * 
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-12-09 22:21:50
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	JSONObject getTj(@Param("custId")String custId,@Param("salesId")String salesId);
}
