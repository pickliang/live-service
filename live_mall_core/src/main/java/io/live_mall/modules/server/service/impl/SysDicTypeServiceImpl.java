package io.live_mall.modules.server.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;

import io.live_mall.modules.server.dao.SysDicTypeDao;
import io.live_mall.modules.server.entity.SysDicTypeEntity;
import io.live_mall.modules.server.service.SysDicTypeService;


@Service("sysDicTypeService")
public class SysDicTypeServiceImpl extends ServiceImpl<SysDicTypeDao, SysDicTypeEntity> implements SysDicTypeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SysDicTypeEntity> page = this.page(
                new Query<SysDicTypeEntity>().getPage(params),
                new QueryWrapper<SysDicTypeEntity>()
        );

        return new PageUtils(page);
    }

}