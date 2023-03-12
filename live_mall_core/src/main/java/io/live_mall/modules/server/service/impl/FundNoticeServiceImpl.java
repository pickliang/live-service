package io.live_mall.modules.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.FundNoticeDao;
import io.live_mall.modules.server.entity.FundNoticeEntity;
import io.live_mall.modules.server.service.FundNoticeService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author yewl
 * @date 2023/3/12 14:06
 * @description
 */
@Service("fundNoticeService")
public class FundNoticeServiceImpl extends ServiceImpl<FundNoticeDao, FundNoticeEntity> implements FundNoticeService {
    @Override
    public PageUtils pages(Map<String, Object> params) {
        return new PageUtils(this.baseMapper.pages(new Query<JSONObject>().getPage(params)));
    }
}
