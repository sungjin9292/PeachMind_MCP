package com.peachmind.mcp.scraper.mcpscraper2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peachmind.mcp.scraper.mcpscraper2.config.NotionConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotionUploadService {

    private final NotionConfig notionConfig;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.notion.com/v1")
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void uploadTweetToNotion(String tweetContent) {
        String token = notionConfig.getApiToken();
        String version = notionConfig.getVersion();
        String dbId = notionConfig.getDatabaseId();

        log.info("[Notion] API Token: {}", token);
        log.info("[Notion] Database ID: {}", dbId);
        log.info("[Notion] Version: {}", version);
        log.info("[Notion] 업로드할 내용: {}", tweetContent);

        String title = tweetContent.length() > 30
                ? tweetContent.substring(0, 30) + "..."
                : tweetContent;

        try {
            // JSON 구조를 안전하게 Map으로 구성
            Map<String, Object> payload = Map.of(
                    "parent", Map.of("database_id", dbId),
                    "properties", Map.of(
                            "Tweet", Map.of(
                                    "title", List.of(Map.of(
                                            "text", Map.of("content", title)
                                    ))
                            )
                    ),
                    "children", List.of(
                            Map.of(
                                    "object", "block",
                                    "type", "paragraph",
                                    "paragraph", Map.of(
                                            "rich_text", List.of(Map.of(
                                                    "type", "text",
                                                    "text", Map.of("content", tweetContent)
                                            ))
                                    )
                            )
                    )
            );

            String jsonPayload = objectMapper.writeValueAsString(payload);

            String response = webClient.post()
                    .uri("/pages")
                    .header("Authorization", "Bearer " + token)
                    .header("Notion-Version", version)
                    .header("Content-Type", "application/json")
                    .bodyValue(jsonPayload)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("[Notion] 응답 성공: {}", response);
        } catch (Exception e) {
            log.error("[Notion] 업로드 실패", e);
        }
    }
}