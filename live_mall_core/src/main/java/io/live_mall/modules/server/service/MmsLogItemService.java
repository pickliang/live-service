package io.live_mall.modules.server.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.MmsLogItemEntity;

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
     * 选中的要下发短信的兑付通知数据
     * @param startDate 开始日期yyyy-MM-dd
     * @param endDate 结束日期yyyy-MM-dd
     * @param ids 订单号
     * @param token 短信token
     * @param userId 用户id
     */
    boolean sendDuiFuCompleted(String token, String startDate, String endDate, String ids, Long userId);

    /**
     * 对付预警通知
     * @param token
     * @param list
     * @return
     */
    boolean sendDuiFuEarlyWarning(String token, List<JSONObject> list);
}
