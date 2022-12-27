package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.InfoTypeEntity;

import java.util.Map;

/**
 * 这是文件 文件组父子类型表
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-12-02 02:07:10
 */
public interface InfoTypeService extends IService<InfoTypeEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

