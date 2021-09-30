package com.andymur.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Response
{
    @JsonProperty
    private List<String> payload;


    public Response(List<String> payload)
    {
        this.payload = payload;
    }


    public List<String> getPayload()
    {
        return payload;
    }


    public void setPayload(List<String> payload)
    {
        this.payload = payload;
    }
}
