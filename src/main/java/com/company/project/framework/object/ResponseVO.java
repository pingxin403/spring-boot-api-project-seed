package com.company.project.framework.object;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.company.project.framework.exception.code.BaseResponseCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;

/**
 * controller返回json模板
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class ResponseVO<T> {
    private Integer status;
    private String msg;
    private T data;

    public ResponseVO(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public ResponseVO(BaseResponseCode status, T data) {
        this(status.getCode(), status.getMsg(), data);
    }

    public String toJson() {
        T t = this.getData();
        if (t instanceof Collection) {
            return JSONObject.toJSONString(this, SerializerFeature.WriteNullListAsEmpty);
        } else {
            return JSONObject.toJSONString(this, SerializerFeature.WriteMapNullValue);
        }
    }
}
