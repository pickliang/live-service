package io.live_mall.modules.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.modules.server.dao.CustomerBannerDao;
import io.live_mall.modules.server.entity.CustomerBannerEntity;
import io.live_mall.modules.server.service.CustomerBannerService;
import org.springframework.stereotype.Service;

/**
 * @author yewl
 * @date 2023/2/15 14:03
 * @description
 */
@Service("customerBannerService")
public class CustomerBannerServiceImpl extends ServiceImpl<CustomerBannerDao, CustomerBannerEntity> implements CustomerBannerService {
}
