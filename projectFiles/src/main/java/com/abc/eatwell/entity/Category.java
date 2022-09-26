package com.abc.eatwell.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * category
 */
@Data
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //type: 1-food dish category, 2-combo category
    private Integer type;

    // category name
    private String name;

    // sort order
    private Integer sort;

    // time created
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // time updated
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // person created
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    // person updated
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

}
