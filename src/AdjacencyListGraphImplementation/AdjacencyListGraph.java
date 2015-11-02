package AdjacencyListGraphImplementation;

import Graph.Graph;

import java.util.Set;
import Graph.Vertex;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by CONNER on 11/2/2015.
 */
public class AdjacencyListGraph implements Graph {

    Set<Vertex> vertices = new TreeSet<>();

    // TODO implement
    public Set<Vertex> getVertices(){
        return vertices;
    }

    // TODO implement
    public void addVertices(Set<Vertex> vertices){
        this.vertices.addAll(vertices);
    }

    // TODO implement
    public void addVertex(Vertex vertex){
        vertices.add(vertex);
    }

    // TODO implement
    public void removeVertex(Vertex vertex){
        vertices.remove(vertex);

        for(Vertex vertex1 : vertices ) {
            ((AdjacencyListVertex) vertex1).removeEdge(vertex);
        }
    }

    // TODO implement
    public void addEdge(Vertex vertex1, Vertex vertex2){
        if(!vertices.contains(vertex1)) {
            this.addVertex(vertex1);
        } else {
            vertex1 = ((SortedSet<Vertex>)vertices).tailSet(vertex1).first();
        }

        if(!vertices.contains(vertex2)) {
            this.addVertex(vertex2);
        } else {
            vertex2 = ((SortedSet<Vertex>)vertices).tailSet(vertex2).first();
        }

        ((AdjacencyListVertex)vertex1).addEdge(vertex2);
        ((AdjacencyListVertex)vertex2).addEdge(vertex1);
    }

    // TODO implement
    public void removeEdge(Vertex vertex1, Vertex vertex2){
        if(vertices.contains(vertex1)) {
            ((AdjacencyListVertex)((SortedSet<Vertex>)vertices).tailSet(vertex1).first()).removeEdge(vertex2);
        }

        if(vertices.contains(vertex2)) {
            ((AdjacencyListVertex)((SortedSet<Vertex>)vertices).tailSet(vertex2).first()).removeEdge(vertex1);
        }
    }
}
