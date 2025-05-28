package com.peachmind.mcp.scraper.mcpscraper2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TweetLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 트위터 게시글 아이디
    @Column(name = "tweet_id", nullable = false, unique = true)
    private String tweetId;

    // 게시글 내용
    @Column(name = "tweet_content", nullable = false, length = 280)
    private String tweetContent;

    // 작성시간
    @Column(name = "tweet_time", nullable = false)
    private LocalDateTime tweetTime;

    // 작성결과
    @Column(name = "status")
    private String status;

    // 에러 메시지
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    // 게시글 url
    @Column(name = "tweet_url")
    private String tweetUrl;
}