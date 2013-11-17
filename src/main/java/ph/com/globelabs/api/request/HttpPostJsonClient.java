package ph.com.globelabs.api.request;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

import ph.com.globelabs.api.util.UriBuilder;

public class HttpPostJsonClient {

    private HttpClient client;
    private HttpPost request;

    public HttpPostJsonClient() {
        HttpClientBuilder builder = HttpClientBuilder.create();
        this.client = builder.build();
        this.request = new HttpPost();

        this.request.addHeader("Content-Type", "application/json");
        this.request.addHeader("Accept", "application/json");
    }

    public HttpResponse execute(String requestUri)
            throws ClientProtocolException, IOException, URISyntaxException {
        Map<String, String> parameters = new HashMap<String, String>();

        this.request.setURI(new URI(UriBuilder.buildToString(requestUri,
                parameters)));
        System.out.println(request);
        System.out.println(IOUtils.toString(request.getEntity().getContent()));
        return this.client.execute(request);
    }

    public HttpEntity getEntity() {
        return this.request.getEntity();
    }

    public void setEntity(HttpEntity entity) {
        this.request.setEntity(entity);
    }

}
