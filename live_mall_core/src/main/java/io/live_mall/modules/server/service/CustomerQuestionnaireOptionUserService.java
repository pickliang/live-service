package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.R;
import io.live_mall.modules.server.entity.CustomerQuestionnaireOptionUserEntity;

/**
 * @author yewl
 * @date 2023/1/29 11:28
 * @description
 */
public interface CustomerQuestionnaireOptionUserService extends IService<CustomerQuestionnaireOptionUserEntity> {
    R answer(String userId, String id, String questionnaireOptionId);
}
