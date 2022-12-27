package io.live_mall.modules.server.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import io.live_mall.modules.server.entity.SysOrgUserEntity;
import io.live_mall.modules.sys.entity.SysUserEntity;

/**
 * 
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-11-27 12:21:45
 */
public interface SysOrgUserService extends IService<SysOrgUserEntity> {

	List<SysUserEntity> getUserList(String id);
}

