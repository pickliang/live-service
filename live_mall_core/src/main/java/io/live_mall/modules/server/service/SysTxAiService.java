package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.SysTxAiEntity;

import java.util.Map;

/**
 * 
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-12-09 20:26:56
 */
public interface SysTxAiService extends IService<SysTxAiEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

