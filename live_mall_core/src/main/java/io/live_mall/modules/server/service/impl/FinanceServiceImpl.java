package io.live_mall.modules.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.FinanceDao;
import io.live_mall.modules.server.entity.FinanceEntity;
import io.live_mall.modules.server.model.FinanceModel;
import io.live_mall.modules.server.service.FinanceService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author yewl
 * @date 2023/1/3 14:49
 * @description
 */
@Service("financeService")
public class FinanceServiceImpl extends ServiceImpl<FinanceDao, FinanceEntity> implements FinanceService {

    @Override
    public PageUtils financePages(Map<String, Object> params) {
        return new PageUtils(this.baseMapper.financePages(new Query<FinanceModel>().getPage(params), params));
    }

    @Override
    public FinanceModel financeInfo(Long id) {
        FinanceEntity finance = this.baseMapper.selectById(id);
        FinanceModel model = new FinanceModel();
        BeanUtils.copyProperties(finance, model);
        return model;
    }

    @Override
    public List<FinanceEntity> companyDynamics(Integer classify) {
        return this.baseMapper.selectList(Wrappers.lambdaQuery(FinanceEntity.class).eq(FinanceEntity::getClassify, classify)
                .eq(FinanceEntity::getStatus, 0).eq(FinanceEntity::getDelFlag, 0).orderByDesc(FinanceEntity::getCreateTime)
                .select(FinanceEntity::getId, FinanceEntity::getTitle, FinanceEntity::getCoverImg, FinanceEntity::getOutline)
                .last("LIMIT 3"));
    }

    @Override
    public PageUtils financeList(Map<String, Object> params) {
        return new PageUtils(this.baseMapper.financeList(new Query<FinanceEntity>().getPage(params), params));
    }
}
