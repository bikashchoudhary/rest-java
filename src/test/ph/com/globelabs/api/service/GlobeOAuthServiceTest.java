package ph.com.globelabs.api.service;

import static org.junit.Assert.assertEquals;

import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

public class GlobeOAuthServiceTest {

    private GlobeOAuthService globeOAuthService;

    @Before
    public void setUp() throws Exception {
        globeOAuthService = new GlobeOAuthService();
    }

    @Test
    public void getLoginUrl() throws URISyntaxException {
        String appId = "345SDxcblesfUSDoifw3ljsdfwou35aj";
        String redirectURL = "http://www.callback-url.com/123abc";

        String expectedLoginURL = "http://developer.globelabs.com.ph/dialog/oauth?redirect_url=http%3A%2F%2Fwww.callback-url.com%2F123abc&app_id=345SDxcblesfUSDoifw3ljsdfwou35aj";
        String actualLoginURL = globeOAuthService.getLoginUrl(appId,
                redirectURL);

        assertEquals(expectedLoginURL, actualLoginURL);
    }

}
