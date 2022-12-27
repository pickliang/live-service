package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.InfoFormEntity;

import java.util.Map;

/**
 * 这是资料库父子文件表
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-12-02 02:07:10
 */
public interface InfoFormService extends IService<InfoFormEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

