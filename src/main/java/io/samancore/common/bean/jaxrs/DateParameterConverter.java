package io.samancore.common.bean.jaxrs;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ext.ParamConverter;

import java.text.ParseException;
import java.util.Date;

import static io.samancore.common.SerializationConstant.DATE_FORMAT_ISO8601;

public class DateParameterConverter implements ParamConverter<Date> {

    @Override
    public Date fromString(String string) {
        try {
            return DATE_FORMAT_ISO8601.parse(string);
        } catch (ParseException ex) {
            throw new BadRequestException(ex);
        }
    }

    @Override
    public String toString(Date t) {
        return DATE_FORMAT_ISO8601.format(t);
    }
}
