package com.peachmind.mcp.scraper.mcpscraper2.service;

import com.peachmind.mcp.scraper.mcpscraper2.dto.ScrapedResultUploadReq;
import com.peachmind.mcp.scraper.mcpscraper2.entity.ScrapingCycle;
import com.peachmind.mcp.scraper.mcpscraper2.repository.ScraperCycleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TweetSchedulerService {

    private final TwitterUploadService twitterUploadService;
    private final ScraperCycleRepository scraperCycleRepository;

    public TweetSchedulerService(TwitterUploadService twitterUploadService
                                ,ScraperCycleRepository scraperCycleRepository ) {
        this.twitterUploadService = twitterUploadService;
        this.scraperCycleRepository = scraperCycleRepository;
    }

    //TODO: 테스트중 cycle 에 맞춰서 크롤링 업로드 자동화 로직 진행 예정
    @Scheduled(fixedRate = 60000)
    public void scheduleTweet() {
        List<ScrapingCycle> list = scraperCycleRepository.findDueScrapingCycles();
            for(ScrapingCycle cycle : list){
                try{
                    //크롤링 정보 세팅
                    String url = cycle.getUrl();
                    String keyword = cycle.getKeyword();
                    //크롤링 서비스 호출

                    //upload 처리
//            twitterUploadService.uploadTwitterPost(req);
                    //uploadDate 갱신
                    cycle.setLastUploadDate(java.time.LocalDate.now());
                    scraperCycleRepository.save(cycle);
                } catch (Exception e) {
                    log.error("**Fail to upload tweet: " + e + "**");
                }
            }
    }
}
