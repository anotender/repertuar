package repertuar.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpUtils {

    private static final HttpClient HTTP_CLIENT = HttpClientBuilder.create().build();

    public static String sendGet(String url) throws IOException {
        HttpGet getRequest = new HttpGet(url);
        HttpResponse getResponse = HTTP_CLIENT.execute(getRequest);
        return EntityUtils.toString(getResponse.getEntity());
    }

}
