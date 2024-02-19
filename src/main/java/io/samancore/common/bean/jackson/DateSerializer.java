package io.samancore.common.bean.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.samancore.common.SerializationConstant;

import java.io.IOException;
import java.util.Date;

public class DateSerializer extends StdSerializer<Date> {

    public DateSerializer() {
        this(null);
    }

    public DateSerializer(Class t) {
        super(t);
    }

    @Override
    public void serialize(Date value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(SerializationConstant.DATE_FORMAT_ISO8601.format(value));
    }
}
