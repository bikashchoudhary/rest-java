package ph.com.globelabs.api.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONException;

import ph.com.globelabs.api.exception.ParameterRequiredException;
import ph.com.globelabs.api.exception.ServiceException;
import ph.com.globelabs.api.request.ChargeUserParameters;
import ph.com.globelabs.api.request.HttpPostClient;
import ph.com.globelabs.api.response.ChargeUserResponse;
import ph.com.globelabs.api.util.UriBuilder;

public class PaymentService {

    private final static String CHARGE_USER_URI = "http://devapi.globelabs.com.ph/payment/v1/transactions/amount";

    protected HttpPostClient client;

    public PaymentService() throws ServiceException {
        super();
        try {
            client = new HttpPostClient();
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    /**
     * Charges a subscriber who has already completed the authorization process.
     * subscriberNumber (as defined in {@link ChargeUserParameters} and
     * accessToken must match.
     * 
     * @param parameters
     *            subscriberNumber, amount, and reference code. See
     *            {@link ChargeUserParameters}.
     * @param accessToken
     *            Access token for the given subscriber.
     * @return See {@link ChargeUserResponse}
     * @throws ServiceException
     * @throws ParameterRequiredException
     */
    public ChargeUserResponse chargeUser(ChargeUserParameters parameters,
            String accessToken) throws ServiceException,
            ParameterRequiredException {
        if (!parameters.isValid()) {
            throw new ParameterRequiredException("Parameters invalid");
        }

        try {
            String requestUri = buildRequestURI(accessToken);

            client.setEntity(parameters.toJsonStringEntity(accessToken));
            HttpResponse response = client.execute(requestUri);

            String contentType = response.getEntity().getContentType()
                    .getValue();
            String[] contentTypes = contentType.split(";");
            contentType = contentTypes[0];
            if ("application/json".equals(contentType)) {
                return new ChargeUserResponse(response);
            } else {
                return new ChargeUserResponse(response.getStatusLine()
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
            throw new ServiceException("Redirect URL invalid", e);
        }
    }

    private String buildRequestURI(String accessToken)
            throws URISyntaxException, UnsupportedEncodingException {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("access_token", accessToken);

        return UriBuilder.buildToString(CHARGE_USER_URI, parameters);
    }

    public HttpPostClient getClient() {
        return client;
    }

}
