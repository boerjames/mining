package MBSPLAlgorithm;

import AdjacencyMatrixGraphImplementation.AdjacencyMatrixGraph;
import AdjacencyMatrixGraphImplementation.AdjacencyMatrixVertex;
import Graph.Vertex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by CONNER on 11/2/2015.
 */
public class MBSPLAlgorithm {

    private AdjacencyMatrixGraph graph;
    private SortedSet<MBSPLVertexVertexDistance> distances;

    public MBSPLAlgorithm(File dataFile) throws IOException {
        graph = new AdjacencyMatrixGraph();
        distances = new TreeSet<>();
        this.getInput(dataFile);
        floydWershall();
    }

    public void run() {
        int clusterid = 0;

        while(distances.size() > 0) {


        }
    }

    private void floydWershall() {
        ArrayList<ArrayList<Integer>> distances = new ArrayList<>();

        for(int i = 0; i < graph.getVertices().size(); i++) {
            distances.add(new ArrayList<>(graph.getVertices().size()));
            for(int j = 0; j < graph.getVertices().size(); j++) {
                distances.get(i).set(j, graph.getEdge(graph.getVertex(i), graph.getVertex(j)));
            }
        }

        for(int k = 0;  k < distances.size(); k++) {
            for(int i = 0; i < distances.size(); i++) {
                for(int j = 0; j < distances.size(); j++) {
                    if(distances.get(i).get(j) > distances.get(i).get(k) + distances.get(k).get(j)) {
                        distances.get(i).set(j, distances.get(i).get(k) + distances.get(k).get(j));
                    }
                }
            }
        }

        for(int i = 0; i < graph.getVertices().size(); i++) {
            for(int j = 0; j < graph.getVertices().size(); j++) {
                this.distances.add(new MBSPLVertexVertexDistance((AdjacencyMatrixVertex)graph.getVertex(i),
                        (AdjacencyMatrixVertex)graph.getVertex(j), distances.get(i).get(j)));
            }
        }

    }

    private void getInput(File dataFile) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(dataFile));

        String line;

        while((line = bufferedReader.readLine()) != null) {
            Scanner scanner = new Scanner(line).useDelimiter("\\t");

            Vertex vertex1 = new AdjacencyMatrixVertex(scanner.next(), -1);
            Vertex vertex2 = new AdjacencyMatrixVertex(scanner.next(), -1);

            graph.addEdge(vertex1, vertex2);
        }
    }
}
