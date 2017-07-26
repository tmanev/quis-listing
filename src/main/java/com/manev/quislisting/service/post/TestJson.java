package com.manev.quislisting.service.post;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trajche Manev on 7/5/2017.
 */
public class TestJson {

    public static void main(String[] args) throws IOException {
        String text = "[3,2]";
        ObjectMapper objectMapper = new ObjectMapper();
        Long[] s = objectMapper.readValue(text, Long[].class);
        for (Long aLong : s) {
            System.out.println(aLong);
        }

        List<Long> elk = new ArrayList<>();

        System.out.println(objectMapper.writeValueAsString(elk));

    }

}
