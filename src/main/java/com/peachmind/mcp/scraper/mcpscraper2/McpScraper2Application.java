package com.peachmind.mcp.scraper.mcpscraper2;

import com.peachmind.mcp.scraper.mcpscraper2.service.PlaywrightCrawlerService;
import com.peachmind.mcp.scraper.mcpscraper2.service.ScrapingService;
import com.peachmind.mcp.scraper.mcpscraper2.service.TwitterUploadService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class McpScraper2Application {

    public static void main(String[] args) {
        SpringApplication.run(McpScraper2Application.class, args);
    }

    @Bean
    public ToolCallbackProvider toolCallbackProvider(ScrapingService scrapingService, TwitterUploadService twitterUploadService, PlaywrightCrawlerService playwrightCrawlerService) {
        return MethodToolCallbackProvider.builder().toolObjects(scrapingService, twitterUploadService, playwrightCrawlerService).build();
    }

}
