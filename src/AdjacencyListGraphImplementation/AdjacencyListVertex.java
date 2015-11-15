package AdjacencyListGraphImplementation;

import Graph.Vertex;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by CONNER on 11/2/2015.
 */
public class AdjacencyListVertex extends Vertex {

    Set<Vertex> edges;

    public AdjacencyListVertex(String name) {
        super(name);
        edges = new HashSet<>();
    }

    public Set<Vertex> getEdges() {
        return edges;
    }

    public void addEdge(Vertex vertex) {
        edges.add(vertex);
    }

    public void removeEdge(Vertex vertex) {
        edges.remove(vertex);
    }

    public int getDegree() {
        return edges.size();
    }
}
