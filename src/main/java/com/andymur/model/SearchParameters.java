package com.andymur.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

public class SearchParameters
{
    @JsonProperty
    private Integer fromPeriod;
    @JsonProperty
    private Integer toPeriod;

    @JsonProperty
    private String city;
    @JsonProperty
    private String country;

    @JsonProperty
    private Set<String> tags = new HashSet<>();


    public void setFromPeriod(Integer fromPeriod)
    {
        this.fromPeriod = fromPeriod;
    }


    public void setToPeriod(Integer toPeriod)
    {
        this.toPeriod = toPeriod;
    }


    public void setCity(String city)
    {
        this.city = city;
    }


    public void setCountry(String country)
    {
        this.country = country;
    }


    public void setTags(Set<String> tags)
    {
        this.tags = tags;
    }


    public Integer getFromPeriod()
    {
        return fromPeriod;
    }


    public Integer getToPeriod()
    {
        return toPeriod;
    }


    public String getCity()
    {
        return city;
    }


    public String getCountry()
    {
        return country;
    }


    public Set<String> getTags()
    {
        return tags;
    }
}
