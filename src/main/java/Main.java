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
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
