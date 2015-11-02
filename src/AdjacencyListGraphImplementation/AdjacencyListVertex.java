package AdjacencyListGraphImplementation;

import Graph.Vertex;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by CONNER on 11/2/2015.
 */
public class AdjacencyListVertex extends Vertex {

    SortedSet<Vertex> edges;

    public AdjacencyListVertex(String name) {
        super(name);
        edges = new TreeSet<>();
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
