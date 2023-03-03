package io.live_mall.modules.server.model;

import lombok.Data;

/**
 * @author yewl
 * @date 2022/12/26 11:08
 * @description
 */
@Data
public class SysUserModel {

    private Long userId;

    private String realname;

    private String personPhotos;

    private String mobile;

    private String job;

    private String personalIntroduce;

    private String certificateIntroduce;


}
