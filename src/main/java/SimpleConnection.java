import org.jetbrains.annotations.NotNull;

public class SimpleConnection {
    private String line;
    private String station;

    public SimpleConnection(Station station) {
        this.line = station.getLine().getNumber();
        this.station = station.getName();
    }

    public String getLine() {
        return line;
    }

    public String getStation() {
        return station;
    }


}
