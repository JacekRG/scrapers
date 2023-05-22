import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ThirdTask {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("https://www.okg.se/.netlify/functions/getReactorOutput", String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            JSONObject json = new JSONObject(responseBody);

            int outputValue = json.getInt("output");
            String time = json.getString("time");

            System.out.println("value: " + outputValue + ", time: " + time);
        } else {
            System.out.println("Error occurred. Status code: " + response.getStatusCode());
        }
    }
}
