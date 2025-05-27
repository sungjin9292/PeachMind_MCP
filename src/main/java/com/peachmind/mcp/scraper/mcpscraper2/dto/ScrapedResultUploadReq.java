package com.peachmind.mcp.scraper.mcpscraper2.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScrapedResultUploadReq {
    @JsonProperty(required = true)
    @JsonPropertyDescription("Title to upload")
    private String title;

    @JsonProperty(required = true)
    @JsonPropertyDescription("Content to upload")
    private String content;
}
