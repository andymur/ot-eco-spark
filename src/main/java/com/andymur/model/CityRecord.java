package com.andymur.model;

public class CityRecord
{
    private final int id;
    private final int countryId;
    private final String name;


    public CityRecord(int id, int countryId, String name)
    {
        this.id = id;
        this.countryId = countryId;
        this.name = name;
    }


    public int getId()
    {
        return id;
    }


    public int getCountryId()
    {
        return countryId;
    }


    public String getName()
    {
        return name;
    }


    @Override public String toString()
    {
        return "CityRecord{" +
            "id=" + id +
            ", countryId=" + countryId +
            ", name='" + name + '\'' +
            '}';
    }
}
