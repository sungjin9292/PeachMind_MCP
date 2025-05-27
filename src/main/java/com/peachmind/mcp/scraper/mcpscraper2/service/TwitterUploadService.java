package com.peachmind.mcp.scraper.mcpscraper2.service;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class TwitterUploadService {

    TwitterConfig twitterConfig;

    public TwitterUploadService(TwitterConfig twitterConfig) {
        this.twitterConfig = twitterConfig;
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

            OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.twitter.com/2/tweets");
            request.addHeader("Content-Type", "application/json;charset=UTF-8");
            request.setPayload("{\"text\":\"" + content + "\"}");

            service.signRequest(tokenResult, request);
            Response response = service.execute(request);

            //response값 검증
            if(response.getCode() == 200){
                return DefaultResponse.success("Success: Posted '" + content + "'", null);
            }else{
                throw new IOException("Failed to post tweet: " + response.getBody());
            }

        }catch (Exception e){
            return DefaultResponse.fail("Fail: Posted " + e, null);
        }
    }
}
