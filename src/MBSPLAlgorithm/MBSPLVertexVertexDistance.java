package MBSPLAlgorithm;

import AdjacencyMatrixGraphImplementation.AdjacencyMatrixVertex;
import Graph.Vertex;

import java.io.Serializable;

/**
 * Created by CONNER on 11/2/2015.
 */
public class MBSPLVertexVertexDistance implements Comparable<MBSPLVertexVertexDistance>, Serializable{

    private AdjacencyMatrixVertex vertex1, vertex2;
    private int distance;

    public MBSPLVertexVertexDistance(AdjacencyMatrixVertex vertex1, AdjacencyMatrixVertex vertex2, int distance) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.distance = distance;
    }

    public AdjacencyMatrixVertex getVertex1() {
        return vertex1;
    }

    public void setVertex1(AdjacencyMatrixVertex vertex1) {
        this.vertex1 = vertex1;
    }

    public AdjacencyMatrixVertex getVertex2() {
        return vertex2;
    }

    public void setVertex2(AdjacencyMatrixVertex vertex2) {
        this.vertex2 = vertex2;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(MBSPLVertexVertexDistance mbsplVertexVertexDistance) {
        return distance != mbsplVertexVertexDistance.getDistance() ? distance - mbsplVertexVertexDistance.getDistance() :
                (vertex1.compareTo(mbsplVertexVertexDistance.getVertex1()) == 0 ? vertex2.compareTo(mbsplVertexVertexDistance.getVertex2()) :
                        vertex1.compareTo(mbsplVertexVertexDistance.vertex1));
    }
}
