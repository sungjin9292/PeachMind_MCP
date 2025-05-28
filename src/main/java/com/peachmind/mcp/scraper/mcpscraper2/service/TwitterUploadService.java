package com.peachmind.mcp.scraper.mcpscraper2.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.peachmind.mcp.scraper.mcpscraper2.config.TwitterConfig;
import com.peachmind.mcp.scraper.mcpscraper2.dto.DefaultResponse;
import com.peachmind.mcp.scraper.mcpscraper2.dto.ScrapedResultUploadReq;
import com.peachmind.mcp.scraper.mcpscraper2.entity.TweetLog;
import com.peachmind.mcp.scraper.mcpscraper2.repository.TweetLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class TwitterUploadService {

    private static final String TWITTER_URL_PREFIX = "https://twitter.com/your_username/status/";

    private final TwitterConfig twitterConfig;
    private final TweetLogRepository tweetLogRepository;
    private final NotionUploadService notionUploadService;

    public TwitterUploadService(TwitterConfig twitterConfig, TweetLogRepository tweetLogRepository, NotionUploadService notionUploadService) {
        this.twitterConfig = twitterConfig;
        this.tweetLogRepository = tweetLogRepository;
        this.notionUploadService = notionUploadService;
    }

    @Tool(name="upload_twitter_post", description = "Upload twitter post")
    public DefaultResponse uploadTwitterPost(ScrapedResultUploadReq req){
        final String TWT_API_KEY = twitterConfig.getTWT_API_KEY();
        final String TWT_API_KEY_SECRET = twitterConfig.getTWT_API_KEY_SECRET();
        final String TWT_ACCESS_TOKEN = twitterConfig.getTWT_ACCESS_TOKEN();
        final String TWT_ACCESS_TOKEN_SECRET = twitterConfig.getTWT_ACCESS_TOKEN_SECRET();

        try{

            final String content = req.getContent().replace("\"", "\\\"");
            //twitter free tier max content length is 280 characters
            if(content.length() > 280){
                log.error("**The maximum content length is 280 characters**");
                throw new IOException("The maximum content length is 280 characters");
            }

            // OAuth 1.0a 인증 시작
            OAuth10aService service = new ServiceBuilder(TWT_API_KEY)
                    .apiSecret(TWT_API_KEY_SECRET)
                    .build(TwitterApi.instance());

            //토큰 세팅(twitter api developer page 발급)
            OAuth1AccessToken  tokenResult  = new OAuth1AccessToken(TWT_ACCESS_TOKEN, TWT_ACCESS_TOKEN_SECRET);

            // 줄바꿈 포함한 content를 JSON으로 직렬화
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> jsonMap = new HashMap<>();
            jsonMap.put("text", content);
            String jsonPayload = objectMapper.writeValueAsString(jsonMap);

            OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.twitter.com/2/tweets");
            request.addHeader("Content-Type", "application/json;charset=UTF-8");
            //request.setPayload("{\"text\":\"" + content + "\"}");
            request.setPayload(jsonPayload);

            service.signRequest(tokenResult, request);
            Response response = service.execute(request);

            log.info("Response code: " + response.getCode());
            log.info("Response body: " + response.getBody());

            if(response.getCode() == 200 || response.getCode() == 201){
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                String tweetId = jsonNode.path("data").path("id").asText();

                saveTweetLog(tweetId, content, "SUCCESS", TWITTER_URL_PREFIX + tweetId);
                notionUploadService.uploadTweetToNotion(content);

                return DefaultResponse.success("Success: Posted '" + content + "'", null);
            }else{
                throw new IOException("Failed to post tweet: " + response.getBody());
            }

        }catch (Exception e){
            return DefaultResponse.fail("Fail: Posted " + e, null);
        }
    }

    // 트위터 게시글 정보 DB 저장
    public void saveTweetLog(String tweetId, String content, String status, String url) {
        TweetLog log = TweetLog.builder()
                .tweetId(tweetId)
                .tweetContent(content)
                .tweetTime(LocalDateTime.now())
                .status(status)
                .tweetUrl(url)
                .build();
        tweetLogRepository.save(log);
    }



}
