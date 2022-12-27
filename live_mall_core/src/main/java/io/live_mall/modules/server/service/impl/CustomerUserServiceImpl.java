package io.live_mall.modules.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.modules.server.dao.CustomerUserDao;
import io.live_mall.modules.server.dto.CustomerUserDto;
import io.live_mall.modules.server.entity.CustomerUserEntity;
import io.live_mall.modules.server.model.CustomerUserModel;
import io.live_mall.modules.server.service.CustomerUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author yewl
 * @date 2022/12/19 13:27
 * @description
 */
@Service("customerUserService")
public class CustomerUserServiceImpl extends ServiceImpl<CustomerUserDao, CustomerUserEntity> implements CustomerUserService {

    @Override
    public CustomerUserModel login(String phone) {
        return this.baseMapper.login(phone);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUserInfo(CustomerUserDto userDto) {
        return this.baseMapper.updateUserInfo(userDto);
    }
}
