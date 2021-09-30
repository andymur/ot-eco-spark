package com.andymur.repository;

import com.andymur.model.CityRecord;
import com.andymur.model.CountryRecord;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DictionaryRepository
{
    @Autowired
    private SparkManager sparkManager;

    private Map<String, List<String>> citiesByCountry = new HashMap<>();
    private List<String> countryNames = new ArrayList<>();
    private List<String> tags = new ArrayList<>();

    private Map<String, Integer> cityMapping = new HashMap<>();
    private Map<String, Integer> countryMapping = new HashMap<>();

    @PostConstruct
    public void init() {
        final List<CountryRecord> countries = convertToCountries((Row[] )sparkManager.readTable("lands").collect());
        final List<CityRecord> cities = convertToCities((Row[])sparkManager.readTable("cities").collect());
        tags = convertToTags((Row[])sparkManager.readTable("tags").collect());

        countryNames = countries.stream().map(CountryRecord::getName).collect(Collectors.toList());


        for (CountryRecord countryRecord: countries) {
            countryMapping.put(countryRecord.getName().toLowerCase(), countryRecord.getId());
        }

        final Map<Integer, String> countryDirectMapping = new HashMap<>();

        for (CountryRecord countryRecord: countries) {
            countryDirectMapping.put(countryRecord.getId(), countryRecord.getName().toLowerCase());
        }

        for (CityRecord cityRecord: cities) {
            cityMapping.put(cityRecord.getName().toLowerCase(), cityRecord.getId());

            final String countryName = countryDirectMapping.get(cityRecord.getCountryId());
            List<String> landCities = citiesByCountry.getOrDefault(countryName, new ArrayList<>());
            landCities.add(cityRecord.getName());
            citiesByCountry.put(countryName, landCities);
        }
    }

    private static List<CityRecord> convertToCities(Row[] rows) {
        return Arrays.stream(rows).map(r -> new CityRecord(r.getInt(0), r.getInt(2), r.getString(1))).collect(Collectors.toList());
    }

    private static List<CountryRecord> convertToCountries(Row[] rows) {
        return Arrays.stream(rows).map(r -> new CountryRecord(r.getInt(0), r.getString(1))).collect(Collectors.toList());
    }

    private static List<String> convertToTags(Row[] rows) {
        return Arrays.stream(rows).map(r -> r.getString(1)).collect(Collectors.toList());
    }

    public List<String> getCountries()
    {
        return countryNames;
    }


    public List<String> getCities(String countryName)
    {
        if (countryName == null) {
            return Collections.emptyList();
        }

        return citiesByCountry.getOrDefault(countryName, Collections.emptyList());
    }


    public List<String> getTags()
    {
        return tags;
    }


    public Integer getCountryId(String countryName)
    {
        if (countryName == null) {
            return null;
        }
        return countryMapping.get(countryName.toLowerCase());
    }


    public Integer getCityId(String cityName)
    {
        if (cityName == null) {
            return null;
        }
        return cityMapping.get(cityName.toLowerCase());
    }
}
