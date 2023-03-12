package io.live_mall.modules.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.live_mall.modules.server.entity.OrderOutEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author yewl
 * @date 2023/3/10 22:33
 * @description
 */
@Mapper
public interface OrderOutDao extends BaseMapper<OrderOutEntity> {
    /**
     * 分红收益
     * @param cardNum 身份证
     * @return
     */
    double sumMoney(@Param("cardNum") String cardNum);
}
