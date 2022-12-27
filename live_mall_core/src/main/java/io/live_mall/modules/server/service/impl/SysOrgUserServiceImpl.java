package io.live_mall.modules.server.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.live_mall.modules.server.dao.SysOrgUserDao;
import io.live_mall.modules.server.entity.SysOrgGroupEntity;
import io.live_mall.modules.server.entity.SysOrgUserEntity;
import io.live_mall.modules.server.service.SysOrgGroupService;
import io.live_mall.modules.server.service.SysOrgUserService;
import io.live_mall.modules.sys.entity.SysUserEntity;
import io.live_mall.modules.sys.service.SysUserService;


@Service("sysOrgUserService")
public class SysOrgUserServiceImpl extends ServiceImpl<SysOrgUserDao, SysOrgUserEntity> implements SysOrgUserService {
	
	@Autowired
	SysUserService sysUserService;

	@Autowired
	SysOrgGroupService sysOrgGroupService;
	
	@Override
	public List<SysUserEntity> getUserList(String orgid) {
		return sysUserService.getUserListByOrgId(orgid);
	}
	
	@Override
	public SysOrgUserEntity getById(Serializable id) {
		// TODO Auto-generated method stub
		SysOrgUserEntity orgUser = super.getById(id);
		if(orgUser == null ) {
			return null;
		}
		orgUser.setOrgView(sysOrgGroupService.getOrgOneView(orgUser.getOrgId()));
		orgUser.setSysOrgGroup(sysOrgGroupService.getById(orgUser.getOrgId()));
		return orgUser;
	}
	
	@Override
	public boolean saveOrUpdate(SysOrgUserEntity entity) {
		if(entity.getOrgId() !=null && entity.getUserId() !=null) {
			return super.saveOrUpdate(entity);
		}
		return false;
	}
}