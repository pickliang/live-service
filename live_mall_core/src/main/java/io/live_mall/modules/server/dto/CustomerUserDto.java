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

    private String cardPhoneR;

    private String cardPhoneL;

    private String cardTime;
    /**
     * 邀请码
     */
    private Integer askCode;

}
