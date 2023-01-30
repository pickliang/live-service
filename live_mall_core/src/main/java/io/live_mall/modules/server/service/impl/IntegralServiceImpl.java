package io.live_mall.modules.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.IntegralDao;
import io.live_mall.modules.server.entity.IntegralEntity;
import io.live_mall.modules.server.service.IntegralService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author yewl
 * @date 2023/1/30 16:39
 * @description
 */
@Service("integralService")
public class IntegralServiceImpl extends ServiceImpl<IntegralDao, IntegralEntity> implements IntegralService {
    @Override
    public PageUtils integralPages(Map<String, Object> params) {
        return new PageUtils(this.baseMapper.integralPages(new Query<IntegralEntity>().getPage(params)));
    }
}
