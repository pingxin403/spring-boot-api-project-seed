package com.company.project.framework.object;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.company.project.business.enums.DeletedEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractDO implements Serializable {
    /**
     * @fieldName: serialVersionUID
     * @fieldType: long
     */
    private static final long serialVersionUID = 508869767856350L;


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    /**
     * 是否删除(1未删除；0已删除)
     */
    @TableLogic //标记逻辑删除属性
    private DeletedEnum deleted;

}
