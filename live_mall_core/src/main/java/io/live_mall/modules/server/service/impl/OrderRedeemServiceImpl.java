package io.live_mall.modules.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.modules.server.dao.OrderRedeemDao;
import io.live_mall.modules.server.entity.OrderRedeemEntity;
import io.live_mall.modules.server.model.BondOrderModel;
import io.live_mall.modules.server.service.OrderRedeemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yewl
 * @date 2023/3/11 20:38
 * @description
 */
@Service("orderRedeemService")
@AllArgsConstructor
@Slf4j
public class OrderRedeemServiceImpl extends ServiceImpl<OrderRedeemDao, OrderRedeemEntity> implements OrderRedeemService {
    @Override
    public List<BondOrderModel> customerBonds(String orderId) {
        return this.baseMapper.customerBonds(orderId);
    }

}
