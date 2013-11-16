package ph.com.globelabs.api.response;

import org.apache.http.HttpResponse;

/**
 * This class holds a response code and response message which represents the
 * HTTP status code and reason phrase of the {@link HttpResponse} it was created
 * from. This class also holds said {@link HttpResponse}.
 * 
 */
public class Response {

    private int responseCode;
    private String responseMessage;

    private HttpResponse httpResponse;

    public Response(HttpResponse httpResponse) {
        this.responseCode = httpResponse.getStatusLine().getStatusCode();
        this.responseMessage = httpResponse.getStatusLine().getReasonPhrase();

        this.httpResponse = httpResponse;
    }

    public Response(int statusCode, String reasonPhrase) {
        this.responseCode = statusCode;
        this.responseMessage = reasonPhrase;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public HttpResponse getHttpResponse() {
        return httpResponse;
    }

    public void setHttpResponse(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    @Override
    public String toString() {
        return "Response [responseCode=" + responseCode + ", responseMessage="
                + responseMessage + ", httpResponse=" + httpResponse + "]";
    }

}
