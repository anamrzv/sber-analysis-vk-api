package sber.social_media_service;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import lombok.Getter;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Getter
public class VkConnectionAgent implements ConnectionAgent {

    private final VkApiClient vk;
    private final UserActor actor;

    @Value("${application.constraints.APP_ID}")
    private String APP_ID;
    @Value("${application.constraints.CLIENT_SECRET}")
    private String CLIENT_SECRET;
    @Value("${application.constraints.REDIRECT_URI}")
    private String REDIRECT_URI;

    @Value("${application.constraints.TOKEN}")
    private String TOKEN;
    @Value("${application.constraints.USER_ID}")
    private Long USER_ID;
    @Value("${application.constraints.CODE}")
    private String CODE;

    public VkConnectionAgent() {
        TransportClient transportClient = new HttpTransportClient();
        vk = new VkApiClient(transportClient);
        actor = new UserActor(USER_ID, TOKEN);
    }

    public void authenticate() throws ClientException, ApiException {
        UserAuthResponse authResponse = vk.oAuth().userAuthorizationCodeFlow(Integer.valueOf(APP_ID), CLIENT_SECRET, REDIRECT_URI, CODE).execute();
        System.out.println("токен " + authResponse.getAccessToken() + "\n");
        System.out.println("юзер айди " + authResponse.getUserId().longValue());
    }

    //TODO: понять как вынимать code из редиректа
    public String getCode() throws URISyntaxException, IOException {
        URIBuilder uriBuilder = new URIBuilder();
        URI uri = uriBuilder.setScheme("https").setHost("oauth.vk.com").setPath("authorize")
                .setParameter("client_id", APP_ID)
                .setParameter("display", "page")
                .setParameter("redirect_uri", REDIRECT_URI)
                .setParameter("scope", "offline,wall,friends,groups,email,phone_number")
                .setParameter("response_type", "code")
                .setParameter("v", "5.131")
                .build();

        //System.out.println("link " + uri.toURL().toString() + '\n');
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = client.execute(request);

        int status = response.getStatusLine().getStatusCode();

        if (status == 200) {
            var entity = response.getEntity();
            return EntityUtils.toString(entity, "UTF-8");
        }
        return null;
    }
}
