/**
 * Copyright (c) 2016-2019 分销峰开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.live_mall.modules.oss.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.live_mall.modules.oss.entity.SysOssEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件上传
 *
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface SysOssDao extends BaseMapper<SysOssEntity> {
	
}
