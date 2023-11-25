package sber.social_media_service;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;

@Getter
public class VkConnectionAgent implements ConnectionAgent {

    private final VkApiClient vk;

    private final String TOKEN;

    public VkConnectionAgent() {
        TransportClient transportClient = new HttpTransportClient();
        vk = new VkApiClient(transportClient);
        Dotenv dotenv = Dotenv.load();
        TOKEN = dotenv.get("TOKEN");
    }
}
