package sber.social_media_service;

import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1/vk")
@RequiredArgsConstructor
public class MainController {

    private final URIBuilder uriBuilder = new URIBuilder();
    private final VkConnectionAgent vkConnectionAgent = new VkConnectionAgent();

    @GetMapping("/get_friends/{person_id}")
    public ResponseEntity<String> getFriendsInfo(@PathVariable(value = "person_id") long personId) {

        String answer = "";

        try {
            URI uri = uriBuilder.setScheme("https").setHost("api.vk.com").setPath("method/friends.get")
                    .setParameter("access_token", vkConnectionAgent.getTOKEN())
                    .setParameter("v", "5.199")
                    .setParameter("user_id", String.valueOf(personId))
                    .setParameter("fields", "city,country,sex,contacts,education,relation,status,universities")
                    .setParameter("order", "hints")
                    .setParameter("count", "15")
                    .build();

            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(uri);
            HttpResponse response = client.execute(request);

            int status = response.getStatusLine().getStatusCode();

            if (status == 200) {
                var entity = response.getEntity();
                answer = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (URISyntaxException | IOException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(answer);
    }

    @GetMapping("/get_profile/{person_id}")
    public ResponseEntity<String> getProfileInfo(@PathVariable(value = "person_id") long personId) {
        String answer = "";

        try {
            URI uri = uriBuilder.setScheme("https").setHost("api.vk.com").setPath("method/users.get")
                    .setParameter("access_token", vkConnectionAgent.getTOKEN())
                    .setParameter("v", "5.199")
                    .setParameter("user_ids", String.valueOf(personId))
                    .setParameter("fields", "about,career,connections,contacts,city,country,sex,site,interests,military,personal,relation,relatives,universities,status")
                    .build();

            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(uri);
            HttpResponse response = client.execute(request);

            int status = response.getStatusLine().getStatusCode();

            if (status == 200) {
                var entity = response.getEntity();
                answer = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (URISyntaxException | IOException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(answer);
    }
}

//add prompt
