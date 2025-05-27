package com.peachmind.mcp.scraper.mcpscraper2.service;

import com.peachmind.mcp.scraper.mcpscraper2.dto.DefaultResponse;
import com.peachmind.mcp.scraper.mcpscraper2.dto.ScraperCycleConfigReq;
import com.peachmind.mcp.scraper.mcpscraper2.entity.ScrapingCycle;
import com.peachmind.mcp.scraper.mcpscraper2.repository.ScraperCycleRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ScrapingService {

    private final ScraperCycleRepository scraperCycleRepository;
    private final PlaywrightCrawlerService playwrightCrawlerService;

    public ScrapingService(ScraperCycleRepository scraperCycleRepository, PlaywrightCrawlerService playwrightCrawlerService) {
        this.scraperCycleRepository = scraperCycleRepository;
        this.playwrightCrawlerService = playwrightCrawlerService;
    }

    @Tool(name="add_new_scraping_cycle", description = "Add new scraping cycle")
    public DefaultResponse addNewScrapingCycle(ScraperCycleConfigReq req) {
        ScrapingCycle result = null;
        String url = req.getUrl();
        String keyword = req.getKeyword();
        int cycle = req.getCycle();

        //Scraping cycle 설정 객체
        ScrapingCycle scrapingCycle = new ScrapingCycle();
        scrapingCycle.setUrl(url);
        scrapingCycle.setKeyword(keyword);
        scrapingCycle.setCycle(cycle);
        scrapingCycle.setLastUploadDate(LocalDate.now());

        try{
            result = scraperCycleRepository.save(scrapingCycle);
        }catch (Exception e){
            return DefaultResponse.fail("Fail: " + e, result);
        }
        return DefaultResponse.success("Success: Saved Scraping Cycle", result);
    }

    @Tool(name="scraping_data_by_keyword", description = "Scraping data by keyword")
    public DefaultResponse scrappingDataByKeyword(ScraperCycleConfigReq req){
        String keyword = req.getKeyword();
        String searchUrl = "https://search.naver.com/search.naver?query=" + keyword;

        try {
            // 1. 네이버 검색 페이지 HTML 파싱
            Document doc = Jsoup.connect(searchUrl).get();

            // 2. 검색 결과 링크 추출 (일반 웹문서 기준)
            Elements links = doc.select("a[href^=https://blog.naver.com]"); // 블로그 링크 예시
            List<String> extractedLinks = new ArrayList<>();

            for (Element link : links) {
                String href = link.attr("href");
                extractedLinks.add(href);
                if (extractedLinks.size() >= 3) break; // 상위 3개만 추출
            }

            // 3. 추출한 링크를 Playwright로 크롤링해서 저장
            List<String> resultPaths = new ArrayList<>();
            for (String url : extractedLinks) {
                String saved = playwrightCrawlerService.crawlAndSave(url);
                resultPaths.add(saved);
            }

            return DefaultResponse.success("크롤링 완료: 총 " + resultPaths.size() + "건", resultPaths);

        } catch (Exception e) {
            log.error("키워드 크롤링 실패", e);
            return DefaultResponse.fail("크롤링 실패: " + e.getMessage(), null);
        }
        //return DefaultResponse.success("Success: Saved Scraping Cycle", null);
    }
}
