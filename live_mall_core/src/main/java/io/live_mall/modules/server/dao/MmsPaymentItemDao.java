package io.live_mall.modules.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.live_mall.modules.server.entity.MmsPaymentItemEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author yewl
 * @date 2023/2/23 13:24
 * @description
 */
@Mapper
public interface MmsPaymentItemDao extends BaseMapper<MmsPaymentItemEntity> {
    IPage<MmsPaymentItemEntity> pages(@Param("pages") IPage<MmsPaymentItemEntity> pages, @Param("mmsLogId") String mmsLogId);
}
