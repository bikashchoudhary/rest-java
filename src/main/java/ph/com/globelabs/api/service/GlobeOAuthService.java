package ph.com.globelabs.api.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import ph.com.globelabs.api.exception.ServiceException;
import ph.com.globelabs.api.request.HttpPostClient;
import ph.com.globelabs.api.response.AccessTokenResponse;
import ph.com.globelabs.api.util.UriBuilder;

public class GlobeOAuthService {

    private final static String REQUEST_URI = "http://developer.globelabs.com.ph/dialog/oauth";
    private final static String ACCESS_URI = "http://developer.globelabs.com.ph/oauth/access_token";

    protected HttpPostClient client;

    public GlobeOAuthService() throws ServiceException {
        super();
        try {
            client = new HttpPostClient();
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    /**
     * Builds a login URL from the request URI, a given app ID, and redirect
     * URL. This URL is used for the OAuth first leg.
     * 
     * @param appId
     *            Given app ID by Globe Labs
     * @param redirectURL
     *            Callback URL after user authorization
     * @return Login URL for user subscription and authentication to the app
     * @throws ServiceException
     */
    public String getLoginUrl(String appId, String redirectURL)
            throws ServiceException {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("app_id", appId);
        parameters.put("redirect_url", redirectURL);

        try {
            String loginUrl = UriBuilder.buildToString(REQUEST_URI, parameters);
            return loginUrl;
        } catch (URISyntaxException e) {
            throw new ServiceException(
                    "Given appId or redirectURL is invalid. Login URL cannot be parsed.",
                    e);
        }
    }

    /**
     * Retrieves the access token for a given subscriber provided the subscriber
     * has completed the authentication process through web (via the login URL).
     * 
     * @param appId
     *            Given app ID by Globe Labs
     * @param appSecret
     *            Given app secret by Globe Labs
     * @param code
     *            The code sent by Globe Labs to the callback URL after
     *            subscriber has completed the web authentication process
     * @return Access token
     * @throws ServiceException
     */
    public AccessTokenResponse getAccessToken(String appId, String appSecret,
            String code) throws ServiceException {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("app_id", appId);
        parameters.put("app_secret", appSecret);
        parameters.put("code", code);

        try {
            String requestUri = UriBuilder
                    .buildToString(ACCESS_URI, parameters);
            HttpResponse response = client.execute(requestUri);

            String contentType = response.getEntity().getContentType()
                    .getValue();
            String[] contentTypes = contentType.split(";");
            contentType = contentTypes[0];
            if ("application/json".equals(contentType)) {
                return new AccessTokenResponse(response);
            } else {
                return new AccessTokenResponse(response.getStatusLine()
                        .getStatusCode(), response.getStatusLine()
                        .getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (URISyntaxException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (IOException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (IllegalStateException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (JSONException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public HttpPostClient getClient() {
        return client;
    }

}
