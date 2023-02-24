package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yewl
 * @date 2023/2/23 16:14
 * @description
 */
@Data
@TableName("t_touch_user")
public class TouchUserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 小鹅通用户id
     */
    @Id
    @TableId(value = "user_id", type = IdType.INPUT)
    private String userId;
    /**
     * 客户编号
     */
    private String memberNo;

    /**
     * 昵称
     */
    private String nickname;
    /**
     * 真实姓名
     */
    private String name;
    /**
     * 压缩后的头像
     */
    private String avatar;
    /**
     * 性别 0-无 1-男 2-女
     */
    private Integer gender;
    /**
     * 手机号码  可能会带区号，如+86-139xxxx
     */
    private String phone;
    /**
     * 用户状态:-1-已注销，0-正常，1-已封号， -2-待注销状态（用户主动注销，30天内进入店铺将会变成正常状态，3-待激活（用户未正常登录的账号）
     */
    private Integer isSeal;
    /**
     * 信息采集手机号
     */
    private String phoneCollect;
    /**
     * sdk用户id
     */
    private String sdkUserId;
    /**
     * 用户创建时间
     */
    private String createdAt;

    private String appId;
    /**
     * 地区编码
     */
    private String areaCode;
    /**
     * phone-来源手机号 collect-来源信息采集
     */
    private String phoneResource;
    /**
     * 创建时间
     */
    private Date createTime;


}
