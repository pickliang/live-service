package io.live_mall.modules.server.service.impl;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.OrderBonusDao;
import io.live_mall.modules.server.entity.OrderBonusEntity;
import io.live_mall.modules.server.model.OrderBonusModel;
import io.live_mall.modules.server.service.OrderBonusService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author yewl
 * @date 2023/3/10 22:22
 * @description
 */
@Service("orderBonusService")
@Slf4j
@AllArgsConstructor
public class OrderBonusServiceImpl extends ServiceImpl<OrderBonusDao, OrderBonusEntity> implements OrderBonusService {
    @Override
    public PageUtils pages(Map<String, Object> params) {
        Integer type = Convert.convert(Integer.class, params.get("type"), 1);
        return new PageUtils(this.baseMapper.pages(new Query<OrderBonusModel>().getPage(params), type));
    }

    @Override
    public PageUtils customerPages(Map<String, Object> params, String cardNum) {
        // 1-在投订单  2-历史订单
        Integer type = Convert.convert(Integer.class, params.get("type"), 1);
        return new PageUtils(this.baseMapper.customerPages(new Query<JSONObject>().getPage(params), type, cardNum));
    }
}
