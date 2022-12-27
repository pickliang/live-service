/**
 * Copyright (c) 2016-2019 分销峰开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.live_mall.modules.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.live_mall.common.annotation.SysLog;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.R;
import io.live_mall.common.validator.Assert;
import io.live_mall.common.validator.ValidatorUtils;
import io.live_mall.common.validator.group.AddGroup;
import io.live_mall.common.validator.group.UpdateGroup;
import io.live_mall.modules.server.entity.MemberEntity;
import io.live_mall.modules.server.service.MemberService;
import io.live_mall.modules.server.service.OrderService;
import io.live_mall.modules.server.service.SysOrgUserService;
import io.live_mall.modules.sys.entity.SysUserEntity;
import io.live_mall.modules.sys.form.PasswordForm;
import io.live_mall.modules.sys.service.SysUserRoleService;
import io.live_mall.modules.sys.service.SysUserService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	
	@Autowired
	SysOrgUserService  sysOrgUserService;
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	OrderService orderService;
	
	@GetMapping("/getUserListByOrgId")
	@RequiresPermissions("sys:user:list")
	public R getUserListByOrgId(@RequestParam String orgId ){
		//只有超级管理员，才能查看所有管理员列表
		return R.ok().put("data",sysOrgUserService.getUserList(orgId));
	}
	
	
	
	

	/**
	 * 所有用户列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:user:list")
	public R list(@RequestParam Map<String, Object> params){
		//只有超级管理员，才能查看所有管理员列表
		PageUtils page = sysUserService.queryPage(params);
		return R.ok().put("page", page);
	}
	
	@GetMapping("/getUserList")
	@RequiresPermissions("sys:user:list")
	public R getUserList(@RequestParam Map<String, Object> params){
		//只有超级管理员，才能查看所有管理员列表
		return R.ok().put("data",sysUserService.list(new QueryWrapper<SysUserEntity>().eq("status","1")));
	}
	
	/**
	 * 获取登录的用户信息
	 */
	@GetMapping("/info")
	public R info(){
		SysUserEntity user = getUser();
		SysUserEntity userEntity = sysUserService.getById(user.getUserId());
		//获取业绩
		userEntity.setAchievement(orderService.sumMoenySaleId(userEntity.getUserId()));
		userEntity.setRoleIdList(sysUserRoleService.queryRoleIdList(user.getUserId()));
		//客户数
		userEntity.setCustNum(memberService.count(new QueryWrapper<MemberEntity>().eq("sale_id", user.getUserId())));
		userEntity.setPassword(null);
		return R.ok().put("user", userEntity);
	}
	
	/**
	 * 修改登录用户密码
	 */
	@SysLog("修改密码")
	@PostMapping("/password")
	public R password(@RequestBody PasswordForm form){
		Assert.isBlank(form.getNewPassword(), "新密码不为能空");
		
		//sha256加密
		String password = new Sha256Hash(form.getPassword(), getUser().getSalt()).toHex();
		//sha256加密
		String newPassword = new Sha256Hash(form.getNewPassword(), getUser().getSalt()).toHex();

		System.out.println("userid====");
		//更新密码
		boolean flag = sysUserService.updatePassword(getUserId(), password, newPassword);
		if(!flag){
			return R.error("原密码不正确");
		}
		return R.ok();
	}
	
	
	
	
	
	/**
	 * 用户信息
	 */
	@GetMapping("/info/{userId}")
	@RequiresPermissions("sys:user:info")
	public R info(@PathVariable("userId") Long userId){
		SysUserEntity user = sysUserService.getById(userId);
		user.setPassword(null);
		//获取用户所属的角色列表
		List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
		user.setRoleIdList(roleIdList);
		return R.ok().put("user", user);
	}
	
	/**
	 * 保存用户
	 */
	@SysLog("保存用户")
	@PostMapping("/save")
	@RequiresPermissions("sys:user:save")
	public R save(@RequestBody SysUserEntity user){
		ValidatorUtils.validateEntity(user, AddGroup.class);
		
		user.setCreateUserId(getUserId());
		sysUserService.saveUser(user);
		
		return R.ok();
	}
	
	
	/**
	 * 修改用户
	 */
	@SysLog("修改用户")
	@PostMapping("/update")
	@RequiresPermissions("sys:user:update")
	public R update(@RequestBody SysUserEntity user){
		ValidatorUtils.validateEntity(user, UpdateGroup.class);
		user.setCreateUserId(getUserId());
		sysUserService.update(user);
		return R.ok();
	}
	
	/**
	 * 修改用户
	 */
	@SysLog("修改用户")
	@PostMapping("/updateApp")
	@RequiresPermissions("sys:user:update")
	public R updateApp(@RequestBody SysUserEntity user){
		ValidatorUtils.validateEntity(user, UpdateGroup.class);
		user.setPassword(null);
		sysUserService.updateById(user);
		return R.ok();
	}
	
	@SysLog("修改状态")
	@PostMapping("/uptStaus")
	@RequiresPermissions("sys:user:update")
	public R uptStaus(@RequestBody SysUserEntity user){
		SysUserEntity sysUserEntity = sysUserService.getById(user.getUserId());
		sysUserEntity.setStatus(user.getStatus());
		sysUserService.updateById(sysUserEntity);
		return R.ok();
	}
	
	
	
	
	/**
	 * 删除用户
	 */
	@SysLog("删除用户")
	@PostMapping("/delete")
	@RequiresPermissions("sys:user:delete")
	public R delete(@RequestBody Long[] userIds){
		if(ArrayUtils.contains(userIds, 1L)){
			return R.error("系统管理员不能删除");
		}
		
		if(ArrayUtils.contains(userIds, getUserId())){
			return R.error("当前用户不能删除");
		}
		
		sysUserService.deleteBatch(userIds);
		
		return R.ok();
	}
	
	/**
	 * 通过角色获取用户列表
	 */
	@GetMapping("/getUserlistByRoleId/{roleId}")
	public R getUserlistByRoleId(@PathVariable("roleId")String roleId){
		return R.ok().put("data", sysUserService.getUserlistByRoleId(roleId));
	}
	
	
	
	
	
}
