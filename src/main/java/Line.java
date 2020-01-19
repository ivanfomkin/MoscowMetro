import java.util.ArrayList;
import java.util.List;

public class Line implements Comparable<Line> {
    private String number;
    private String name;
    private String color;
    private List<Station> stations;

    public Line(String number, String name, String color) {
        this.number = number;
        this.name = name;
        this.color = color;
        stations = new ArrayList<>();
    }

    public void addStation(Station station) {
        if (!stations.contains(station))
            stations.add(station);
        System.out.println("Station " + station.getName() + " added to " + name);
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    @Override
    public boolean equals(Object obj) {
        return compareTo((Line) obj) == 0;
    }

    @Override
    public int compareTo(Line o) {
        return number.compareToIgnoreCase(o.getNumber());
    }

    @Override
    public String toString() {
        return name;
    }
}
