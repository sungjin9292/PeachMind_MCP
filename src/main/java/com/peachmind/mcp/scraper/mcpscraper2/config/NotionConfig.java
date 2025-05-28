package com.peachmind.mcp.scraper.mcpscraper2.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "notion")
@Getter
@Setter
public class NotionConfig {
    private String apiToken;
    private String databaseId;
    private String version;
}
