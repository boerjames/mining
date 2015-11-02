package Graph;

import java.util.Set;

/**
 * Created by CONNER on 11/2/2015.
 */
public interface Graph {

    Set<Vertex> getVertices();
    void addVertices(Set<Vertex> vertices);
    void addVertex(Vertex vertex);
    void removeVertex(Vertex vertex);
    void addEdge(Vertex vertex1, Vertex vertex2);
    void removeEdge(Vertex vertex1, Vertex vertex2);

}
