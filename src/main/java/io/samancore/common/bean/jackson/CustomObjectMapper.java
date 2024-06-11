package io.samancore.common.bean.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomObjectMapper {

    private static final SimpleDateFormat DATE_FORMAT_FORMIO = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");

    private CustomObjectMapper() {
    }

    public static ObjectMapper changeFeature(ObjectMapper mapper) {
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.setDateFormat(DATE_FORMAT_FORMIO);
        return mapper;
    }
}
