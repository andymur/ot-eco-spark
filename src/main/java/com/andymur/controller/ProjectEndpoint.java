package com.andymur.controller;

import com.andymur.model.Response;
import com.andymur.model.SearchParameters;
import com.andymur.model.StatisticsResponse;
import com.andymur.repository.AggregatorRepository;
import com.andymur.repository.DictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class ProjectEndpoint
{
    @Autowired
    private AggregatorRepository aggregatorRepository;

    @Autowired
    private DictionaryRepository dictionaryRepository;

    @GetMapping("/countries/")
    public Response getCountries()
    {
        return new Response(dictionaryRepository.getCountries());
    }

    @GetMapping("/cities/{countryName}")
    public Response getCities(@PathVariable String countryName) {
        return new Response(dictionaryRepository.getCities(countryName.toLowerCase()));
    }

    @GetMapping("/tags/")
    public Response getTags() {
        return new Response(dictionaryRepository.getTags());
    }

    @PostMapping("/jobstatistics/")
    public StatisticsResponse getJobStatistics(@RequestBody SearchParameters searchParameters) {
        final Integer countryId = dictionaryRepository.getCountryId(searchParameters.getCountry());
        final Integer cityId = dictionaryRepository.getCityId(searchParameters.getCity());

        return new StatisticsResponse(aggregatorRepository.calculate(searchParameters.getFromPeriod(), searchParameters.getToPeriod(),
            cityId, countryId, searchParameters.getTags()));
    }

}
