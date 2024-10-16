package io.samancore.common.bean.jaxrs;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ext.ParamConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import static io.samancore.common.SerializationConstant.DATE_FORMAT_ISO8601;
import static io.samancore.common.SerializationConstant.DESERIALIZER_DATE_FORMATTERS;

public class DateParameterConverter implements ParamConverter<Date> {

    @Override
    public Date fromString(String string) {
        for (SimpleDateFormat formatter : DESERIALIZER_DATE_FORMATTERS) {
            try {
                return formatter.parse(string);
            } catch (ParseException ignore) {
                //ignore
            }
        }
        throw new BadRequestException("Unparseable date: \"" + string + "\". Supported formats: " +
                Arrays.stream(DESERIALIZER_DATE_FORMATTERS).map(SimpleDateFormat::toPattern).collect(Collectors.joining("; ")));
    }

    @Override
    public String toString(Date t) {
        return DATE_FORMAT_ISO8601.format(t);
    }
}
