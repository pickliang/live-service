package io.live_mall.modules.server.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;

import io.live_mall.modules.server.dao.InfoFormDao;
import io.live_mall.modules.server.entity.InfoFormEntity;
import io.live_mall.modules.server.service.InfoFormService;


@Service("infoFormService")
public class InfoFormServiceImpl extends ServiceImpl<InfoFormDao, InfoFormEntity> implements InfoFormService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<InfoFormEntity> page = this.page(
                new Query<InfoFormEntity>().getPage(params),
                new QueryWrapper<InfoFormEntity>().orderByDesc("create_date")
        );

        return new PageUtils(page);
    }

}