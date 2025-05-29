package com.peachmind.mcp.scraper.mcpscraper2.config;

import jakarta.annotation.PostConstruct;
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

    @PostConstruct
    public void init() {
        System.out.println("NotionConfig Loaded:");
        System.out.println("  token = " + apiToken);
        System.out.println("  dbId = " + databaseId);
        System.out.println("  version = " + version);
    }

}
