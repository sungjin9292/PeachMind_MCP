package com.peachmind.mcp.scraper.mcpscraper2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ScrapingData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;
    private String keyword;
    private String title;
    private String content;
    private String summary;
    private LocalDate crawledDate;
}
