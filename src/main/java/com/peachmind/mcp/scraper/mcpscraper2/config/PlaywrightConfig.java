package com.peachmind.mcp.scraper.mcpscraper2.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class PlaywrightConfig {

    private final String saveDirectory;

    public PlaywrightConfig(@Value("${playwright.save.directory}") String saveDirectory) {
        this.saveDirectory = saveDirectory;
    }
}
