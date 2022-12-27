package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.OrgTaskEntity;

import java.util.Map;

/**
 * 
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2021-04-18 17:23:53
 */
public interface OrgTaskService extends IService<OrgTaskEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

