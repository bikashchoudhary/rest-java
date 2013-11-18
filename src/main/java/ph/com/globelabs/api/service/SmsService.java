package ph.com.globelabs.api.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import ph.com.globelabs.api.exception.ParameterRequiredException;
import ph.com.globelabs.api.exception.ServiceException;
import ph.com.globelabs.api.request.HttpPostClient;
import ph.com.globelabs.api.response.SendSmsResponse;
import ph.com.globelabs.api.util.UriBuilder;

public class SmsService {

    private final static String SEND_SMS_URI = "http://devapi.globelabs.com.ph/smsmessaging/v1/outbound/{shortCode}/requests";

    protected HttpPostClient client;

    public SmsService() throws ServiceException {
        super();
        try {
            client = new HttpPostClient();
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    /**
     * Sends an SMS to a subscriber who has already completed the authorization
     * process. Recipient number and access token (as obtained from
     * GlobeOAuthService) must match.
     * 
     * @param recipientNumber
     *            The nine-digit subscriber number with format 9xxxxxxxxx
     * @param message
     *            Message must be 160 characters or less.
     * @param accessToken
     *            Access token for the given subscriber.
     * @param shortCode
     *            The last 4 digits of your app short code (2158XXXX).
     * @return See {@link SendSmsResponse}
     * @throws ParameterRequiredException
     * @throws ServiceException
     */
    public SendSmsResponse sendSms(String recipientNumber, String message,
            String accessToken, String shortCode)
            throws ParameterRequiredException, ServiceException {
        try {
            if (message == null || message.length() > 160
                    || recipientNumber == null) {
                String exceptionMessage = "";
                if (message == null) {
                    exceptionMessage += "Message must not be null. ";
                } else if (message.length() > 160) {
                    exceptionMessage += "Message must not exceed 160 characters. ";
                }
                if (recipientNumber == null) {
                    exceptionMessage += "Address must not be null. ";
                }
                throw new ParameterRequiredException(exceptionMessage);
            }

            String requestUri = buildRequestURI(shortCode, accessToken);
            client.setEntity(buildJsonStringEntity(recipientNumber, message));
            HttpResponse response = client.execute(requestUri);

            String contentType = response.getEntity().getContentType()
                    .getValue();
            String[] contentTypes = contentType.split(";");
            contentType = contentTypes[0];
            if ("application/json".equals(contentType)) {
                return new SendSmsResponse(response);
            } else {
                return new SendSmsResponse(response.getStatusLine()
                        .getStatusCode(), response.getStatusLine()
                        .getReasonPhrase());
            }
        } catch (IllegalStateException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (JSONException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (IOException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (URISyntaxException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private String buildRequestURI(String shortCode, String accessToken)
            throws URISyntaxException, UnsupportedEncodingException {
        Map<String, String> parameters = new HashMap<String, String>();

        String uri = SEND_SMS_URI;
        uri = uri.replace("{shortCode}",
                URLEncoder.encode(shortCode, "ISO-8859-1"));

        parameters.put("access_token", accessToken);

        return UriBuilder.buildToString(uri, parameters);
    }

    private StringEntity buildJsonStringEntity(String recipientNumber,
            String message) throws UnsupportedEncodingException, JSONException {
        JSONObject requestContent = new JSONObject();
        requestContent.put("address", recipientNumber);
        requestContent.put("message", message);

        StringEntity stringEntity = new StringEntity(requestContent.toString());
        return stringEntity;
    }

    public HttpPostClient getClient() {
        return client;
    }

}
