package io.live_mall.modules.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.live_mall.modules.server.entity.HistoryDuifuPayEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HistoryDuifuPayDao extends BaseMapper<HistoryDuifuPayEntity> {
    /**
     * 下一个对付日期
     * @param historyDuifuId id
     * @return
     */
    HistoryDuifuPayEntity nextHistoryDuifuPay(String historyDuifuId);
}
