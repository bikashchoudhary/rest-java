package ph.com.globelabs.api.request;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

public class ChargeUserParameters {

    private String subscriberNumber;
    private BigDecimal amount;
    private String referenceCode;

    /**
     * Defines parameters for charging a subscriber.
     * 
     * @param subscriberNumber
     *            Parameter format can be +639xxxx­xxxxxx, 09xxxxxxxx, or
     *            9xxxxxxx.
     * @param referenceCode
     *            Unique reference code to identify the transaction.
     * @param amount
     *            Currency format. Must be only up to two decimal places.
     */
    public ChargeUserParameters(String subscriberNumber, String referenceCode,
            BigDecimal amount) {
        super();
        this.subscriberNumber = subscriberNumber;
        this.referenceCode = referenceCode;
        this.amount = amount.setScale(2, RoundingMode.CEILING);
    }

    public boolean isValid() {
        return subscriberNumber != null && referenceCode != null
                && amount != null;
    }

    /**
     * Transforms the parameters plus the access token into a JSON formatted
     * StringEntity.
     * 
     * @param accessToken
     *            Access token for the given subscriber.
     * @return JSON format of all the parameters along with the access token.
     * @throws UnsupportedEncodingException
     * @throws JSONException
     */
    public StringEntity toJsonStringEntity(String accessToken)
            throws UnsupportedEncodingException, JSONException {
        JSONObject json = new JSONObject();

        json.put("endUserId", this.subscriberNumber);
        json.put("amount", this.amount.toString());
        json.put("referenceCode", this.referenceCode);
        json.put("access_token", accessToken);

        StringEntity stringEntity = new StringEntity(json.toString());
        return stringEntity;
    }

    public String getSubscriberNumber() {
        return subscriberNumber;
    }

    public void setSubscriberNumber(String subscriberNumber) {
        this.subscriberNumber = subscriberNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount.setScale(2, RoundingMode.CEILING);
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    @Override
    public String toString() {
        return "ChargeUserParameters [subscriberNumber=" + subscriberNumber
                + ", amount=" + amount + ", referenceCode=" + referenceCode
                + "]";
    }

}
