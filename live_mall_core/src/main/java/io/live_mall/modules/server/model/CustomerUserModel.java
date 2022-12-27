package io.live_mall.modules.server.model;

import lombok.Data;

import java.util.Date;

/**
 * @author yewl
 * @date 2022/12/19 13:35
 * @description
 */
@Data
public class CustomerUserModel {
    private String id;

    private String phone;

    private String cardNum;

    private String token;

    private Date expireTime;
}
