/**
 * Copyright (c) 2016-2019 分销峰开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.live_mall.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.live_mall.modules.server.model.SysUserModel;
import io.live_mall.modules.sys.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUserEntity> {

	IPage<SysUserEntity> selectPageVo(IPage<SysUserEntity> page, @Param("params") Map<String, Object> params);
	
	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */
	List<String> queryAllPerms(Long userId);
	
	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(Long userId);
	
	/**
	 * 根据用户名，查询系统用户
	 */
	SysUserEntity queryByUserName(String username);

	List<SysUserEntity> getUserlistByRoleId(String roleId);

	List<SysUserEntity> getUserListByOrgId(String orgId);

	List<SysUserEntity> selectUserOgr(Integer level);

	List<SysUserModel> orgRegionUser(String orgGroupIds);

	SysUserModel sysUserByCardNum(String cardNum);
}
