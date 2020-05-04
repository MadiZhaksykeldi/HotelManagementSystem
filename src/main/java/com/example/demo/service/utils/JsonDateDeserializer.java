package com.example.demo.service.utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JsonDateDeserializer extends JsonDeserializer<Date> {
    Logger logger = LoggerFactory.getLogger(JsonDateDeserializer.class);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext arg1) throws IOException {
        String text = jsonParser.getText();
        try {
            return dateFormat.parse(text);
        } catch (ParseException exception) {
            logger.error("Error : ", exception);
            return null;
        }
    }
}