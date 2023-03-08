package io.live_mall.modules.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.modules.server.dao.CustomerUserDao;
import io.live_mall.modules.server.dao.OrderDao;
import io.live_mall.modules.server.dto.CustomerUserDto;
import io.live_mall.modules.server.entity.CustomerUserEntity;
import io.live_mall.modules.server.entity.OrderEntity;
import io.live_mall.modules.server.model.CustomerUserModel;
import io.live_mall.modules.server.service.CustomerUserIntegralItemService;
import io.live_mall.modules.server.service.CustomerUserService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

/**
 * @author yewl
 * @date 2022/12/19 13:27
 * @description
 */
@Service("customerUserService")
@AllArgsConstructor
public class CustomerUserServiceImpl extends ServiceImpl<CustomerUserDao, CustomerUserEntity> implements CustomerUserService {

    private final OrderDao orderDao;
    private final CustomerUserIntegralItemService customerUserIntegralItemService;

    @Override
    public CustomerUserModel login(String phone) {
        return this.baseMapper.login(phone);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateUserInfo(CustomerUserDto userDto) {
        // 是否存在有效的理财订单
        Integer count = orderDao.selectCount(Wrappers.lambdaQuery(OrderEntity.class).eq(OrderEntity::getCardNum, userDto.getCardNum()).eq(OrderEntity::getStatus, 4).last("LIMIT 1"));
        Integer code = count == 0 ? null : code();
        // 增加积分
        CompletableFuture.supplyAsync(() -> customerUserIntegralItemService.saveCustomerUserIntegralItem(userDto.getId(), userDto.getCardNum()));
        this.baseMapper.updateUserInfo(userDto, code);
        return code;
    }

    /**
     * 分配授权码
     * @return Integer
     */
    private Integer code() {
        Integer code = RandomUtils.nextInt(100000, 999999);
        Integer count = this.baseMapper.selectCount(Wrappers.lambdaQuery(CustomerUserEntity.class).eq(CustomerUserEntity::getCode, code).last("LIMIT 1"));
        while (count != 0) {
            code = RandomUtils.nextInt(100000, 999999);;
            count = this.baseMapper.selectCount(Wrappers.lambdaQuery(CustomerUserEntity.class).eq(CustomerUserEntity::getCode, code).last("LIMIT 1"));
        }
        return code;
    }
}
