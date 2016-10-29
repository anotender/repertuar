package repertuar.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Map;

public class HttpUtils {

    private static HttpClient client = HttpClientBuilder.create().build();

    public static JSONObject sendGet(String url) throws IOException {
        return sendGet(url, Collections.emptyMap());
    }

    public static JSONObject sendGet(String url, Map<String, String> params) throws IOException {
        HttpGet getRequest = new HttpGet(prepareGetUrl(url, params));
        HttpResponse getResponse = client.execute(getRequest);
        getRequest.releaseConnection();

        BufferedReader rd = new BufferedReader(new InputStreamReader(getResponse.getEntity().getContent(), "UTF-8"));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        return new JSONObject(result.toString());
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