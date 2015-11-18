package AdjacencyMatrixGraphImplementation;

import Graph.Vertex;

import java.io.Serializable;

/**
 * Created by CONNER on 11/2/2015.
 */
public class AdjacencyMatrixVertex extends Vertex implements Serializable {

    private int vertexID;

    public AdjacencyMatrixVertex(String name, int vertexID) {
        super(name);
        this.vertexID = vertexID;
    }

    public int getVertexID() {
        return vertexID;
    }

    public int getDegree() {
        return 0;
    }
}
