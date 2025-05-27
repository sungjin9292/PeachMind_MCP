package com.peachmind.mcp.scraper.mcpscraper2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DefaultResponse {
    private String responseMessage;
    private boolean success;
    private Object data;
    private LocalDateTime localDateTime;

    public static DefaultResponse success(String message, Object data){
        return DefaultResponse.builder()
                .responseMessage(message)
                .success(true)
                .data(data)
                .localDateTime(LocalDateTime.now())
                .build();
    }

    public static DefaultResponse fail(String message, Object data){
        return DefaultResponse.builder()
                .responseMessage(message)
                .success(false)
                .data(data)
                .localDateTime(LocalDateTime.now())
                .build();
    }
}
