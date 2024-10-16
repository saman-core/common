package io.samancore.common.bean.jackson;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import static io.samancore.common.SerializationConstant.DESERIALIZER_DATE_FORMATTERS;

public class DateMultiFormatDeserializer extends StdDeserializer<Date> {

    public DateMultiFormatDeserializer() {
        this(null);
    }

    public DateMultiFormatDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Date deserialize(JsonParser jsonparser, DeserializationContext context) throws IOException {
        String date = jsonparser.getText();

        for (SimpleDateFormat formatter : DESERIALIZER_DATE_FORMATTERS) {
            try {
                return formatter.parse(date);
            } catch (ParseException ignore) {
                //ignore
            }
        }
        throw new JsonParseException(jsonparser, "Unparseable date: \"" + date + "\". Supported formats: " +
                Arrays.stream(DESERIALIZER_DATE_FORMATTERS).map(SimpleDateFormat::toPattern).collect(Collectors.joining("; ")));
    }
}
