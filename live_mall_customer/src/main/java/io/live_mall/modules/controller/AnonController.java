package io.live_mall.modules.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.live_mall.common.utils.R;
import io.live_mall.modules.server.entity.CustomerUserEntity;
import io.live_mall.modules.server.entity.InformationBrowseEntity;
import io.live_mall.modules.server.entity.InformationEntity;
import io.live_mall.modules.server.model.CustomerUserModel;
import io.live_mall.modules.server.model.InformationModel;
import io.live_mall.modules.server.service.CustomerUserService;
import io.live_mall.modules.server.service.InformationBrowseService;
import io.live_mall.modules.server.service.InformationService;
import io.live_mall.modules.sys.oauth2.TokenGenerator;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
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
    private final InformationBrowseService informationBrowseService;

    /**
     * 登录
     * @param phone 手机号
     * @return
     */
    @GetMapping(value = "/login")
    public R login(String phone) {
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
     * 资讯详情
     * @param id 主键id
     * @return
     */
    @GetMapping(value = "/information-info/{id}")
    public R customerInformationInfo(@PathVariable("id") String id) {
        InformationEntity information = informationService.getById(id);
        if (Objects.nonNull(information) && 1 == information.getDelFlag()) {
            return R.error("资讯已删除");
        }
        InformationModel model = new InformationModel();
        BeanUtils.copyProperties(information, model);
        return R.ok().put("data", model);
    }

    /**
     * 资讯浏览保存
     * @param entity
     * @return
     */
    @PostMapping(value = "/information-browser")
    public R informationBrowserSave(@RequestBody InformationBrowseEntity entity) {
        if (StringUtils.isBlank(entity.getInformationId())) {
            return R.error();
        }
        entity.setCreateTime(new Date());
        boolean save = informationBrowseService.save(entity);
        return save ? R.ok() : R.error();
    }
}
