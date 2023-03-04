package io.live_mall.modules.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.live_mall.common.utils.R;
import io.live_mall.common.utils.ShiroUtils;
import io.live_mall.modules.server.dto.CustomerUserDto;
import io.live_mall.modules.server.entity.CustomerUserEntity;
import io.live_mall.modules.server.entity.OrgRegionEntity;
import io.live_mall.modules.server.entity.SysUserArchivesEntity;
import io.live_mall.modules.server.model.CustomerUserModel;
import io.live_mall.modules.server.model.OrgRegionModel;
import io.live_mall.modules.server.model.SysUserModel;
import io.live_mall.modules.server.service.CustomerUserService;
import io.live_mall.modules.server.service.OrgRegionService;
import io.live_mall.modules.server.service.SysOrgGroupService;
import io.live_mall.modules.server.service.SysUserArchivesService;
import io.live_mall.modules.sys.entity.SysUserEntity;
import io.live_mall.modules.sys.service.SysUserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author yewl
 * @date 2022/12/19 10:48
 * @description
 */
@RestController
@AllArgsConstructor
public class CustomerUserController {
    private final CustomerUserService customerUserService;
    private final SysUserService sysUserService;
    private final OrgRegionService orgRegionService;
    private final SysOrgGroupService sysOrgGroupService;
    private final SysUserArchivesService sysUserArchivesService;


    /**
     * 修改用户证件信息
     * @param user 用户
     * @return
     */
    @PostMapping(value = "/modify-info")
    public R updateCustomerInfo(@RequestBody @Valid CustomerUserDto user) {
        int count = customerUserService.count(Wrappers.lambdaQuery(CustomerUserEntity.class).eq(CustomerUserEntity::getCardNum, user.getCardNum()));
        if (count > 0) {
            return R.error("身份证已被绑定");
        }
        user.setId(ShiroUtils.getUserEntity().getId());
        customerUserService.updateUserInfo(user);
        return R.ok();
    }

    /**
     * 理财师所属地域
     * @return
     */
    @GetMapping(value = "/org-regions")
    public R regions() {
        List<OrgRegionEntity> list = orgRegionService.list(Wrappers.lambdaQuery(OrgRegionEntity.class).eq(OrgRegionEntity::getDelFlag, 0));
        List<OrgRegionModel> models = new ArrayList<>();
        list.forEach(region -> {
            OrgRegionModel model = new OrgRegionModel();
            BeanUtils.copyProperties(region, model);
            String orgGroupIds = sysOrgGroupService.orgGroupIds(region.getName());
            model.setOrgGroupId(orgGroupIds);
            models.add(model);
        });
        return R.ok().put("data", models);
    }

    /**
     * 理财师
     * @param orgGroupId
     * @return
     */
    @GetMapping(value = "/org-regions-sales")
    public R regionsSale(@RequestParam String orgGroupId) {
        String orgGroupIds = orgGroupId.replace(",", "|");
        List<SysUserModel> users = sysUserService.orgRegionUser(orgGroupIds);
        return R.ok().put("data", users);
    }

    /**
     * 绑定理财师
     * @param userId 用户id
     * @return
     */
    @GetMapping(value = "/bind-sale")
    public R bindSale(@RequestParam String userId) {
        CustomerUserModel userEntity = ShiroUtils.getUserEntity();
        int count = customerUserService.count(Wrappers.lambdaQuery(CustomerUserEntity.class).eq(CustomerUserEntity::getId, userEntity.getId()).isNotNull(CustomerUserEntity::getSaleId));
        if (count > 0) {
            return R.error("已绑定理财师");
        }
        boolean update = customerUserService.update(Wrappers.lambdaUpdate(CustomerUserEntity.class)
                .set(CustomerUserEntity::getSaleId, userId).eq(CustomerUserEntity::getId, userEntity.getId()));
        return update ? R.ok() : R.error();
    }

    /**
     * 我的理财师
     * @return
     */
    @GetMapping(value = "/my-sale")
    public R mySale() {
        CustomerUserModel userEntity = ShiroUtils.getUserEntity();
        CustomerUserEntity customerUser = customerUserService.getById(userEntity.getId());
        SysUserModel userModel = null;
        if (Objects.nonNull(customerUser)) {
            // 绑定的有理财师
            if (Objects.nonNull(customerUser.getSaleId())) {
                userModel = new SysUserModel();
                SysUserEntity sysUser = sysUserService.getById(customerUser.getSaleId());
                userModel.setUserId(sysUser.getUserId());
                userModel.setRealname(sysUser.getRealname());
                userModel.setMobile(sysUser.getMobile());
            }else if (Objects.nonNull(customerUser.getCardNum())) {
                // 没有绑定理财师 根据身份证获取理财师信息
                userModel = sysUserService.sysUserByCardNum(customerUser.getCardNum());
            }
            SysUserArchivesEntity sysUserArchives = sysUserArchivesService.getById(customerUser.getSaleId());
            if (Objects.nonNull(userModel) && Objects.nonNull(sysUserArchives)) {
                userModel.setPersonPhotos(sysUserArchives.getHalfLengthPhoto());
                userModel.setHalfLengthPhoto(sysUserArchives.getHalfLengthPhoto());
                userModel.setCertificateIntroduce(sysUserArchives.getCertificateIntroduce());
            }
        }

        return R.ok().put("data", userModel);
    }

}
