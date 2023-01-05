package io.live_mall.modules.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.live_mall.modules.server.entity.FinanceEntity;
import io.live_mall.modules.server.model.FinanceModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author yewl
 * @date 2023/1/3 14:47
 * @description
 */
@Mapper
public interface FinanceDao extends BaseMapper<FinanceEntity> {
    IPage<FinanceModel> financePages(@Param("pages") IPage<FinanceModel> pages, @Param("params") Map<String, Object> params);
    IPage<FinanceEntity> financeList(@Param("pages") IPage<FinanceEntity> pages, @Param("params") Map<String, Object> params);
}
