package io.live_mall.modules.applets;

import io.live_mall.common.utils.R;
import io.live_mall.modules.applets.service.AppletsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yewl
 * @date 2022/12/28 12:58
 * @description
 */
@RestController
@RequestMapping("applets")
@AllArgsConstructor
public class AppletsController {

    private final AppletsService appletsService;

    /**
     * 获取手机号
     * @param code
     * @return
     */
    @GetMapping(value = "/get-phone")
    public R getPhoneNumber(String code) {
        String phoneNumber = appletsService.getPhoneNumber(code);
        return R.ok().put("data", phoneNumber);
    }
}
