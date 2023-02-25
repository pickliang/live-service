package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.MmsLogEntity;

import java.util.Map;

/**
 * @author yewl
 * @date 2023/2/20 21:02
 * @description
 */
public interface MmsLogService extends IService<MmsLogEntity> {
    PageUtils pages(Map<String, Object> params);
    /**
     * 选中的要下发短信的兑付通知数据
     * @param startDate 开始日期yyyy-MM-dd
     * @param endDate 结束日期yyyy-MM-dd
     * @param ids 订单号
     * @param mmsToken 短信token
     * @param userId 用户id
     */
    void sendDuiFuMms(String startDate, String endDate, String ids, String mmsToken, Long userId);

    /**
     * 选中要下发短信的付息通知数据
     * @param startDate 开始日期yyyy-MM-dd
     * @param endDate 结束日期yyyy-MM-dd
     * @param ids 订单号
     * @param mmsToken 短信token
     * @param userId 用户id
     */
    void sendPayMendMms(String startDate, String endDate, String ids, String mmsToken, Long userId);
}
