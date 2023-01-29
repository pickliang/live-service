package io.live_mall.modules.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.modules.server.dao.CustomerQuestionnaireDao;
import io.live_mall.modules.server.entity.CustomerQuestionnaireEntity;
import io.live_mall.modules.server.service.CustomerQuestionnaireService;
import org.springframework.stereotype.Service;

/**
 * @author yewl
 * @date 2023/1/29 11:26
 * @description
 */
@Service("customerQuestionnaireService")
public class CustomerQuestionnaireServiceImpl extends ServiceImpl<CustomerQuestionnaireDao, CustomerQuestionnaireEntity> implements CustomerQuestionnaireService {
}
