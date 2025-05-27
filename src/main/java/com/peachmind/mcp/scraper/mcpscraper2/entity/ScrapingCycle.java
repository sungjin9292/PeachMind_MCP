package com.peachmind.mcp.scraper.mcpscraper2.entity;

import jakarta.persistence.*;
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
public class ScrapingCycle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;
    private String keyword;
    private int cycle;
    @Column(name = "last_upload_dt")
    private LocalDate lastUploadDate;
}
