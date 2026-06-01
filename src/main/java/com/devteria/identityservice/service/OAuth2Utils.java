package com.devteria.identityservice.service;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;

import org.apache.http.util.EntityUtils;

import org.json.JSONObject;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OAuth2Utils {

    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    
    @Value("${app.oauth2.client-id:}")
    private String clientId;
    
    @Value("${app.oauth2.client-secret:}")
    private String clientSecret;
    
    @Value("${app.oauth2.refresh-token:}")
    private String refreshToken;

    public String refreshAccessToken() throws IOException {
        // Cấu hình POST request để lấy Access Token mới
        HttpPost post = new HttpPost(TOKEN_URL);
        post.addHeader("Content-Type", "application/x-www-form-urlencoded");

        StringEntity entity = new StringEntity("client_id=" + clientId + "&client_secret=" + clientSecret +
                "&refresh_token=" + refreshToken + "&grant_type=refresh_token");
        post.setEntity(entity);

        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(post)) {

            String jsonResponse = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = new JSONObject(jsonResponse);

            return jsonObject.getString("access_token");
        }
    }
}
