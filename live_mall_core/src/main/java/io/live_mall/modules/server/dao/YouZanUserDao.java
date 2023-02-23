package io.live_mall.modules.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.live_mall.modules.server.entity.YouZanUserEntity;
import io.live_mall.modules.server.model.YouZanUserModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yewl
 * @date 2023/2/19 11:12
 * @description
 */
@Mapper
public interface YouZanUserDao extends BaseMapper<YouZanUserEntity> {
    YouZanUserModel yzUserDetail(String userId);
}
