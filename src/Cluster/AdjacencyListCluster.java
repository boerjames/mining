package Cluster;

import AdjacencyListGraphImplementation.AdjacencyListVertex;
import Graph.Vertex;

import java.util.HashSet;
import java.util.Set;

public class AdjacencyListCluster {

    private final String name;
    private final Set<AdjacencyListVertex> cluster;

    public AdjacencyListCluster(String name) {
        this.name = name;
        this.cluster = new HashSet<>();
    }

    public AdjacencyListCluster(AdjacencyListCluster c1, AdjacencyListCluster c2) {
        this.name = c1.getName() + " " + c2.getName();
        this.cluster = new HashSet<>(c1.getCluster());
        this.cluster.addAll(c2.getCluster());
    }

    public void add(AdjacencyListVertex item) {
        cluster.add(item);
    }

    public String getName() {
        return name;
    }

    public Set<AdjacencyListVertex> getCluster() {
        return cluster;
    }

    public Set<AdjacencyListVertex> getNeighbors() {
        Set<AdjacencyListVertex> neighbors = new HashSet<>();

        // add all edges this cluster touches
        for (AdjacencyListVertex v : cluster) {
            Set<Vertex> edges = v.getEdges();
            for (Vertex edge : edges) {
                neighbors.add((AdjacencyListVertex)edge);
            }
        }

        // remove the cluster itself to get only the neighbors
        neighbors.removeAll(cluster);

        return neighbors;
    }

    public double similarity(AdjacencyListCluster other, String method) {
        Set<AdjacencyListVertex> neighbors = this.getNeighbors();
        Set<AdjacencyListVertex> otherNeighbors = other.getNeighbors();

        Set<AdjacencyListVertex> intersection = new HashSet<>(neighbors);
        intersection.retainAll(otherNeighbors);

        double intersectionSize = (double) intersection.size();

        if (method.equals("jaccard")) {
            neighbors.addAll(otherNeighbors);
            return intersectionSize / neighbors.size();
        } else if (method.equals("geometric")) {
            return (intersectionSize * intersectionSize) / (neighbors.size() * otherNeighbors.size());
        } else if (method.equals("dice")) {
            return (2.0 * intersectionSize) / (neighbors.size() + otherNeighbors.size());
        } else if (method.equals("simpson")) {
            return intersectionSize / Math.min(neighbors.size(), otherNeighbors.size());
        } else if (method.equals("maryland")) {
            return 0.5 * ((intersectionSize / neighbors.size()) + (intersectionSize / otherNeighbors.size()));
        } else {
            return 0.0;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (AdjacencyListVertex v : cluster) {
            sb.append(v.getName() + " ");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdjacencyListCluster cluster = (AdjacencyListCluster) o;
        return name.equals(cluster.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
