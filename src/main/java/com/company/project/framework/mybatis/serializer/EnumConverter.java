package com.company.project.framework.mybatis.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.company.project.framework.mybatis.BaseCodeEnum;
import com.company.project.util.CodeEnumUtil;

import java.lang.reflect.Type;

/**
 * @author hyp
 * Project name is spring-boot-api-project-seed
 * Include in com.company.project.framework.config.serializer
 * hyp create at 20-4-4
 **/
public class EnumConverter implements ObjectSerializer, ObjectDeserializer {

    /**
     * fastjson 序列化
     *
     * @param serializer
     * @param object
     * @param fieldName
     * @param fieldType
     * @param features
     */
    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) {
        serializer.write(((BaseCodeEnum) object).getMsg());
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }

    /**
     * fastjson 反序列化
     *
     * @param parser
     * @param type
     * @param fieldName
     * @param <T>
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        Class enumType = (Class) type;

        // 类型校验：枚举类型并且实现 EnumValue 接口
        if (!enumType.isEnum() || !EnumValue.class.isAssignableFrom(enumType)) {
            return null;
        }

        final JSONLexer lexer = parser.lexer;
        final int token = lexer.token();
        String value = null;
        if (token == JSONToken.LITERAL_STRING) {
            value = lexer.stringVal();
        } else {
            value = (String) parser.parse();
        }

        return (T) CodeEnumUtil.codeOf(enumType, value);
    }
}
