/**
 * Copyright (c) 2016-2019 分销峰开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.live_mall.modules.sys.service.impl;

import io.live_mall.modules.sys.service.ShiroService;
import io.live_mall.modules.server.dao.CustomerUserDao;
import io.live_mall.modules.server.model.CustomerUserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShiroServiceImpl implements ShiroService {
    @Autowired
    private CustomerUserDao customerUserDao;
    @Override
    public CustomerUserModel queryByToken(String token) {
        return customerUserDao.queryByToken(token);
    }
}
