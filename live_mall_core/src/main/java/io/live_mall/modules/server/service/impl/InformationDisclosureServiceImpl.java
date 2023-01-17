package io.live_mall.modules.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.InformationDisclosureAnnexDao;
import io.live_mall.modules.server.dao.InformationDisclosureDao;
import io.live_mall.modules.server.dto.InformationDisclosureDto;
import io.live_mall.modules.server.entity.InformationDisclosureAnnexEntity;
import io.live_mall.modules.server.entity.InformationDisclosureEntity;
import io.live_mall.modules.server.model.InformationDisclosureModel;
import io.live_mall.modules.server.service.InformationDisclosureService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author yewl
 * @date 2023/1/16 15:16
 * @description
 */
@Service("informationDisclosureService")
@AllArgsConstructor
public class InformationDisclosureServiceImpl extends ServiceImpl<InformationDisclosureDao, InformationDisclosureEntity> implements InformationDisclosureService {
    private final InformationDisclosureAnnexDao informationDisclosureAnnexDao;

    @Override
    public PageUtils pages(Map<String, Object> params) {
        IPage<InformationDisclosureModel> pages = this.baseMapper.pages(new Query<InformationDisclosureModel>().getPage(params), params);
        return new PageUtils(pages);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int saveOrUpdateInformationDisclosure(InformationDisclosureDto disclosureDto, Long userId) {
        InformationDisclosureEntity entity = new InformationDisclosureEntity();
        BeanUtils.copyProperties(disclosureDto, entity);
        int n = 0;
        if (StringUtils.isNotBlank(disclosureDto.getId())) {
            entity.setUpdateTime(new Date());
            entity.setUpdateUser(userId);
            n = this.baseMapper.updateById(entity);
            informationDisclosureAnnexDao.update(new InformationDisclosureAnnexEntity(), Wrappers.lambdaUpdate(InformationDisclosureAnnexEntity.class)
                    .set(InformationDisclosureAnnexEntity::getDelFlag, 1)
                    .eq(InformationDisclosureAnnexEntity::getInformationDisclosureId, disclosureDto.getId()));
        }else {
            entity.setCreateTime(new Date());
            entity.setCreateUser(userId);
            n = this.baseMapper.insert(entity);
        }
        disclosureDto.getAnnexes().forEach(annex -> {
            annex.setInformationDisclosureId(entity.getId());
            String url = annex.getUrl();
            annex.setSuffix(url.substring(url.lastIndexOf("."), url.length()));
            informationDisclosureAnnexDao.insert(annex);
        });
        return n;
    }

    @Override
    public PageUtils customerPages(Map<String, Object> params) {
        return new PageUtils(this.baseMapper.customerPages(new Query<JSONObject>().getPage(params), params));
    }

    @Override
    public InformationDisclosureModel informationDisclosureInfo(String id) {
        InformationDisclosureModel model = this.baseMapper.informationDisclosureInfo(id);
        List<InformationDisclosureAnnexEntity> annexes = informationDisclosureAnnexDao.selectList(Wrappers.lambdaQuery(InformationDisclosureAnnexEntity.class)
                .eq(InformationDisclosureAnnexEntity::getInformationDisclosureId, id).eq(InformationDisclosureAnnexEntity::getDelFlag, 0));
        model.setAnnexes(annexes);
        return model;
    }


}
