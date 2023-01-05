package io.live_mall.modules.server.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.InformationDao;
import io.live_mall.modules.server.entity.InformationEntity;
import io.live_mall.modules.server.model.InformationModel;
import io.live_mall.modules.server.service.InformationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author yewl
 * @date 2023/1/3 16:20
 * @description
 */
@Service("informationService")
public class InformationServiceImpl extends ServiceImpl<InformationDao, InformationEntity> implements InformationService {
    @Override
    public PageUtils informationPages(Map<String, Object> params) {
        IPage<InformationModel> pages = this.baseMapper.informationPages(new Query<InformationModel>().getPage(params), params);
        return new PageUtils(pages);
    }

    @Override
    public List<InformationModel> customerInformation(Integer classify) {
        return this.baseMapper.customerInformation(classify);
    }

    @Override
    public PageUtils customerInformationPages(Map<String, Object> params) {
        return new PageUtils(this.baseMapper.customerInformationPages(new Query<InformationModel>().getPage(params), params));
    }
}
