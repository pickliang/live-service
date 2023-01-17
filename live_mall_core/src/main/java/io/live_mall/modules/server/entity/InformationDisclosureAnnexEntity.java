package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yewl
 * @date 2023/1/16 15:10
 * @description
 */
@Data
@TableName("information_disclosure_annex")
public class InformationDisclosureAnnexEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 信息披露id
     */
    private String informationDisclosureId;
    /**
     * 信息披露地址
     */
    private String url;
    /**
     * 附件名称
     */
    private String name;
    /**
     * 文件后缀
     */
    private String suffix;

    private Integer delFlag;
}
