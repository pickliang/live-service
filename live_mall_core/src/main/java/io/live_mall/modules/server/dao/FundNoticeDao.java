package io.live_mall.modules.server.dao;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.live_mall.modules.server.entity.FundNoticeEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yewl
 * @date 2023/3/12 14:05
 * @description
 */
@Mapper
public interface FundNoticeDao extends BaseMapper<FundNoticeEntity> {
    IPage<JSONObject> pages(IPage<JSONObject> pages);
}
