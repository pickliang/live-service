/**
 * Copyright (c) 2016-2019 分销峰开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.live_mall.modules.sys.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.live_mall.common.utils.Constant;
import io.live_mall.modules.sys.dao.SysMenuDao;
import io.live_mall.modules.sys.dao.SysUserDao;
import io.live_mall.modules.sys.dao.SysUserRoleDao;
import io.live_mall.modules.sys.dao.SysUserTokenDao;
import io.live_mall.modules.sys.entity.SysMenuEntity;
import io.live_mall.modules.sys.entity.SysUserEntity;
import io.live_mall.modules.sys.entity.SysUserTokenEntity;
import io.live_mall.modules.sys.service.ShiroService;
import io.live_mall.modules.sys.service.SysUserRoleService;
import io.live_mall.modules.sys.service.SysUserService;

import java.util.*;

@Service
public class ShiroServiceImpl implements ShiroService {
    @Autowired
    private SysMenuDao sysMenuDao;
    @Autowired
    private SysUserDao sysUserDao;
    
    @Autowired
    private SysUserService sysUserService;
    
    @Autowired
    private SysUserRoleService sysUserRoleService;
    
    
    @Autowired
    private SysUserTokenDao sysUserTokenDao;

    @Override
    public Set<String> getUserPermissions(long userId) {
        List<String> permsList;

        //系统管理员，拥有最高权限
        if(userId == Constant.SUPER_ADMIN){
            List<SysMenuEntity> menuList = sysMenuDao.selectList(null);
            permsList = new ArrayList<>(menuList.size());
            for(SysMenuEntity menu : menuList){
                permsList.add(menu.getPerms());
            }
        }else{
            permsList = sysUserDao.queryAllPerms(userId);
        }
        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for(String perms : permsList){
            if(StringUtils.isBlank(perms)){
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
        return permsSet;
    }

    @Override
    public SysUserTokenEntity queryByToken(String token) {
        return sysUserTokenDao.queryByToken(token);
    }

    @Override
    public SysUserEntity queryUser(Long userId) {
    	SysUserEntity selectById = sysUserService.getById(userId);
    	List<Long> queryRoleIdList = sysUserRoleService.queryRoleIdList(userId);
    	selectById.setRoleIdList(queryRoleIdList);
        return selectById;
    }
}
