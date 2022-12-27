package io.live_mall.modules.server.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;

import io.live_mall.modules.server.dao.SysDicDetailDao;
import io.live_mall.modules.server.entity.SysDicDetailEntity;
import io.live_mall.modules.server.service.SysDicDetailService;


@Service("sysDicDetailService")
public class SysDicDetailServiceImpl extends ServiceImpl<SysDicDetailDao, SysDicDetailEntity> implements SysDicDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SysDicDetailEntity> page = this.page(
                new Query<SysDicDetailEntity>().getPage(params),
                new QueryWrapper<SysDicDetailEntity>()
        );

        return new PageUtils(page);
    }

}