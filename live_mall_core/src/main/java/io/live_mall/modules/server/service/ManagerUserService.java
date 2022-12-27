package io.live_mall.modules.server.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.ManagerUserEntity;

import java.util.Map;

/**
 * 管理人管理
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-11-27 12:13:39
 */
public interface ManagerUserService extends IService<ManagerUserEntity> {


	PageUtils queryPage(JSONObject params);
}

