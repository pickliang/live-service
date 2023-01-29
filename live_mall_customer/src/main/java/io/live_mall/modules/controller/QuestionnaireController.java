package io.live_mall.modules.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.live_mall.common.utils.R;
import io.live_mall.common.utils.ShiroUtils;
import io.live_mall.modules.server.entity.CustomerQuestionnaireEntity;
import io.live_mall.modules.server.entity.CustomerQuestionnaireOptionEntity;
import io.live_mall.modules.server.entity.CustomerQuestionnaireOptionUserEntity;
import io.live_mall.modules.server.entity.CustomerUserQuestionnaireEntity;
import io.live_mall.modules.server.model.CustomerQuestionnaireModel;
import io.live_mall.modules.server.model.CustomerQuestionnaireOptionModel;
import io.live_mall.modules.server.service.CustomerQuestionnaireOptionService;
import io.live_mall.modules.server.service.CustomerQuestionnaireOptionUserService;
import io.live_mall.modules.server.service.CustomerQuestionnaireService;
import io.live_mall.modules.server.service.CustomerUserQuestionnaireService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yewl
 * @date 2023/1/29 11:30
 * @description
 */
@RestController
@RequestMapping("questionnaire")
@AllArgsConstructor
public class QuestionnaireController {

    private final CustomerQuestionnaireService customerQuestionnaireService;
    private final CustomerQuestionnaireOptionService customerQuestionnaireOptionService;
    private final CustomerQuestionnaireOptionUserService customerQuestionnaireOptionUserService;
    private final CustomerUserQuestionnaireService customerUserQuestionnaireService;

    /**
     * 问卷分数
     * @return
     */
    @GetMapping(value = "/score")
    public R score() {
        String userId = ShiroUtils.getUserId();
        CustomerUserQuestionnaireEntity userQuestionnaireEntity = customerUserQuestionnaireService.getOne(Wrappers.lambdaQuery(CustomerUserQuestionnaireEntity.class)
                .eq(CustomerUserQuestionnaireEntity::getCustomerUserId, userId).eq(CustomerUserQuestionnaireEntity::getDelFlag, 0).last("LIMIT 1"));
        Integer score = Objects.nonNull(userQuestionnaireEntity) ? userQuestionnaireEntity.getScore() : 0;
        return R.ok().put("data", score);
    }

    /**
     * 问卷
     * @return
     */
    @GetMapping(value = "/list")
    public R list() {
        String userId = ShiroUtils.getUserId();
        // 问卷题
        List<CustomerQuestionnaireEntity> questionnaireList = customerQuestionnaireService.list(Wrappers.lambdaQuery(CustomerQuestionnaireEntity.class)
                .eq(CustomerQuestionnaireEntity::getDelFlag, 0).orderByAsc(CustomerQuestionnaireEntity::getOrderNumber));
        // 问卷选项
        List<CustomerQuestionnaireOptionEntity> optionEntityList = customerQuestionnaireOptionService.list(Wrappers.lambdaQuery(CustomerQuestionnaireOptionEntity.class)
                .eq(CustomerQuestionnaireOptionEntity::getDelFlag, 0).orderByAsc(CustomerQuestionnaireOptionEntity::getQuestionnaireId));
        Map<String, List<CustomerQuestionnaireOptionEntity>> optionMaps = optionEntityList.stream().collect(Collectors.groupingBy(CustomerQuestionnaireOptionEntity::getQuestionnaireId));
        // 用户已答
        List<CustomerQuestionnaireOptionUserEntity> optionUserEntities = customerQuestionnaireOptionUserService.list(Wrappers.lambdaQuery(CustomerQuestionnaireOptionUserEntity.class)
                .eq(CustomerQuestionnaireOptionUserEntity::getCustomerUserId, userId).eq(CustomerQuestionnaireOptionUserEntity::getDelFlag, 0));
        Map<String, CustomerQuestionnaireOptionUserEntity> userEntityMap = optionUserEntities.stream().collect(Collectors.toMap(CustomerQuestionnaireOptionUserEntity::getQuestionnaireOptionId, entity -> entity));
        // 返回的结果
        List<CustomerQuestionnaireModel> models = new ArrayList<>();
        // 问卷题匹配选项
        questionnaireList.forEach(questionnaire -> {
            // 问卷题的选项
            List<CustomerQuestionnaireOptionEntity> optionEntities= optionMaps.get(questionnaire.getId());
            List<CustomerQuestionnaireOptionModel> optionList = new ArrayList<>();
            // 遍历已答问卷选项
            optionEntities.forEach(option -> {
                CustomerQuestionnaireOptionModel optionModel = new CustomerQuestionnaireOptionModel();
                BeanUtils.copyProperties(option, optionModel);
                CustomerQuestionnaireOptionUserEntity optionUserEntity = userEntityMap.get(option.getId());
                if (Objects.nonNull(optionUserEntity)) {
                    optionModel.setCheck(true);
                }
                optionList.add(optionModel);
            });

            CustomerQuestionnaireModel model = new CustomerQuestionnaireModel();
            BeanUtils.copyProperties(questionnaire, model);
            model.setOptionList(optionList);
            models.add(model);
        });
        return R.ok().put("data", models);
    }

    /**
     * 答题
     * @param id 问卷id
     * @param questionnaireOptionId 选项id
     * @return
     */
    @GetMapping(value = "/answer")
    public R answer(String id,String questionnaireOptionId) {
        return customerQuestionnaireOptionUserService.answer(ShiroUtils.getUserId(), id, questionnaireOptionId);
    }
}
