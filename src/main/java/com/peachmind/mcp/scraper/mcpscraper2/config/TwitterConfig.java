package com.peachmind.mcp.scraper.mcpscraper2.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class TwitterConfig {
    private final String TWT_API_KEY;
    private final String TWT_API_KEY_SECRET;
    private final String TWT_ACCESS_TOKEN;
    private final String TWT_ACCESS_TOKEN_SECRET;
    private final String TWT_CLIENT_ID;
    private final String TWT_CLIENT_SECRET;
    private final String TWT_CALLBACK_URL;

    public TwitterConfig(@Value("${twitter.api.key}") String TWT_API_KEY,
                        @Value("${twitter.api.key.secret}") String TWT_API_KEY_SECRET,
                        @Value("${twitter.access.token}") String TWT_ACCESS_TOKEN,
                        @Value("${twitter.access.token.secret}") String TWT_ACCESS_TOKEN_SECRET,
                        @Value("${twitter.client.id}") String TWT_CLIENT_ID,
                        @Value("${twitter.client.secret}") String TWT_CLIENT_SECRET,
                        @Value("${twitter.callback.url}") String TWT_CALLBACK_URL
    ) {
        this.TWT_API_KEY = TWT_API_KEY;
        this.TWT_API_KEY_SECRET = TWT_API_KEY_SECRET;
        this.TWT_ACCESS_TOKEN = TWT_ACCESS_TOKEN;
        this.TWT_ACCESS_TOKEN_SECRET = TWT_ACCESS_TOKEN_SECRET;
        this.TWT_CLIENT_ID = TWT_CLIENT_ID;
        this.TWT_CLIENT_SECRET = TWT_CLIENT_SECRET;
        this.TWT_CALLBACK_URL = TWT_CALLBACK_URL;
    }
}
