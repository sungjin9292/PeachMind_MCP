package com.peachmind.mcp.scraper.mcpscraper2.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScraperCycleConfigReq {
    @JsonProperty(required = true)
    @JsonPropertyDescription("Url for scrapping")
    private String url;

    @JsonProperty(required = true)
    @JsonPropertyDescription("Keyword for scrapping")
    private String keyword;

    @JsonProperty(required = true)
    @JsonPropertyDescription("Cycle for scrapping")
    private int cycle;
}
