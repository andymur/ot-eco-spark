package com.andymur;

import com.andymur.model.AggregationValue;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SerializatorRunner
{

    public static void main(String[] args)
    {
        List<AggregationValue> values = new ArrayList<>(
            Arrays.asList(new AggregationValue(2020, 1, 30L),
            new AggregationValue(2020, 2, 20))
        );

        System.out.println(translateDTOToJsonString(values));
    }

    public static <T> String translateDTOToJsonString(T dto)
    {
        try
        {
            return new ObjectMapper().writeValueAsString(dto);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }


    public static <T> T translateJsonStringToDTO(String json, Class<T> clazz)
    {
        try
        {
            return new ObjectMapper().readValue(json, clazz);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

}
