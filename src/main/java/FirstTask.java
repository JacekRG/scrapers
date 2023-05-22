import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FirstTask {
    private static final String URL = "https://www.ote-cr.cz/en/statistics/electricity-imbalances";

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.GET, null, String.class);
        String html = response.getBody();

        int startIndex = html.indexOf("<table class=\"table");
        int endIndex = html.indexOf("</table>", startIndex);

        if (startIndex != -1 && endIndex != -1) {
            String tableHtml = html.substring(startIndex, endIndex + 8);

            saveAsHtmlFile(tableHtml, "electricity_imbalance.html");
            System.out.println("Tabela została  pobrana a następnie zapisana jako plik HTML.");
        } else {
            System.out.println("Tabela 'Statistics Electricity Imbalance' nie została znaleziona");
        }
    }

    private static void saveAsHtmlFile(String html, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(html);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
