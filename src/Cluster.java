import java.util.HashSet;
import java.util.Set;

public class Cluster<T> {

    private final String name;
    private final Set<T> cluster;

    public Cluster(String name) {
        this.name = name;
        this.cluster = new HashSet<>();
    }

    public void add(T item) {
        cluster.add(item);
    }

    public String getName() {
        return name;
    }

    public Set<T> getCluster() {
        return cluster;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cluster cluster = (Cluster) o;
        return name.equals(cluster.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
