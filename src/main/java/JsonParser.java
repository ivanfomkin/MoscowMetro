import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class JsonParser {
    public static List<String> parseStationFromJSON(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(new File(filePath));
        JsonNode stationsNode = rootNode.get("stations");
        List<String> stationsList = new ArrayList<>(); //Будем хранить тут имена станций
        /**
         * Парсим имена станций из JSONа
         */
        Iterator<JsonNode> jsonNodeIterator = stationsNode.getElements();
        jsonNodeIterator.forEachRemaining(jsonNode -> {
            jsonNode.getElements().forEachRemaining(stationName -> stationsList.add(stationName.asText()));
        });
        return stationsList;
    }
}
