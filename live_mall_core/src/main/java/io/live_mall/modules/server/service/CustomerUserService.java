package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.modules.server.dto.CustomerUserDto;
import io.live_mall.modules.server.entity.CustomerUserEntity;
import io.live_mall.modules.server.model.CustomerUserModel;

/**
 * @author yewl
 * @date 2022/12/19 13:26
 * @description
 */
public interface CustomerUserService extends IService<CustomerUserEntity> {
    /**
     * 根据手机号登录
     * @param phone 电话
     * @return CustomerUserModel
     */
    CustomerUserModel login(String phone);

    /**
     * 更新用户证件信息
     * @param userDto
     * @return
     */
    int updateUserInfo(CustomerUserDto userDto);
}
