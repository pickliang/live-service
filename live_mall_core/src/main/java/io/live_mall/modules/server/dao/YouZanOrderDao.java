package io.live_mall.modules.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.live_mall.modules.server.entity.YouZanOrderEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;

/**
 * @author yewl
 * @date 2023/2/19 13:23
 * @description
 */
@Mapper
public interface YouZanOrderDao extends BaseMapper<YouZanOrderEntity> {
    /**
     * 累计消费金额
     * @param yzOpenId
     * @return
     */
    double consumerAmount(String yzOpenId);

    /**
     * 累计消费订单数
     * @param yzOpenId
     * @return
     */
    long consumerOrderNum(String yzOpenId);

    /**
     * 最近下单时间
     * @param yzOpenId
     * @return
     */
    Date recentlyOderTime(String yzOpenId);
}
