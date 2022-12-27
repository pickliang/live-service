package io.live_mall.modules.server.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;

import io.live_mall.modules.server.dao.SysTxAiDao;
import io.live_mall.modules.server.entity.SysTxAiEntity;
import io.live_mall.modules.server.service.SysTxAiService;


@Service("sysTxAiService")
public class SysTxAiServiceImpl extends ServiceImpl<SysTxAiDao, SysTxAiEntity> implements SysTxAiService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SysTxAiEntity> page = this.page(
                new Query<SysTxAiEntity>().getPage(params),
                new QueryWrapper<SysTxAiEntity>()
        );

        return new PageUtils(page);
    }

}