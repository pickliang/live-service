package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.FinanceEntity;
import io.live_mall.modules.server.model.FinanceModel;

import java.util.List;
import java.util.Map;

/**
 * @author yewl
 * @date 2023/1/3 14:48
 * @description
 */
public interface FinanceService extends IService<FinanceEntity> {
    PageUtils financePages(Map<String, Object> params);

    FinanceModel financeInfo(Long id);

    List<FinanceEntity> companyDynamics(Integer classify);

    PageUtils financeList(Map<String, Object> params);
}
