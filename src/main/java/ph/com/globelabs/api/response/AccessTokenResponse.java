package ph.com.globelabs.api.response;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import ph.com.globelabs.api.exception.ServiceException;

/**
 * This object is created from the expected response of the server. Obtainable
 * information include the following: accessToken, subscriberNumber, and error
 * (if any).
 * 
 * This response also has a responseCode, responseMessage, and holds the raw
 * HttpResponse. See {@link Response}.
 */
public class AccessTokenResponse extends Response {

    private String accessToken;
    private String subscriberNumber;

    public AccessTokenResponse(HttpResponse httpResponse)
            throws ServiceException {
        super(httpResponse);

        try {
            JSONObject responseContent = new JSONObject(
                    IOUtils.toString(httpResponse.getEntity().getContent()));

            if (responseContent.has("access_token")
                    && responseContent.has("subscriber_number")) {
                this.accessToken = responseContent.getString("access_token");
                this.subscriberNumber = responseContent
                        .getString("subscriber_number");
            } else if (responseContent.has("error")) {
                throw new ServiceException(responseContent.getString("error"));
            } else {
                throw new ServiceException(
                        "Cannot parse response. Original Response Content: "
                                + IOUtils.toString(httpResponse.getEntity()
                                        .getContent()));
            }
        } catch (JSONException e) {
            throw new ServiceException("Cannot parse response.");
        } catch (IllegalStateException e) {
            throw new ServiceException("Cannot parse response.");
        } catch (IOException e) {
            throw new ServiceException("Cannot parse response.");
        }
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getSubscriberNumber() {
        return subscriberNumber;
    }

    @Override
    public String toString() {
        return "AccessTokenResponse [accessToken=" + accessToken
                + ", subscriberNumber=" + subscriberNumber + "]";
    }

}
