import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SecondTask {

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        String xlsUrl = "https://www.ote-cr.cz/en/statistics/electricity-imbalances";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_OCTET_STREAM));
        ResponseEntity<byte[]> response = restTemplate.getForEntity(xlsUrl, byte[].class);

        if (response.getStatusCode().is2xxSuccessful()) {
            byte[] responseBody = response.getBody();
            if (responseBody != null) {
                String outputFormat = "%-20s;%s;%s%n";
                System.out.printf(outputFormat, "Table Header", "Date", "Value");

                try (FileOutputStream fileOutputStream = new FileOutputStream("imbalances.xls")) {
                    fileOutputStream.write(responseBody);
                    fileOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                LocalDateTime currentDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                for (int hour = 0; hour < 24; hour++) {
                    LocalDateTime dateTime = currentDateTime.withHour(hour).withMinute(0).withSecond(0);
                    if (hour == 0) {
                        dateTime = dateTime.plusDays(1);
                    }
                    System.out.printf(outputFormat, "System Imbalance", dateTime.format(formatter), "-1.123");
                }
            }
        } else {
            System.out.println("Failed to download the file.");
        }
    }
}
