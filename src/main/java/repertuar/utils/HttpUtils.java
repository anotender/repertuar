package repertuar.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpUtils {

    private static final HttpClient client = HttpClientBuilder.create().build();

    public static String sendGet(String url) throws IOException {
        return sendGet(url, Collections.emptyMap());
    }

    public static String sendGet(String url, Map<String, String> params) throws IOException {
        HttpGet getRequest = new HttpGet(prepareGetUrl(url, params));
        HttpResponse getResponse = client.execute(getRequest);

        String content = new BufferedReader(new InputStreamReader(getResponse.getEntity().getContent(), "UTF-8"))
                .lines()
                .collect(Collectors.joining("\n"));

        EntityUtils.consume(getResponse.getEntity());

        return content;
    }

    private static String prepareGetUrl(String url, Map<String, String> params) {
        StringBuilder builder = new StringBuilder(url);
        builder.append("?");

        params.forEach((k, v) -> builder
                .append(k)
                .append("=")
                .append(v)
                .append("&")
        );

        return builder.toString();
    }

}
