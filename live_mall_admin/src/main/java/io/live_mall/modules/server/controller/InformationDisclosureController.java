package io.live_mall.modules.server.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.R;
import io.live_mall.common.utils.ShiroUtils;
import io.live_mall.modules.server.dto.InformationDisclosureDto;
import io.live_mall.modules.server.entity.InformationDisclosureAnnexEntity;
import io.live_mall.modules.server.entity.InformationDisclosureEntity;
import io.live_mall.modules.server.model.InformationDisclosureModel;
import io.live_mall.modules.server.service.InformationDisclosureAnnexService;
import io.live_mall.modules.server.service.InformationDisclosureService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author yewl
 * @date 2023/1/16 15:19
 * @description
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("information-disclosure")
public class InformationDisclosureController {
    private final InformationDisclosureService informationDisclosureService;
    private final InformationDisclosureAnnexService informationDisclosureAnnexService;

    /**
     * 信息披露列表
     * @param params
     * @return
     */
    @GetMapping(value = "/list")
    @RequiresPermissions("server:disclosure:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils pages = informationDisclosureService.pages(params);
        return R.ok().put("data", pages);
    }

    /**
     * 信息披露新增或修改
     * @param disclosureDto
     * @return
     */
    @PostMapping(value = "/save-update")
    @RequiresPermissions("server:disclosure:save")
    public R save(@RequestBody @Valid InformationDisclosureDto disclosureDto) {
        int save = informationDisclosureService.saveOrUpdateInformationDisclosure(disclosureDto, ShiroUtils.getUserId());
        return save > 0 ? R.ok() : R.error();
    }

    /**
     * 信息披露删除
     * @param id 主键id
     * @return
     */
    @PutMapping(value = "/delete/{id}")
    @RequiresPermissions("server:disclosure:update")
    public R delete(@PathVariable("id") String id) {
        boolean update = informationDisclosureService.update(Wrappers.lambdaUpdate(InformationDisclosureEntity.class)
                .set(InformationDisclosureEntity::getDelFlag, 1).eq(InformationDisclosureEntity::getId, id));
        return update ? R.ok() : R.error();
    }

    /**
     * 信息披露详情
     * @param id
     * @return
     */
    @GetMapping(value = "/info/{id}")
    @RequiresPermissions("server:disclosure:info")
    public R info(@PathVariable("id") String id) {
        InformationDisclosureEntity entity = informationDisclosureService.getById(id);
        InformationDisclosureModel model = new InformationDisclosureModel();
        BeanUtils.copyProperties(entity, model);
        List<InformationDisclosureAnnexEntity> annexes = informationDisclosureAnnexService.list(Wrappers.lambdaQuery(InformationDisclosureAnnexEntity.class)
                .eq(InformationDisclosureAnnexEntity::getInformationDisclosureId, id));
        model.setAnnexes(annexes);
        return R.ok().put("data", model);
    }
}
