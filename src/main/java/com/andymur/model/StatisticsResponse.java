package com.andymur.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class StatisticsResponse
{
    @JsonProperty
    private List<AggregationValue> payload;


    public StatisticsResponse(List<AggregationValue> payload)
    {
        this.payload = payload;
    }


    public List<AggregationValue> getPayload()
    {
        return payload;
    }


    public void setPayload(List<AggregationValue> payload)
    {
        this.payload = payload;
    }
}
