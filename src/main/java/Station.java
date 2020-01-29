import org.codehaus.jackson.annotate.JsonIgnore;

public class Station implements Comparable<Station> {
    @JsonIgnore
    private Line line;
    private String name;

    public Station(String name, Line line) {
        this.line = line;
        this.name = name;
    }

    public Line getLine() {
        return line;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        return compareTo((Station) obj) == 0;
    }

    @Override
    public int compareTo(Station o) {
        int lineComparision = line.compareTo(o.getLine());
        if (lineComparision != 0) {
            return lineComparision;
        }
        return name.compareToIgnoreCase(o.getName());
    }
}
