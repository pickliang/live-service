package io.live_mall.modules.server.dao;

import io.live_mall.modules.server.entity.InfoFormEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 这是资料库父子文件表
 * 
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-12-02 02:07:10
 */
@Mapper
public interface InfoFormDao extends BaseMapper<InfoFormEntity> {
	
}
