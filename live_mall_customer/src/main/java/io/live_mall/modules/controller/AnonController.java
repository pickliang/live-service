package io.live_mall.modules.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import io.live_mall.common.utils.R;
import io.live_mall.modules.server.entity.CustomerBannerEntity;
import io.live_mall.modules.server.entity.CustomerUserEntity;
import io.live_mall.modules.server.model.CustomerUserModel;
import io.live_mall.modules.server.model.InformationModel;
import io.live_mall.modules.server.service.CustomerBannerService;
import io.live_mall.modules.server.service.CustomerUserService;
import io.live_mall.modules.server.service.InformationService;
import io.live_mall.modules.sys.oauth2.TokenGenerator;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author yewl
 * @date 2023/1/6 13:01
 * @description 不需要登录的api
 */
@RestController
@AllArgsConstructor
@RequestMapping("anon")
public class AnonController {
    private final InformationService informationService;
    private final CustomerUserService customerUserService;
    private final CustomerBannerService customerBannerService;


    /**
     * 登录
     * @param phone 手机号
     * @return
     */
    @GetMapping(value = "/login")
    public R login(String phone) {
        if (StringUtils.isBlank(phone)) {
            return R.error("手机号不可为空");
        }
        // 根据手机号获取用户信息
        CustomerUserModel user = customerUserService.login(phone);
        String token = TokenGenerator.generateValue();
        //当前时间
        Date now = new Date();
        //过期时间
        Date expireTime = Date.from(LocalDateTime.now().plusDays(7).atZone(ZoneId.systemDefault()).toInstant());
        if (Objects.isNull(user)) {
            // 创建用户
            CustomerUserEntity userEntity = new CustomerUserEntity();
            userEntity.setPhone(phone);
            userEntity.setToken(token);
            userEntity.setExpireTime(expireTime);
            userEntity.setTokenUptTime(now);
            userEntity.setCreateDate(now);
            customerUserService.save(userEntity);
            user = new CustomerUserModel();
            user.setId(userEntity.getId());
        }else {
            customerUserService.update(Wrappers.lambdaUpdate(CustomerUserEntity.class)
                    .set(CustomerUserEntity::getToken, token)
                    .set(CustomerUserEntity::getExpireTime, expireTime)
                    .eq(CustomerUserEntity::getId, user.getId()));
        }
        user.setToken(token);
        user.setPhone(phone);
        user.setExpireTime(expireTime);
        return R.ok().put("data", user);
    }


    /**
     * 投资资讯
     * @return
     */
    @GetMapping(value = "/information-list")
    public R customerInformation() {
        Map<String, Object> result = Maps.newHashMap();
        // 白话财经
        List<InformationModel> vernacular = informationService.customerInformation(1);
        // 固收资讯
        List<InformationModel> income = informationService.customerInformation(2);
        // 股权资讯
        List<InformationModel> stock  = informationService.customerInformation(3);
        // 二级市场资讯
        List<InformationModel> market = informationService.customerInformation(4);
        result.put("vernacular", vernacular);
        result.put("income", income);
        result.put("stock", stock);
        result.put("market", market);
        return R.ok().put("data", result);
    }

    /**
     * 轮播图
     * @return
     */
    @GetMapping(value = "/banners")
    public R customerBanners() {
        List<CustomerBannerEntity> list = customerBannerService.list(Wrappers.lambdaQuery(CustomerBannerEntity.class)
                .eq(CustomerBannerEntity::getStatus, 0));
        return R.ok().put("data", list);
    }

}
