import org.codehaus.jackson.map.ObjectMapper;

import java.io.FileOutputStream;

public class Main {

    public static void main(String[] args) {
        final String JSON_OUT_FILE = "src/main/resources/metro.json";
        MetroParser parser = new MetroParser();
        StationIndex moscowMetro = null;
        try {
            moscowMetro = parser.getStationIndex();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (moscowMetro != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(new FileOutputStream(JSON_OUT_FILE), moscowMetro);
                System.out.println("All station parsed! Result file: " + JSON_OUT_FILE);
                int stationCounter = JsonParser.parseStationFromJSON(JSON_OUT_FILE).size();
                System.out.println("Count of all stations of metro, Monorail and  Moscow Central Ring is " + stationCounter);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
