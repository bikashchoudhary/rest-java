package ph.com.globelabs.api.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

import ph.com.globelabs.api.exception.ServiceException;
import ph.com.globelabs.api.response.AccessTokenResponse;
import ph.com.globelabs.api.util.UriBuilder;

public class GlobeOAuthService {

    private static String REQUEST_URI = "http://developer.globelabs.com.ph/dialog/oauth";
    private static String ACCESS_URI = "http://developer.globelabs.com.ph/oauth/access_token";

    public GlobeOAuthService() {
        super();
    }

    public String getLoginUrl(String appId, String redirectURL)
            throws URISyntaxException {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("app_id", appId);
        parameters.put("redirect_url", redirectURL);

        String loginUrl = UriBuilder.buildToString(REQUEST_URI, parameters);
        return loginUrl;
    }

    public AccessTokenResponse getAccessToken(String appId, String appSecret,
            String code) throws ServiceException {
        HttpClientBuilder builder = HttpClientBuilder.create();
        HttpClient client = builder.build();

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("app_id", appId);
        parameters.put("app_secret", appSecret);
        parameters.put("code", code);

        try {
            String url = UriBuilder.buildToString(ACCESS_URI, parameters);
            HttpPost request = new HttpPost(url);

            HttpResponse httpResponse = client.execute(request);
            AccessTokenResponse response = new AccessTokenResponse(httpResponse);

            return response;
        } catch (ClientProtocolException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (URISyntaxException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (IOException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

}
