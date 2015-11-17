package MBSPLAlgorithm;

import AdjacencyMatrixGraphImplementation.AdjacencyMatrixGraph;
import AdjacencyMatrixGraphImplementation.AdjacencyMatrixVertex;
import Cluster.Cluster;
import Graph.Vertex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.UnaryOperator;

/**
 * Created by CONNER on 11/2/2015.
 */
public class MBSPLAlgorithm {

    private static final double THRESHOLD = 5;

    private AdjacencyMatrixGraph graph;
    private SortedSet<MBSPLVertexVertexDistance> distances;
    private SortedSet<Cluster<AdjacencyMatrixVertex>> clusters;
    private Map<AdjacencyMatrixVertex, String> vertexClusterID;

    public MBSPLAlgorithm(File dataFile) throws IOException {
        graph = new AdjacencyMatrixGraph();
        distances = new TreeSet<>();
        this.getInput(dataFile);
        dijkstra();
    }

    public void run() {
        int clusterid = 0;

        while(distances.size() > 0 || distances.first().getDistance() > THRESHOLD) {

            MBSPLVertexVertexDistance mbsplVertexVertexDistance = distances.first();
            distances.remove(mbsplVertexVertexDistance);

            Cluster<AdjacencyMatrixVertex> cluster1 = null;
            Cluster<AdjacencyMatrixVertex> cluster2 = null;

            if(vertexClusterID.containsKey(mbsplVertexVertexDistance.getVertex1())) {
                cluster1 = getClusterByID(vertexClusterID.get(mbsplVertexVertexDistance.getVertex1()));
            } if(vertexClusterID.containsKey(mbsplVertexVertexDistance.getVertex2())) {
                cluster2 = getClusterByID(vertexClusterID.get(mbsplVertexVertexDistance.getVertex2()));
            }

            if(cluster1 != null) {

                if (cluster2 != null) {
                    cluster1.addAll(cluster2.getCluster());
                    for(AdjacencyMatrixVertex vertex : cluster2.getCluster()) {
                        vertexClusterID.put(vertex, cluster1.getName());
                    }
                } else {
                    cluster1.add(mbsplVertexVertexDistance.getVertex2());
                    vertexClusterID.put(mbsplVertexVertexDistance.getVertex2(), cluster1.getName());
                }

            } else if(cluster2 != null) {

                cluster2.add(mbsplVertexVertexDistance.getVertex1());
                vertexClusterID.put(mbsplVertexVertexDistance.getVertex1(), cluster2.getName());

            } else {
                cluster1 = new Cluster<AdjacencyMatrixVertex>(clusterid+"");
                cluster1.add(mbsplVertexVertexDistance.getVertex1());
                cluster1.add(mbsplVertexVertexDistance.getVertex2());

                vertexClusterID.put(mbsplVertexVertexDistance.getVertex2(), cluster1.getName());
                vertexClusterID.put(mbsplVertexVertexDistance.getVertex1(), cluster1.getName());

                clusterid++;
            }
        }

        for(Cluster<AdjacencyMatrixVertex> cluster : clusters) {
            if (cluster.getCluster().size() < 3) {
                clusters.remove(cluster);
            }
        }
    }

    private Cluster<AdjacencyMatrixVertex> getClusterByID(String clusterid) {
        Cluster<AdjacencyMatrixVertex> cluster = null;

        for(Cluster<AdjacencyMatrixVertex> cluster1 : clusters) {
            if(cluster1.getName().compareTo(clusterid) == 0) {
                cluster = cluster1;
            }
        }

        return cluster;
    }

    private void floydWershall() {
        ArrayList<ArrayList<Integer>> distances = new ArrayList<>(graph.getAdjacencyMatrix());

//        for(int i = 0; i < graph.getVertices().size(); i++) {
//            distances.add(new ArrayList<>(graph.getVertices().size()));
//            for(int j = 0; j < graph.getVertices().size(); j++) {
//                distances.get(i).add(graph.getEdge(graph.getVertex(i), graph.getVertex(j)));
//            }
//        }

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

    private void dijkstra() {

        ArrayList<ArrayList<Integer>> distances = new ArrayList<>(graph.getAdjacencyMatrix());

        for(ArrayList<Integer> integers : distances) {
            integers.replaceAll(new UnaryOperator<Integer>() {
                @Override
                public Integer apply(Integer integer) {
                    return integer == 0 ? Integer.MAX_VALUE : integer;
                }
            });
        }




        for (int i = 0; i < distances.size(); i++) {
            final int fin = i;
            SortedSet<Integer> q = new TreeSet<>(new Comparator<Integer>(){
                @Override
                public int compare(Integer o1, Integer o2) {
                    return distances.get(fin).get(o1) - distances.get(fin).get(o2);
                }
            });

            q.addAll(distances.get(i));

            distances.get(i).set(i, 0);

            while(q.size() > 0) {
                int vertex = q.first();
                q.remove(vertex);

                for(int neighbor : distances.get(vertex)) {
                    int alt = distances.get(i).get(vertex);
                    if(alt < distances.get(i).get(neighbor)) {
                        distances.get(i).set(neighbor, alt);
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

    @Override
    public String toString() {
        String ret = clusters.size() + "\n";

        for (Cluster<AdjacencyMatrixVertex> cluster : clusters) {

            ret += cluster.getCluster().size();

            for(Vertex vertex : cluster.getCluster()) {
                ret += " " +vertex.getName();
            }

            ret += "\n";
        }

        return ret;
    }
}
