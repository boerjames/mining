package AdjacencyMatrixGraphImplementation;

import Graph.Graph;
import Graph.Vertex;

import java.util.*;

/**
 * Created by CONNER on 11/2/2015.
 */
public class AdjacencyMatrixGraph implements Graph {

    private Set<Vertex> vertices;
    private ArrayList<ArrayList<Integer>> adjacencyMatrix;

    public AdjacencyMatrixGraph() {
        vertices = new TreeSet<>();
        adjacencyMatrix = new ArrayList<>();
    }

    // TODO implement
    public Set<Vertex> getVertices(){
        return vertices;
    }

    // TODO implement
    public void addVertices(Set<Vertex> vertices){
        for (Vertex vertex : vertices) {
            if(!this.vertices.contains(vertex)) {
                AdjacencyMatrixVertex adjacencyMatrixVertex = new AdjacencyMatrixVertex(vertex.getName(), this.vertices.size());

                this.vertices.add(adjacencyMatrixVertex);

                for(ArrayList<Integer> arrayList : this.adjacencyMatrix) {
                    arrayList.add(0);
                }

                adjacencyMatrix.add(new ArrayList<>(this.vertices.size()));
            }
        }
    }

    // TODO implement
    public void addVertex(Vertex vertex){
        if(!this.vertices.contains(vertex)) {
            AdjacencyMatrixVertex adjacencyMatrixVertex = new AdjacencyMatrixVertex(vertex.getName(), this.vertices.size());

            this.vertices.add(adjacencyMatrixVertex);

            for(ArrayList<Integer> arrayList : this.adjacencyMatrix) {
                arrayList.add(Integer.MAX_VALUE);
            }

            ArrayList<Integer> arrayList = new ArrayList<>();
            for(Vertex vertex1 : vertices) {
                if(arrayList.size()-1 == adjacencyMatrixVertex.getVertexID()) {
                    arrayList.add(0);
                } else {
                    arrayList.add(Integer.MAX_VALUE);
                }

            }
            adjacencyMatrix.add(arrayList);
        }
    }

    // TODO implement
    public void removeVertex(Vertex vertex){
        if(vertices.contains(vertex)) {
            AdjacencyMatrixVertex adjacencyMatrixVertex = (AdjacencyMatrixVertex)((SortedSet<Vertex>)this.vertices).tailSet(vertex).first();
            this.vertices.remove(adjacencyMatrixVertex);
            for(ArrayList<Integer> arrayList : adjacencyMatrix) {
                arrayList.remove(adjacencyMatrixVertex.getVertexID());
            }

            this.adjacencyMatrix.remove(adjacencyMatrixVertex.getVertexID());
        }
    }

    // TODO implement
    public void addEdge(Vertex vertex1, Vertex vertex2){
        if(!this.vertices.contains(vertex1)) {
            this.addVertex(vertex1);
        } if(!this.vertices.contains(vertex2)) {
            this.addVertex(vertex2);
        }

        AdjacencyMatrixVertex adjacencyMatrixVertex1 = (AdjacencyMatrixVertex)((SortedSet<Vertex>)this.vertices).tailSet(vertex1).first();
        AdjacencyMatrixVertex adjacencyMatrixVertex2 = (AdjacencyMatrixVertex)((SortedSet<Vertex>)this.vertices).tailSet(vertex2).first();

        this.adjacencyMatrix.get(adjacencyMatrixVertex1.getVertexID()).set(adjacencyMatrixVertex2.getVertexID(), 1);
        this.adjacencyMatrix.get(adjacencyMatrixVertex2.getVertexID()).set(adjacencyMatrixVertex1.getVertexID(), 1);
    }

    // TODO implement
    public void removeEdge(Vertex vertex1, Vertex vertex2){
        AdjacencyMatrixVertex adjacencyMatrixVertex1 = (AdjacencyMatrixVertex)((SortedSet<Vertex>)this.vertices).tailSet(vertex1).first();
        AdjacencyMatrixVertex adjacencyMatrixVertex2 = (AdjacencyMatrixVertex)((SortedSet<Vertex>)this.vertices).tailSet(vertex2).first();

        this.adjacencyMatrix.get(adjacencyMatrixVertex1.getVertexID()).set(adjacencyMatrixVertex2.getVertexID(), 0);
        this.adjacencyMatrix.get(adjacencyMatrixVertex2.getVertexID()).set(adjacencyMatrixVertex1.getVertexID(), 0);
    }

    public int getEdge(Vertex vertex1, Vertex vertex2) {
        AdjacencyMatrixVertex adjacencyMatrixVertex1 = (AdjacencyMatrixVertex)((SortedSet<Vertex>)this.vertices).tailSet(vertex1).first();
        AdjacencyMatrixVertex adjacencyMatrixVertex2 = (AdjacencyMatrixVertex)((SortedSet<Vertex>)this.vertices).tailSet(vertex2).first();

        return this.adjacencyMatrix.get(adjacencyMatrixVertex1.getVertexID()).get(adjacencyMatrixVertex2.getVertexID());
    }

    public Vertex getVertex(int vectorID) {
        ArrayList<Vertex> sortedSet = new ArrayList<>();
        sortedSet.addAll(this.vertices);
        Collections.sort(sortedSet, new VertexIDComparator());
        return sortedSet.get(vectorID);
    }

    public ArrayList<ArrayList<Integer>> getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    public Set<AdjacencyMatrixVertex> getEdges(AdjacencyMatrixVertex vertex) {
        Set<AdjacencyMatrixVertex> edges = new HashSet<>();
        for(int i = 0; i < adjacencyMatrix.get(vertex.getVertexID()).size(); i++) {
            if(adjacencyMatrix.get(vertex.getVertexID()).get(i) == 1) {
                edges.add((AdjacencyMatrixVertex)getVertex(i));
            }
        }
        return edges;
    }

    class VertexIDComparator implements Comparator<Vertex> {
        @Override
        public int compare(Vertex adjacencyMatrixVertex1, Vertex adjacencyMatrixVertex2) {
            return ((AdjacencyMatrixVertex)adjacencyMatrixVertex1).getVertexID() - ((AdjacencyMatrixVertex)adjacencyMatrixVertex2).getVertexID();
        }
    }
}
