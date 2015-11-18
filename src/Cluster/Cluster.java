package Cluster;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Cluster<T> implements Comparable<Cluster<T>>{

    private final String name;
    private final Set<T> cluster;

    public Cluster(String name) {
        this.name = name;
        this.cluster = new HashSet<>();
    }

    public void add(T item) {
        cluster.add(item);
    }

    public void addAll(Collection<T> items) {cluster.addAll(items);}

    public String getName() {
        return name;
    }

    public Set<T> getCluster() {
        return cluster;
    }

    @Override
    public int compareTo(Cluster<T> cluster) {
        return name.compareTo(cluster.getName());
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
