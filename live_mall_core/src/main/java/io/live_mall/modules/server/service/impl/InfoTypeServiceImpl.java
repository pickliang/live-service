package io.live_mall.modules.server.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;

import io.live_mall.modules.server.dao.InfoTypeDao;
import io.live_mall.modules.server.entity.InfoTypeEntity;
import io.live_mall.modules.server.service.InfoTypeService;


@Service("infoTypeService")
public class InfoTypeServiceImpl extends ServiceImpl<InfoTypeDao, InfoTypeEntity> implements InfoTypeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<InfoTypeEntity> page = this.page(
                new Query<InfoTypeEntity>().getPage(params),
                new QueryWrapper<InfoTypeEntity>().orderByDesc("create_date")
        );

        return new PageUtils(page);
    }

}