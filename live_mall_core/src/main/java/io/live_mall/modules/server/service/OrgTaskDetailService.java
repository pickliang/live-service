package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.OrgTaskDetailEntity;

import java.util.Map;

/**
 * 
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2021-04-22 21:38:06
 */
public interface OrgTaskDetailService extends IService<OrgTaskDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

