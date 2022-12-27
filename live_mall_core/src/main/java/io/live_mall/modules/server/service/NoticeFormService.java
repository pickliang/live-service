package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.NoticeFormEntity;

import java.util.Map;

/**
 * 通知
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-12-02 02:07:09
 */
public interface NoticeFormService extends IService<NoticeFormEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

