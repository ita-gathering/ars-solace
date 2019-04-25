package com.approval.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ocean Liang
 * @date 4/25/2019
 */
public class JsonUtils {
    private JsonUtils() {
    }

    public static <T> String objectToJson(T obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsString(obj);
    }

    public static <T> T jsonToObject(String json, Class<T> clazz) throws IOException {
        if (StringUtils.hasText(json)) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, clazz);
        }
        return null;
    }

    public static <T> List<T> jsonToList(String json, Class<T> clazz) throws IOException {
        if (StringUtils.hasText(json)) {
            ObjectMapper mapper = new ObjectMapper();
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
            return mapper.readValue(json, javaType);
        }
        return new ArrayList<>();
    }

    public static String zipJson(String json) {
        return json.replaceAll("\\s*\\n\\s*", "");
    }
}
