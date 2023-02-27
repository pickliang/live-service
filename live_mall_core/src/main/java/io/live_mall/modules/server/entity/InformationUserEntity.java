package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yewl
 * @date 2023/2/27 11:18
 * @description
 */
@Data
@TableName("information_user")
public class InformationUserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 理财师id
     */
    private Long userId;
    /**
     * 资讯id
     */
    private Long informationId;
    /**
     * 创建时间
     */
    private Date createTime;
}
