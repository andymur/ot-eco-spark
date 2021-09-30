package com.andymur.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AggregationValue {

    @JsonProperty
    private int year;
    @JsonProperty
    private int month;
    @JsonProperty
    private long count;


    public AggregationValue(int year, int month, long count)
    {
        this.year = year;
        this.month = month;
        this.count = count;
    }

    public void setYear(int year)
    {
        this.year = year;
    }


    public void setMonth(int month)
    {
        this.month = month;
    }


    public void setCount(int count)
    {
        this.count = count;
    }


    public int getYear()
    {
        return year;
    }


    public int getMonth()
    {
        return month;
    }


    public long getCount()
    {
        return count;
    }


    @Override public String toString()
    {
        return "AggregationValue{" +
            "year=" + year +
            ", month=" + month +
            ", count=" + count +
            '}';
    }
}
