package io.live_mall.modules.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.InformationBrowseDao;
import io.live_mall.modules.server.entity.InformationBrowseEntity;
import io.live_mall.modules.server.model.InformationBrowserModel;
import io.live_mall.modules.server.service.InformationBrowseService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author yewl
 * @date 2023/1/6 12:44
 * @description
 */
@Service
public class InformationBrowseServiceImpl extends ServiceImpl<InformationBrowseDao, InformationBrowseEntity> implements InformationBrowseService {

    @Override
    public PageUtils informationBrowserList(Map<String, Object> params) {
        return new PageUtils(this.baseMapper.informationBrowserList(new Query<InformationBrowserModel>().getPage(params), params));
    }

    @Override
    public Long countPeopleNumber(String informationId) {
        return this.baseMapper.countPeopleNumber(informationId);
    }
}
