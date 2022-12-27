package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.SysDicDetailEntity;

import java.util.Map;

/**
 * 
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-11-27 12:21:45
 */
public interface SysDicDetailService extends IService<SysDicDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

