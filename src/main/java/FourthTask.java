import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FourthTask {

    private static final String FILE_URL = "http://www.nhc.noaa.gov/data/hurdat/hurdat2-nepac-1949-2016-041317.txt";
    private static final String OUTPUT_FILE = "hurricane_data_output.txt";

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(FILE_URL, HttpMethod.GET, null, String.class);
        String fileContent = response.getBody();

        List<HurricaneData> hurricaneDataList = parseHurricaneData(fileContent);
        saveHurricaneData(hurricaneDataList);
    }

    private static List<HurricaneData> parseHurricaneData(String fileContent) {
        List<HurricaneData> hurricaneDataList = new ArrayList<>();

        String[] lines = fileContent.split("\n");
        for (String line : lines) {
            if (line.startsWith("EP")) {
                String[] parts = line.split(",");
                String stormName = parts[1].trim();
                int year = Integer.parseInt(parts[0].substring(4, 8));
                int windSpeedKnots = Integer.parseInt(parts[6].trim());

                if (year >= 2015 && stormName.endsWith("A")) {
                    HurricaneData hurricaneData = new HurricaneData(stormName, windSpeedKnots);
                    hurricaneDataList.add(hurricaneData);
                }
            }
        }

        return hurricaneDataList;
    }

    private static void saveHurricaneData(List<HurricaneData> hurricaneDataList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE))) {
            if (!hurricaneDataList.isEmpty()) {
                for (HurricaneData hurricaneData : hurricaneDataList) {
                    String outputLine = hurricaneData.getStormName() + ", " + hurricaneData.getWindSpeedKnots() + " knots";
                    writer.write(outputLine);
                    writer.newLine();
                }
            } else {
                writer.write("No hurricanes found after 2015 with names ending in 'A'.");
            }

            System.out.println("Hurricane data saved to " + OUTPUT_FILE);
        } catch (IOException e) {
            System.out.println("Failed to save hurricane data: " + e.getMessage());
        }
    }

    private static class HurricaneData {
        private String stormName;
        private int windSpeedKnots;

        public HurricaneData(String stormName, int windSpeedKnots) {
            this.stormName = stormName;
            this.windSpeedKnots = windSpeedKnots;
        }

        public String getStormName() {
            return stormName;
        }

        public int getWindSpeedKnots() {
            return windSpeedKnots;
        }
    }
}
