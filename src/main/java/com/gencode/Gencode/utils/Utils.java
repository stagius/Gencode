package com.gencode.Gencode.utils;

import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class Utils {

    public String formatAppName(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

}
