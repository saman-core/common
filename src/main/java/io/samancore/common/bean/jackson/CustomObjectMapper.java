package io.samancore.common.bean.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.util.Date;

public class CustomObjectMapper {

    private CustomObjectMapper() {
    }

    public static ObjectMapper changeFeature(ObjectMapper mapper) {
        SimpleModule dateModule = new SimpleModule();
        dateModule.addDeserializer(Date.class, new DateMultiFormatDeserializer());
        dateModule.addSerializer(Date.class, new DateSerializer());

        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.registerModule(dateModule);
        return mapper;
    }
}
