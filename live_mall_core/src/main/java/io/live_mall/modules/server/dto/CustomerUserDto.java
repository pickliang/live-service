package io.live_mall.modules.server.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yewl
 * @date 2022/12/20 15:03
 * @description
 */
@Data
public class CustomerUserDto {

    private String id;

    @NotBlank(message = "名称不可为空")
    private String name;

    @NotBlank(message = "证件号不可为空")
    private String cardNum;

    @NotBlank(message = "证件不可为空")
    private String cardPhoneR;

    @NotBlank(message = "证件不可为空")
    private String cardPhoneL;

    @NotBlank(message = "证件有效期不可为空")
    private String cardTime;
    /**
     * 邀请码
     */
    private Integer askCode;

}
