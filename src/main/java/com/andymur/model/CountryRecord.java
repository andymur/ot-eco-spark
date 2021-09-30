package com.andymur.model;

public class CountryRecord
{
    private final int id;
    private final String name;


    public CountryRecord(int id, String name)
    {
        this.id = id;
        this.name = name;
    }


    public int getId()
    {
        return id;
    }


    public String getName()
    {
        return name;
    }


    @Override public String toString()
    {
        return "DictionaryRecord{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }
}
