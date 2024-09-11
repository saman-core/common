package io.samancore.common.bean.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.SimpleDateFormat;

import static io.samancore.common.SerializationConstant.DATE_PATTERN;

public class CustomObjectMapper {
    private static final SimpleDateFormat DATE_FORMAT_FORMIO = new SimpleDateFormat(DATE_PATTERN);

    private CustomObjectMapper() {
    }

    public static ObjectMapper changeFeature(ObjectMapper mapper) {
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.setDateFormat(DATE_FORMAT_FORMIO);
        return mapper;
    }
}
