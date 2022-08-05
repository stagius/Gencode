package com.gencode.Gencode.utils;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Data
public class Utils {

    public String formatFistLetterCapital(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public Map<String, String> formatAttributes(String attributes) {
        // ex: message=String,age=int etc.
        Map<String, String> result = new HashMap<>();
        List<String> tempList = Arrays.asList(attributes.replaceAll(" ", "").split(","));

        tempList.forEach(value -> {
            result.put(value.split("=")[0], value.split("=")[1]);
        });
        return result;
    }

}
