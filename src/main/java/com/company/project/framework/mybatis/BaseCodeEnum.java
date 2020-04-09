package com.company.project.framework.mybatis;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;


@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public interface BaseCodeEnum {
    Integer getCode();

    @JsonValue
    String getMsg();


}
