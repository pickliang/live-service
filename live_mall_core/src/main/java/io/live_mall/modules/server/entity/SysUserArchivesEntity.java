package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * @author yewl
 * @date 2023/2/17 16:44
 * @description
 */
@Data
@TableName("sys_user_archives")
public class SysUserArchivesEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Id
    @TableId(value = "user_id", type = IdType.INPUT)
    private Long userId;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 从业经历
     */
    private String experience;
    /**
     * 职业证书介绍
     */
    private String certificateIntroduce;
    /**
     * 职业证书图片
     */
    private String certificateImage;
    /**
     * 个人介绍
     */
    private String personalIntroduce;
    /**
     * 职务
     */
    private String job;
    /**
     * 首页透明底半身照
     */
    private String halfLengthPhoto;
    /**
     * 职业照
     */
    private String certificatePhoto;
    /**
     * 推荐到客户端小程序  0-是；1-否
     */
    private Integer status;
}
