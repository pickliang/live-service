package io.live_mall.modules.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.modules.server.dao.CustomerUserQuestionnaireDao;
import io.live_mall.modules.server.entity.CustomerUserQuestionnaireEntity;
import io.live_mall.modules.server.service.CustomerUserQuestionnaireService;
import org.springframework.stereotype.Service;

/**
 * @author yewl
 * @date 2023/1/29 14:26
 * @description
 */
@Service("customerUserQuestionnaireService")
public class CustomerUserQuestionnaireServiceImpl extends ServiceImpl<CustomerUserQuestionnaireDao, CustomerUserQuestionnaireEntity> implements CustomerUserQuestionnaireService {
}
