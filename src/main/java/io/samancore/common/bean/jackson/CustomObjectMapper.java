package io.samancore.common.bean.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.util.Date;

import static io.samancore.common.SerializationConstant.DATE_FORMAT_ISO8601;

public class CustomObjectMapper {
    private CustomObjectMapper() {
    }

    public static ObjectMapper changeFeature(ObjectMapper mapper) {
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.setDateFormat(DATE_FORMAT_ISO8601);
        SimpleModule dateModule = new SimpleModule();
        dateModule.addSerializer(Date.class, new DateSerializer());
        mapper.registerModule(dateModule);
        return mapper;
    }
}
