package com.peachmind.mcp.scraper.mcpscraper2.repository;

import com.peachmind.mcp.scraper.mcpscraper2.entity.ScrapingCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ScraperCycleRepository extends JpaRepository<ScrapingCycle, Long> {
    List<ScrapingCycle> findByLastUploadDateIsNullOrLastUploadDateBefore(LocalDate targetDate);
    @Query(value = "SELECT * FROM scraping_cycle s WHERE s.last_upload_dt IS NULL OR s.last_upload_dt <= CURRENT_DATE - make_interval(days => s.cycle);", nativeQuery = true)
    List<ScrapingCycle> findDueScrapingCycles();
}
