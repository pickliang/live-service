/**
 * Copyright (c) 2016-2019 分销峰开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.live_mall.modules.sys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.exception.RRException;
import io.live_mall.common.utils.Constant;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.SysUserArchivesDao;
import io.live_mall.modules.server.entity.SysOrgUserEntity;
import io.live_mall.modules.server.entity.SysUserArchivesEntity;
import io.live_mall.modules.server.model.SysUserModel;
import io.live_mall.modules.server.service.SysOrgGroupService;
import io.live_mall.modules.server.service.SysOrgUserService;
import io.live_mall.modules.sys.dao.SysUserDao;
import io.live_mall.modules.sys.entity.SysUserEntity;
import io.live_mall.modules.sys.service.SysRoleService;
import io.live_mall.modules.sys.service.SysUserRoleService;
import io.live_mall.modules.sys.service.SysUserService;
import io.live_mall.modules.sys.service.WxService;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;


/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysOrgUserService sysOrgUserService;
	
	@Autowired
	private SysOrgGroupService sysOrgGroupService;
	
	@Autowired
	private WxService wxService;
	@Autowired
	private SysUserArchivesDao sysUserArchivesDao;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		if(params.get("orgId") != null ) {
			String childrenOrgId = sysOrgGroupService.getChildrenOrgIdAndCurrentId(params.get("orgId")+"");
			params.put("orgId", childrenOrgId);
		}
		IPage<SysUserEntity> page = this.baseMapper.selectPageVo(
			new Query<SysUserEntity>().getPage(params),params
		);
		return new PageUtils(page);
	}
	
	@Override
	public SysUserEntity getById(Serializable id) {
		// TODO Auto-generated method stub
		  if(id == null) {
			  return null;
		  }
		 SysUserEntity user = super.getById(id);
		 if(user== null) {
			 return null;
		 }
		 SysOrgUserEntity sysOrgUserEntity = sysOrgUserService.getById(id);
		 if(sysOrgUserEntity != null ) {
			 user.setOrg(sysOrgUserEntity);
		 }
		return user;
	}
	
	
	@Override
	public SysUserEntity getByReaName(String name) {
		// TODO Auto-generated method stub
			SysUserEntity user = this.getOne(new QueryWrapper<SysUserEntity>().eq("realname", name));
		 if(user== null) {
			 return null;
		 }
		 SysOrgUserEntity sysOrgUserEntity = sysOrgUserService.getById(user.getUserId());
		 if(sysOrgUserEntity != null ) {
			 user.setOrg(sysOrgUserEntity);
		 }
		return user;
	}
	

	@Override
	public List<String> queryAllPerms(Long userId) {
		return baseMapper.queryAllPerms(userId);
	}

	@Override
	public List<Long> queryAllMenuId(Long userId) {
		return baseMapper.queryAllMenuId(userId);
	}

	@Override
	public SysUserEntity queryByUserName(String username) {
		return baseMapper.queryByUserName(username);
	}

	@Override
	@Transactional
	public void saveUser(SysUserEntity user) {
		user.setCreateTime(new Date());
		//sha256加密
		String salt = RandomStringUtils.randomAlphanumeric(20);
		user.setPassword(new Sha256Hash(user.getPassword(), salt).toHex());
		user.setSalt(salt);
		this.save(user);
		//检查角色是否越权
		//checkRole(user);
		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
		
		//保存组织关系
		user.getOrg().setUserId(user.getUserId());
		sysOrgUserService.saveOrUpdate(user.getOrg());
	}

	@Override
	@Transactional
	public void update(SysUserEntity user) {
		Boolean changepwdFlag = false;
		if(StringUtils.isBlank(user.getPassword())){
			user.setPassword(null);
		}else{
			if (user.getPassword().equals("123456")) {
				changepwdFlag = true;
			}
			user.setPassword(new Sha256Hash(user.getPassword(), user.getSalt()).toHex());
		}
		this.updateById(user);
		
		//检查角色是否越权
		//checkRole(user);
		if(changepwdFlag == false) {
			//保存用户与角色关系
			sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());

			//保存组织关系
			user.getOrg().setUserId(user.getUserId());
			sysOrgUserService.saveOrUpdate(user.getOrg());
		}
	}

	@Override
	public void deleteBatch(Long[] userId) {
		this.removeByIds(Arrays.asList(userId));
	}

	@Override
	public boolean updatePassword(Long userId, String password, String newPassword) {
		SysUserEntity userEntity = new SysUserEntity();
		userEntity.setPassword(newPassword);
		return this.update(userEntity,
				new QueryWrapper<SysUserEntity>().eq("user_id", userId));
	}
	
	/**
	 * 检查角色是否越权
	 */
	private void checkRole(SysUserEntity user){
		if(user.getRoleIdList() == null || user.getRoleIdList().size() == 0){
			return;
		}
		//如果不是超级管理员，则需要判断用户的角色是否自己创建
		if(user.getCreateUserId() == Constant.SUPER_ADMIN){
			return ;
		}
		
		//查询用户创建的角色列表
		List<Long> roleIdList = sysRoleService.queryRoleIdList(user.getCreateUserId());

		//判断是否越权
		if(!roleIdList.containsAll(user.getRoleIdList())){
			throw new RRException("新增用户所选角色，不是本人创建");
		}
	}

	@Override
	public List<SysUserEntity> getUserlistByRoleId(String roleId) {
		// TODO Auto-generated method stub
		return baseMapper.getUserlistByRoleId(roleId);
	}

	@Override
	public List<SysUserEntity> getUserListByOrgId(String orgid) {
		return baseMapper.getUserListByOrgId(orgid);
	}

	@Override
	public String wxPhone(JSONObject userJson,Long userId) {
		// TODO Auto-generated method stub
		SysUserEntity user = this.getById(userId);
		wxService.getPhone(userJson,user);
		this.updateById(user);
		return null;
	}

	@Override
	public List<SysUserModel> orgRegionUser(String orgGroupIds) {
		List<SysUserModel> sysUserModels = this.baseMapper.orgRegionUser(orgGroupIds);
		sysUserModels.forEach(model -> {
			SysUserArchivesEntity userArchives = sysUserArchivesDao.selectById(model.getUserId());
			if (Objects.nonNull(userArchives)) {
				model.setPersonPhotos(userArchives.getCertificatePhoto());
			}
		});
		return sysUserModels;
	}

	@Override
	public SysUserModel sysUserByCardNum(String cardNum) {
		return this.baseMapper.sysUserByCardNum(cardNum);
	}
}