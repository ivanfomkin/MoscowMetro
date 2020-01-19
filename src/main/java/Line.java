import java.util.*;

public class Line implements Comparable<Line> {
    private String number;
    private String name;
    private String color;
    private Set<Station> stations;

    public Line(String number, String name, String color) {
        this.number = number;
        this.name = name;
        this.color = color;
        this.stations = new TreeSet<>();
    }

    public void addStation(Station station) {
        stations.add(station);
    }

    public Set<Station> getStations() {
        return stations;
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
    public int compareTo(Line line) {
        return number.compareTo(line.getNumber());
    }

    @Override
    public String toString() {
        return name;
    }
}
