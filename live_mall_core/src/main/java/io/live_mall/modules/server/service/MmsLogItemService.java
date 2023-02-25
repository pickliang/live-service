package io.live_mall.modules.server.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.MmsLogItemEntity;
import io.live_mall.modules.server.model.DuiFuNoticeModel;

import java.util.List;
import java.util.Map;

/**
 * @author yewl
 * @date 2023/2/20 21:06
 * @description
 */
public interface MmsLogItemService extends IService<MmsLogItemEntity> {
    PageUtils pages(Map<String, Object> params);
    /**
     * 对付完成通知
     * @param token
     * @param list
     * @param logId
     * @param userId
     * @return
     */
    boolean sendDuifuMms(String token, List<DuiFuNoticeModel> list, String logId, Long userId);

    /**
     * 对付预警通知
     * @param token
     * @param list
     * @return
     */
    boolean sendDuiFuEarlyWarning(String token, List<JSONObject> list);
}
