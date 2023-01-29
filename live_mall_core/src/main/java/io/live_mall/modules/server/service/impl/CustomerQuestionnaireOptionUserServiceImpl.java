package io.live_mall.modules.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.R;
import io.live_mall.modules.server.dao.CustomerQuestionnaireDao;
import io.live_mall.modules.server.dao.CustomerQuestionnaireOptionDao;
import io.live_mall.modules.server.dao.CustomerQuestionnaireOptionUserDao;
import io.live_mall.modules.server.dao.CustomerUserQuestionnaireDao;
import io.live_mall.modules.server.entity.CustomerQuestionnaireEntity;
import io.live_mall.modules.server.entity.CustomerQuestionnaireOptionEntity;
import io.live_mall.modules.server.entity.CustomerQuestionnaireOptionUserEntity;
import io.live_mall.modules.server.entity.CustomerUserQuestionnaireEntity;
import io.live_mall.modules.server.service.CustomerQuestionnaireOptionUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yewl
 * @date 2023/1/29 11:28
 * @description
 */
@Service("customerQuestionnaireUserService")
@AllArgsConstructor
public class CustomerQuestionnaireOptionUserServiceImpl extends ServiceImpl<CustomerQuestionnaireOptionUserDao, CustomerQuestionnaireOptionUserEntity> implements CustomerQuestionnaireOptionUserService {

    private final CustomerQuestionnaireDao customerQuestionnaireDao;
    private final CustomerUserQuestionnaireDao customerUserQuestionnaireDao;
    private final CustomerQuestionnaireOptionDao customerQuestionnaireOptionDao;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public R answer(String userId, String id, String questionnaireOptionId) {
        List<CustomerQuestionnaireEntity> questionnaireEntities = customerQuestionnaireDao.selectList(Wrappers.lambdaQuery(CustomerQuestionnaireEntity.class).eq(CustomerQuestionnaireEntity::getDelFlag, 0));
        List<CustomerQuestionnaireOptionUserEntity> optionUserEntityList = this.baseMapper.selectList(Wrappers.lambdaQuery(CustomerQuestionnaireOptionUserEntity.class).eq(CustomerQuestionnaireOptionUserEntity::getDelFlag, 0));
        if (questionnaireEntities.size() == optionUserEntityList.size()) {
            return R.error("已完成作答");
        }
        CustomerQuestionnaireOptionEntity optionEntity = customerQuestionnaireOptionDao.selectById(questionnaireOptionId);
        CustomerQuestionnaireOptionUserEntity entity = new CustomerQuestionnaireOptionUserEntity();
        entity.setCustomerUserId(userId);
        entity.setQuestionnaireId(id);
        entity.setQuestionnaireOptionId(questionnaireOptionId);
        entity.setScore(optionEntity.getScore());
        entity.setCreateTime(new Date());
        int update = this.baseMapper.update(entity, Wrappers.lambdaUpdate(CustomerQuestionnaireOptionUserEntity.class)
                .eq(CustomerQuestionnaireOptionUserEntity::getCustomerUserId, userId).eq(CustomerQuestionnaireOptionUserEntity::getQuestionnaireId, id)
                .eq(CustomerQuestionnaireOptionUserEntity::getDelFlag, 0));
        if (0 == update) {
            this.baseMapper.insert(entity);
        }

        if (questionnaireEntities.size() == optionUserEntityList.size()) {
            Integer score = optionUserEntityList.stream().collect(Collectors.summingInt(CustomerQuestionnaireOptionUserEntity::getScore));
            CustomerUserQuestionnaireEntity userQuestionnaireEntity = new CustomerUserQuestionnaireEntity();
            userQuestionnaireEntity.setCustomerUserId(userId);
            userQuestionnaireEntity.setScore(score);
            String grade = (score > 20 && score <= 37) ? "稳健型" : (score > 37 && score <= 53) ? "平衡型" : (score > 53 && score <= 82) ? "成长型" : score > 82 ? "积极型": "保守型";
            userQuestionnaireEntity.setGrade(grade);
            userQuestionnaireEntity.setCreateTime(new Date());
            customerUserQuestionnaireDao.insert(userQuestionnaireEntity);
        }
        return R.ok();
    }
}
