package io.live_mall.modules.server.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import io.live_mall.modules.server.entity.MemberTemplateMsgEntity;

/**
 * 会员消息订阅表
 * 
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-09-23 22:54:07
 */
@Mapper
public interface MemberTemplateMsgDao extends BaseMapper<MemberTemplateMsgEntity> {

}
