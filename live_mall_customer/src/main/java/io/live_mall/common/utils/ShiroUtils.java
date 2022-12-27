/**
 * Copyright (c) 2016-2019 分销峰开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.live_mall.common.utils;

import io.live_mall.modules.server.model.CustomerUserModel;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * Shiro工具类
 *
 * @author Mark sunlightcs@gmail.com
 */
public class ShiroUtils {

	public static Session getSession() {
		return SecurityUtils.getSubject().getSession();
	}

	public static Subject getSubject() {
		return SecurityUtils.getSubject();
	}

	public static CustomerUserModel getUserEntity() {
		return (CustomerUserModel)SecurityUtils.getSubject().getPrincipal();
	}
	

	public static String getUserId() {
		return getUserEntity().getId();
	}
	
	public static void setSessionAttribute(Object key, Object value) {
		getSession().setAttribute(key, value);
	}

	public static Object getSessionAttribute(Object key) {
		return getSession().getAttribute(key);
	}

	public static boolean isLogin() {
		return SecurityUtils.getSubject().getPrincipal() != null;
	}

	// public static String getKaptcha(String key) {
	// 	Object kaptcha = getSessionAttribute(key);
	// 	if(kaptcha == null){
	// 		throw new RRException("验证码已失效");
	// 	}
	// 	getSession().removeAttribute(key);
	// 	return kaptcha.toString();
	// }

}
