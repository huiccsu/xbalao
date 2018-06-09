package com.xbalao.interceptor.web;

import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
/***
 * 
 * Copyright (c) 2017
 * @ClassName:     ObjectMappingCustomer.java
 * @Description:   对json格式中null转""操作
 * 
 * @author:        hui
 * @version:       V1.0  
 * @Date:           2017年6月28日 上午10:47:57
 */
public class ObjectMappingCustomer extends com.fasterxml.jackson.databind.ObjectMapper {

    /**
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
     */
    private static final long serialVersionUID = 1L;

    public ObjectMappingCustomer() {
        super();

        // 允许单引号
        // this.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES,
        // true);
        // 字段和值都加引号
        // this.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES,
        // true);
        // 数字也加引号
        // this.configure(com.fasterxml.jackson.core.JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS,
        // true);
        // this.configure(com.fasterxml.jackson.core.JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS,
        // true);
        // 空值处理为空串
        this.getSerializerProvider().setNullValueSerializer(
                new com.fasterxml.jackson.databind.JsonSerializer<Object>() {

                    @Override
                    public void serialize(Object arg0, com.fasterxml.jackson.core.JsonGenerator arg1,
                            SerializerProvider arg2) throws IOException,
                            com.fasterxml.jackson.core.JsonProcessingException {
                        arg1.writeString("");
                    }
                });
    }
}