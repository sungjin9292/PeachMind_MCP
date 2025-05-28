package com.peachmind.mcp.scraper.mcpscraper2.service;

import com.peachmind.mcp.scraper.mcpscraper2.config.NotionConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotionUploadService {

    private final NotionConfig notionConfig;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.notion.com/v1")
            .build();

    public void uploadTweetToNotion(String tweetContent) {
        String token = notionConfig.getApiToken();
        String version = notionConfig.getVersion();
        String dbId = notionConfig.getDatabaseId();

        String jsonPayload = """
        {
          "parent": { "database_id": "%s" },
          "properties": {
            "Tweet": {
              "title": [
                {
                  "text": {
                    "content": "%s"
                  }
                }
              ]
            }
          }
        }
        """.formatted(dbId, tweetContent.replace("\"", "\\\""));

        webClient.post()
                .uri("/pages")
                .header("Authorization", "Bearer " + token)
                .header("Notion-Version", version)
                .header("Content-Type", "application/json")
                .bodyValue(jsonPayload)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> log.info("Notion 응답: " + response))
                .doOnError(error -> log.error("Notion 업로드 실패", error))
                .block();
    }
}
