package MCSPAlgorithm;

import AdjacencyMatrixGraphImplementation.AdjacencyMatrixGraph;
import AdjacencyMatrixGraphImplementation.AdjacencyMatrixVertex;
import Cluster.Cluster;
import Graph.Vertex;
import MBSPLAlgorithm.MBSPLVertexVertexDistance;

import java.io.*;
import java.util.*;
import java.util.function.UnaryOperator;

/**
 * Created by conner on 11/19/15.
 */
public class MinimumCut {

    private static double DENSITYTHRESHOLD = .1;

    private AdjacencyMatrixGraph graph;
    private SortedSet<Cluster<AdjacencyMatrixVertex>> clusters;
    private SortedSet<MBSPLVertexVertexDistance> distances;
    private int clusterid;

    public MinimumCut(SortedSet<Cluster<AdjacencyMatrixVertex>> clusters, AdjacencyMatrixGraph graph, int clusterid) throws IOException{
        this.clusters = clusters;
        this.graph = graph;
        this.clusterid = clusterid;
        this.distances = new TreeSet<>();

        //Read shortest paths
        File file = new File("shortest-paths.txt");
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        ObjectInput objectInput = new ObjectInputStream(bufferedInputStream);
        try {
            MBSPLVertexVertexDistance distance;
            try {
                while ((distance = (MBSPLVertexVertexDistance) objectInput.readObject()) != null)
                    distances.add(distance);
            } catch (EOFException e) {}
        } catch (ClassNotFoundException e) {}
    }

    public void run() {
        List<MBSPLVertexVertexDistance> reverseDistances = new ArrayList<>(distances);
        Collections.reverse(reverseDistances);

        while (containsClusterUnderDensity()) {
            System.out.print(clusters.size() + " ");
            // TODO Possibly Sort for speed. James is lazy.
            for(Cluster<AdjacencyMatrixVertex> cluster : clusters) {
                System.out.print(cluster.getName() + " ");
                // TODO Possibly add map to density.
                if(getClusterDensity(cluster) < DENSITYTHRESHOLD) {
                    System.out.print(cluster.getName() + "+ ");
                    AdjacencyMatrixVertex vertex1 = null, vertex2 = null;

                    for(MBSPLVertexVertexDistance distance : reverseDistances) {
                        if(cluster.getCluster().contains(distance.getVertex1()) && cluster.getCluster().contains(distance.getVertex2())) {
                            vertex1 = distance.getVertex1();
                            vertex2 = distance.getVertex2();
                            break;
                        }
                    }

                    if(vertex1 != null && vertex2 != null) {
                        System.out.print("Running Stoer Wagner");
                        modifiedStoerWagner(cluster, vertex1, vertex2);
                    }
                }
            }
        }
    }

    private void modifiedStoerWagner(Cluster<AdjacencyMatrixVertex> cluster, AdjacencyMatrixVertex vertex1, AdjacencyMatrixVertex vertex2) {

        Cluster<AdjacencyMatrixVertex> cluster1 = new Cluster<AdjacencyMatrixVertex>(clusterid+"");
        clusterid++;
        Cluster<AdjacencyMatrixVertex> cluster2 = new Cluster<AdjacencyMatrixVertex>(clusterid+"");
        clusterid++;

        //Add a neighbor for initialization
        cluster1.add(vertex1);
        cluster1.add(graph.getEdges(vertex1).iterator().next());
        cluster.getCluster().remove(vertex1);
        cluster.getCluster().remove(graph.getEdges(vertex1).iterator().next());

        cluster2.add(vertex2);
        Iterator<AdjacencyMatrixVertex> iterator = graph.getEdges(vertex2).iterator();
        AdjacencyMatrixVertex vertexn = iterator.next();
        while(iterator.hasNext() && cluster1.getCluster().contains(vertexn)) {
            vertexn = iterator.next();
        }
        cluster2.add(vertexn);
        cluster.getCluster().remove(vertex2);
        cluster.getCluster().remove(vertexn);

        // while cluster still has vertices
        while(cluster.getCluster().size() > 0) {
            // Find greatest vertex weight and add to cluster.
            AdjacencyMatrixVertex currentMaxVertexCluster1 = null;
            int currentMaxWeightCluster1 = 0;
            AdjacencyMatrixVertex currentMaxVertexCluster2 = null;
            int currentMaxWeightCluster2 = 0;

            for(AdjacencyMatrixVertex vertex : cluster.getCluster()) {
                int currentWeightCluster1 = getVertexWeight(cluster1, vertex);
                int currentWeightCluster2 = getVertexWeight(cluster2, vertex);
                if(currentWeightCluster1 > currentMaxWeightCluster1) {
                    currentMaxVertexCluster1 = vertex;
                    currentMaxWeightCluster1 = currentWeightCluster1;
                }

                if(currentWeightCluster2 > currentMaxWeightCluster2) {
                    currentMaxVertexCluster2 = vertex;
                    currentMaxWeightCluster2 = currentWeightCluster2;
                }
            }

            // if vertex is the same for both clusters check clustering coefficient
            if(currentMaxVertexCluster1.compareTo(currentMaxVertexCluster2) == 0) {
                cluster1.add(currentMaxVertexCluster1);
                double cluster1Density = getClusterDensity(cluster1);

                cluster2.add(currentMaxVertexCluster2);
                double cluster2Density = getClusterDensity(cluster2);

                if(cluster1Density > cluster2Density) {
                    cluster2.getCluster().remove(currentMaxVertexCluster2);
                } else {
                    cluster1.getCluster().remove(currentMaxVertexCluster1);
                }

                cluster.getCluster().remove(currentMaxVertexCluster1);
            } else {
                if(currentMaxWeightCluster1 > currentMaxWeightCluster2) {
                    cluster1.add(currentMaxVertexCluster1);
                    cluster.getCluster().remove(currentMaxVertexCluster1);
                } else {
                    cluster2.add(currentMaxVertexCluster2);
                    cluster.getCluster().remove(currentMaxVertexCluster2);
                }
            }
        }

        clusters.add(cluster1);
        clusters.add(cluster2);
        clusters.remove(cluster);

    }

    private int getVertexWeight(Cluster<AdjacencyMatrixVertex> cluster, AdjacencyMatrixVertex vertex) {
        int weight = 0;

        Set<AdjacencyMatrixVertex> intersectionSet = new HashSet<AdjacencyMatrixVertex>(cluster.getCluster());
        intersectionSet.retainAll(graph.getEdges(vertex));
        if(intersectionSet.size() <= 0) return 0;


        for(AdjacencyMatrixVertex vertex1 : graph.getEdges(vertex)) {
            if(cluster.getCluster().contains(vertex1)) {
                weight++;
            }
        }

        return weight;
    }

    private boolean containsClusterUnderDensity() {
        for(Cluster<AdjacencyMatrixVertex> cluster : clusters) {
            if(getClusterDensity(cluster) < DENSITYTHRESHOLD) {
                return true;
            }
        }
        return false;
    }

    private double getClusterDensity(Cluster<AdjacencyMatrixVertex> cluster) {

        int sum = 0;

        for(AdjacencyMatrixVertex vertex : cluster.getCluster()) {
            for (AdjacencyMatrixVertex vertex1 : graph.getEdges(vertex)) {
                if(cluster.getCluster().contains(vertex1)) {
                    sum++;
                }
            }
        }

        return (double)sum/(cluster.getCluster().size()*(cluster.getCluster().size()-1));
    }

    @Override
    public String toString() {
        SortedSet<Cluster<AdjacencyMatrixVertex>> clusters1 = new TreeSet<>(new Comparator<Cluster>() {
            @Override
            public int compare(Cluster o1, Cluster o2) {
                return o1.getCluster().size() == o2.getCluster().size() ? o1.getName().compareTo(o2.getName()) :
                        o2.getCluster().size() - o1.getCluster().size();
            }
        });
        clusters1.addAll(clusters);
        String ret = clusters1.size() + "\n";

        for (Cluster<AdjacencyMatrixVertex> cluster : clusters1) {

            ret += cluster.getCluster().size();

            for(Vertex vertex : cluster.getCluster()) {
                ret += " " +vertex.getName();
            }

            ret += "\n";
        }

        return ret;
    }

}
