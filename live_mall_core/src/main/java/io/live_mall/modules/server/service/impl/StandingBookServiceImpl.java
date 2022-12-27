package io.live_mall.modules.server.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;

import io.live_mall.modules.server.dao.StandingBookDao;
import io.live_mall.modules.server.entity.StandingBookEntity;
import io.live_mall.modules.server.service.StandingBookService;


@Service("standingBookService")
public class StandingBookServiceImpl extends ServiceImpl<StandingBookDao, StandingBookEntity> implements StandingBookService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<StandingBookEntity> page = this.page(
                new Query<StandingBookEntity>().getPage(params),
                new QueryWrapper<StandingBookEntity>().orderByDesc("create_date")
        );

        return new PageUtils(page);
    }

}