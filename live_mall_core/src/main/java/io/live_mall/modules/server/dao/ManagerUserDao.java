package io.live_mall.modules.server.dao;

import io.live_mall.modules.server.entity.ManagerUserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 管理人管理
 * 
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-11-27 12:13:39
 */
@Mapper
public interface ManagerUserDao extends BaseMapper<ManagerUserEntity> {
	
	/**
	 * 包含统计
	 * @param id
	 * @return
	 */
	ManagerUserEntity  getOneAndTj(String id);
	
}
