package com.peachmind.mcp.scraper.mcpscraper2.service;

import com.microsoft.playwright.*;
import com.peachmind.mcp.scraper.mcpscraper2.config.PlaywrightConfig;
import com.peachmind.mcp.scraper.mcpscraper2.config.TwitterConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

@Slf4j
@Service
public class PlaywrightCrawlerService {

    private final PlaywrightConfig playwrightConfig;

    public PlaywrightCrawlerService(PlaywrightConfig playwrightConfig) {
        this.playwrightConfig = playwrightConfig;
    }

    @Tool(name = "crawl_and_save_page", description = "crawl_and_save_page")
    public String crawlAndSave(String url) {
        log.info("크롤링 시작: {}", url);

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(true)
            );
            Page page = browser.newPage();
            page.navigate(url);

            String htmlContent = page.content();
            String title = page.title();

            // 파일명 안전하게 변환
            String safeFilename = title.replaceAll("[^a-zA-Z0-9\\-_]", "_") + ".txt";
            //String fullPath = Paths.get(SAVE_DIRECTORY, safeFilename).toString();
            String fullPath = Paths.get(playwrightConfig.getSaveDirectory(), safeFilename).toString();

            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(fullPath), StandardCharsets.UTF_8)
            )) {
                writer.write(htmlContent);
            }

            log.info("크롤링 완료: {} -> {}", title, fullPath);
            browser.close();
            return "저장 완료: " + fullPath;

        } catch (IOException e) {
            log.error("파일 저장 실패", e);
            return "파일 저장 실패: " + e.getMessage();
        } catch (Exception e) {
            log.error("크롤링 실패", e);
            return "크롤링 실패: " + e.getMessage();
        }
    }
}
