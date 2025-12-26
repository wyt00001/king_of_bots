package com.kob.backend.service.impl.user.account.acwing.utils;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;          // 5.x 的包
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class HttpClientUtil {

    public static String get(String url, List<NameValuePair> params) {
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            uriBuilder.addParameters(params);   // 5.x 推荐 addParameters

            try (CloseableHttpClient client = HttpClients.createDefault()) {
                HttpGet httpGet = new HttpGet(uriBuilder.build());
                return client.execute(httpGet, response -> {
                    HttpEntity entity = response.getEntity();
                    return entity == null ? null : EntityUtils.toString(entity);
                });
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}