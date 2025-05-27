package com.peachmind.mcp.scraper.mcpscraper2.repository;

import com.peachmind.mcp.scraper.mcpscraper2.entity.TweetLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TweetLogRepository extends JpaRepository<TweetLog, Long> {
    boolean existsByTweetId(String tweetId);
}
