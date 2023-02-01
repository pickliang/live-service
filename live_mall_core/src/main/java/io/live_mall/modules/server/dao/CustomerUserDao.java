package io.live_mall.modules.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.live_mall.modules.server.dto.CustomerUserDto;
import io.live_mall.modules.server.entity.CustomerUserEntity;
import io.live_mall.modules.server.model.CustomerUserModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author yewl
 * @date 2022/12/19 11:38
 * @description
 */
@Mapper
public interface CustomerUserDao extends BaseMapper<CustomerUserEntity> {
    /**
     * 根据手机号登录
     * @param phone 电话
     * @return CustomerUserModel
     */
    CustomerUserModel login(@Param("phone") String phone);

    /**
     * 根据token获取用户信息
     * @param token token
     * @return CustomerUserModel
     */
    CustomerUserModel queryByToken(String token);

    /**
     * 更新用户证件信息
     * @param userDto
     * @param code 邀请码
     * @return
     */
    int updateUserInfo(@Param("user") CustomerUserDto userDto, @Param("code") Integer code);
}
