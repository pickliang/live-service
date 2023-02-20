package io.live_mall.modules.applets;

import io.live_mall.common.utils.R;
import io.live_mall.modules.applets.service.AppletsService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

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
    @SneakyThrows
    public R getPhoneNumber(String code) {
        String phoneNumber = appletsService.getPhoneNumber(code);
        return R.ok().put("data", phoneNumber);
    }

    /**
     * 获取不限制的小程序码
     * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/qrcode-link/qr-code/getUnlimitedQRCode.html
     * @param scene 参数
     * @param page 默认是主页，页面 page，例如 pages/index/index，根路径前不要填加 /，不能携带参数
     * @param checkPath 默认是true，检查page 是否存在，为 true 时 page 必须是已经发布的小程序存在的页面（否则报错）；
     *                  为 false 时允许小程序未发布或者 page 不存在， 但page 有数量上限（60000个）请勿滥用。
     * @param envVersion 要打开的小程序版本。正式版为 "release"，体验版为 "trial"，开发版为 "develop"。默认是正式版。
     * @return
     */
    @GetMapping(value = "/unlimit-code")
    @SneakyThrows
    public R getUnlimitedQRCode(@RequestParam String scene, @RequestParam String page, @RequestParam Boolean checkPath, @RequestParam String envVersion) {
        byte[] result = appletsService.getUnlimitedQRCode(scene, page, checkPath, envVersion);
        BASE64Encoder encoder = new BASE64Encoder();
        return R.ok().put("data", encoder.encode(result));
    }

    /**
     * 获取小程序跳转链接
     * @param envVersion
     * @return
     */
    @GetMapping(value = "/url-link")
    @SneakyThrows
    public R getUrlLink(@RequestParam String envVersion) {
        String urlLink = appletsService.getUrlLink(envVersion);
        return R.ok().put("data", urlLink);
    }
}
