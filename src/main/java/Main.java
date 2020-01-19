import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Main {
    public static void main(String[] args) {
        MetroParser parser = new MetroParser();
        StationIndex moscowMetro = null;
        try {
            moscowMetro = parser.getStationIndex();
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        if (moscowMetro != null) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(moscowMetro);
            System.out.println(json);
        }
    }
}
