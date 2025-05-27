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

    //
    @Column(name = "tweet_content", nullable = false, length = 280)
    private String tweetContent;

    @Column(name = "tweet_time", nullable = false)
    private LocalDateTime tweetTime;

    // 선택: 실패 시 에러 메시지 등 확장 가능
    @Column(name = "status")
    private String status;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "tweet_url")
    private String tweetUrl;
}