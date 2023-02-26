package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.MmsPaymentItemEntity;
import io.live_mall.modules.server.model.DuiFuNoticeModel;

import java.util.List;
import java.util.Map;

/**
 * @author yewl
 * @date 2023/2/23 13:25
 * @description
 */
public interface MmsPaymentItemService extends IService<MmsPaymentItemEntity> {
    PageUtils pages(Map<String, Object> params);

    /**
     * 选中要下发短信的付息通知数据
     * @param startDate 开始日期yyyy-MM-dd
     * @param endDate 结束日期yyyy-MM-dd
     * @param ids 订单号
     * @param token 短信token
     * @param userId 用户id
     * @return boolean
     */
    boolean sendPaymentCompleted(String token, String startDate, String endDate, String ids, Long userId);

    public boolean sendPaymentEarlyWarning(String token, List<DuiFuNoticeModel> list);
}
