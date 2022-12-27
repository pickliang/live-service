package io.live_mall.modules.server.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;

import io.live_mall.modules.server.dao.OrgTaskDetailDao;
import io.live_mall.modules.server.entity.OrgTaskDetailEntity;
import io.live_mall.modules.server.service.OrgTaskDetailService;


@Service("orgTaskDetailService")
public class OrgTaskDetailServiceImpl extends ServiceImpl<OrgTaskDetailDao, OrgTaskDetailEntity> implements OrgTaskDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrgTaskDetailEntity> page = this.page(
                new Query<OrgTaskDetailEntity>().getPage(params),
                new QueryWrapper<OrgTaskDetailEntity>()
        );
        return new PageUtils(page);
    }

}