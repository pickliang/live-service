package io.live_mall.modules.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.modules.server.dao.CustomerQuestionnaireOptionDao;
import io.live_mall.modules.server.entity.CustomerQuestionnaireOptionEntity;
import io.live_mall.modules.server.service.CustomerQuestionnaireOptionService;
import org.springframework.stereotype.Service;

/**
 * @author yewl
 * @date 2023/1/29 11:27
 * @description
 */
@Service("customerQuestionnaireOptionService")
public class CustomerQuestionnaireOptionServiceImpl extends ServiceImpl<CustomerQuestionnaireOptionDao, CustomerQuestionnaireOptionEntity> implements CustomerQuestionnaireOptionService {
}
