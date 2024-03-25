package com.hitachi.coe.fullstack.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hitachi.coe.fullstack.constant.ErrorConstant;
import com.hitachi.coe.fullstack.model.ExcelConfigModel;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Slf4j
public class JsonUtils {

    private JsonUtils(){}

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * @param object any object to convert to json string
     * @return Json string from an object
     * @throws JsonProcessingException error when converting object to json
     * @author tminhto
     */
    public static String toJson(Object object) throws JsonProcessingException {
        // Use the ObjectMapper instance to convert the object to a json string
        return OBJECT_MAPPER.writeValueAsString(object);
    }

    /**
     * @param jsonString json string from resource
     * @param clazz the class of the object to convert from json string
     * @return An object of the specified class from the json string
     * @throws JsonProcessingException error when parsing json to POJO
     * @author tminhto
     */
    public static <T> T fromJson(String jsonString, TypeReference<T> typeReference) throws JsonProcessingException {
        // Use the ObjectMapper instance to convert the json string to an object of the specified class
        return OBJECT_MAPPER.readValue(jsonString, typeReference);
    }

    /**
     * @param jsonString json string from resource
     * @return Json Config Model for the Excel Templates
     * @throws JsonProcessingException error when parsing json to POJO
     */
    public static ExcelConfigModel convertJsonToPojo(String jsonString) throws JsonProcessingException {
        ExcelConfigModel excelConfigModel;
        excelConfigModel = OBJECT_MAPPER.readValue(jsonString, ExcelConfigModel.class);
        return excelConfigModel;
    }

    /**
     * @param filePath file location
     * @return Json string from a file
     * @author Lam
     */
    public static String readFileAsString(String filePath) {
        InputStream inputStream;
        String result = "";
        try {
            inputStream = JsonUtils.class.getResourceAsStream(filePath);
            result = new String(Objects.requireNonNull(inputStream).readAllBytes());
        } catch ( IOException ex ) {
            log.error(ErrorConstant.MESSAGE_FILE_NOT_FOUND);
        }
        return result;
    }
}
