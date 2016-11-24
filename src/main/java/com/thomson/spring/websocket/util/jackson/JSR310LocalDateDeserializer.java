package com.thomson.spring.websocket.util.jackson;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;


/**
 * Created on 8/3/2016.
 */
public class JSR310LocalDateDeserializer extends JsonDeserializer<LocalDate>{
    public static final JSR310LocalDateDeserializer INSTANCE = new JSR310LocalDateDeserializer();

    private JSR310LocalDateDeserializer() {
    }

    private static final DateTimeFormatter ISO_DATE_OPTIONAL_TIME;

    static {
        ISO_DATE_OPTIONAL_TIME = new DateTimeFormatterBuilder()
                .append(DateTimeFormatter.ISO_LOCAL_DATE)
                .optionalStart()
                .appendLiteral('T')
                .append(DateTimeFormatter.ISO_OFFSET_TIME)
                .toFormatter();
    }

    @Override
    public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        switch (parser.getCurrentToken()) {
            case START_ARRAY:
                if (parser.nextToken() == JsonToken.END_ARRAY) {
                    return null;
                }
                int year = parser.getIntValue();

                parser.nextToken();
                int month = parser.getIntValue();

                parser.nextToken();
                int day = parser.getIntValue();

                if (parser.nextToken() != JsonToken.END_ARRAY) {
                    throw context.wrongTokenException(parser, JsonToken.END_ARRAY, "Expected array to end.");
                }
                return LocalDate.of(year, month, day);

            case VALUE_STRING:
                String string = parser.getText().trim();
                if (string.length() == 0) {
                    return null;
                }
                return LocalDate.parse(string, ISO_DATE_OPTIONAL_TIME);
        }
        throw context.wrongTokenException(parser, JsonToken.START_ARRAY, "Expected array or string.");
    }
}
