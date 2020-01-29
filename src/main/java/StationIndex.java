import org.codehaus.jackson.annotate.JsonProperty;

import java.util.*;
import java.util.stream.Collectors;

public class StationIndex {
    private HashMap<String, Line> number2line;
    private TreeSet<Station> stations;
    private TreeMap<Station, TreeSet<Station>> connections;
    private List<List<SimpleConnection>> simpleConnections; //Будем хранить переходы в упрощённом виде

    public StationIndex() {
    }

    public StationIndex(String name) {
        number2line = new HashMap<>();
        stations = new TreeSet<>();
        connections = new TreeMap<>();
        System.out.println(name + " successful created!");
    }

    public void addStation(Station station) {
        stations.add(station);
    }

    public void addLine(Line line) {
        number2line.put(line.getNumber(), line);
    }

    public void addConnection(List<Station> stations) {
        for (Station station : stations) {
            if (!connections.containsKey(station)) {
                connections.put(station, new TreeSet<>());
            }
            TreeSet<Station> connectedStations = connections.get(station);
            connectedStations.addAll(stations.stream()
                    .filter(s -> !s.equals(station)).collect(Collectors.toList()));
        }
    }

    public Line getLine(String number) {
        return number2line.get(number);
    }

    public Station getStation(String name) {
        for (Station station : stations) {
            if (station.getName().equalsIgnoreCase(name)) {
                return station;
            }
        }
        return null;
    }

    public Station getStation(String name, String lineNumber) {
        Station query = new Station(name, getLine(lineNumber));
        Station station = stations.ceiling(query);
        return station.equals(query) ? station : null;
    }

    public Set<Station> getConnectedStations(Station station) {
        if (connections.containsKey(station)) {
            return connections.get(station);
        }
        return new TreeSet<>();
    }

    @JsonProperty("lines")
    public Collection<Line> getLines() {
        return number2line.values();
    }

    @JsonProperty("stations")
    public Map<String, List<String>> getStations() {
        Map<String, List<String>> stationsWithLineNumbers = new HashMap<>();
        number2line.values().forEach(line ->
                stationsWithLineNumbers.put(line.getNumber(), line.getStationNames()));
        return stationsWithLineNumbers;
    }

    public void createSimpleConnections() {
        simpleConnections = new ArrayList<>();
        connections.values().forEach(set -> set.forEach(station -> {
                    List<SimpleConnection> connectedStations = new ArrayList<>();
                    connectedStations.add(new SimpleConnection(station));
                    connections.get(station).forEach(station1 -> connectedStations.add(new SimpleConnection(station1)));
                    simpleConnections.add(connectedStations);
                }
        ));
    }

    @JsonProperty("connections")
    public List<List<SimpleConnection>> getSimpleConnections() {
        return simpleConnections;
    }
}
