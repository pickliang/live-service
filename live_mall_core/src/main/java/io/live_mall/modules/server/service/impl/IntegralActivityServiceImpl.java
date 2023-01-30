package io.live_mall.modules.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.IntegralActivityDao;
import io.live_mall.modules.server.entity.IntegralActivityEntity;
import io.live_mall.modules.server.service.IntegralActivityService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author yewl
 * @date 2023/1/30 16:41
 * @description
 */
@Service("integralActivityService")
public class IntegralActivityServiceImpl extends ServiceImpl<IntegralActivityDao, IntegralActivityEntity> implements IntegralActivityService {
    @Override
    public PageUtils integralActivityPages(Map<String, Object> params) {
        return new PageUtils(this.baseMapper.integralActivityPages(new Query<IntegralActivityEntity>().getPage(params)));
    }
}
