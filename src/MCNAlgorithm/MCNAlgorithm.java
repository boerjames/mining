package MCNAlgorithm;

import AdjacencyListGraphImplementation.AdjacencyListGraph;
import AdjacencyListGraphImplementation.AdjacencyListVertex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import Cluster.AdjacencyListCluster;
import Graph.Vertex;


public class MCNAlgorithm {
    private int clusterID = 0;
    private AdjacencyListGraph graph;
    private Set<AdjacencyListCluster> clusters;

    public MCNAlgorithm(String file) {
        graph = new AdjacencyListGraph();
        clusters = new HashSet<>();
        getInput(file);
    }

    public void run(double threshold) {

        // to prepare, make each vertex into a cluster
        for (Vertex v : graph.getVertices()) {
            AdjacencyListCluster c = new AdjacencyListCluster(clusterID++);
            c.add((AdjacencyListVertex)v);
            clusters.add(c);
        }

        // repeat 1 and 2 until no more clusters can be merged
        boolean mergePossible;
        double maxSimilarity;
        int iteration = 0;
        do {
            System.out.println("Iteration: " + (++iteration));
            System.out.println("Num clusters: " + clusters.size());
            mergePossible = false;
            maxSimilarity = 0.0;
            AdjacencyListCluster max1 = null;
            AdjacencyListCluster max2 = null;

            // 1. find the most similar vertices from different clusters based on a similarity function
            for (AdjacencyListCluster c1 : clusters) {
                for (AdjacencyListCluster c2 : clusters) {
                    if (c1.equals(c2)) continue;

                    double similarity = c1.similarity(c2, "jaccard");

                    if (similarity > threshold && similarity > maxSimilarity) {
                        max1 = c1;
                        max2 = c2;
                        maxSimilarity = similarity;
                        mergePossible = true;
                    }
                }
            }

            // 2. merge the two clusters if the merged cluster reaches a density threshold
            if (mergePossible) {
                AdjacencyListCluster merge = new AdjacencyListCluster(clusterID++, max1, max2);
                clusters.add(merge);
                clusters.remove(max1);
                clusters.remove(max2);
            }

        } while (mergePossible);

    }

    public void print() {
        System.out.println(clusters.size());
        for (AdjacencyListCluster c : clusters) {
            System.out.print(c.getID() + " [" + c.getCluster().size() + "]");
            for (AdjacencyListVertex s : c.getCluster()) {
                System.out.println(" " + s.getName());
            }
        }
    }

    public void save() {
        PrintWriter writer;
        try {
            writer = new PrintWriter("mcn_results.txt");

            // print out the clusters
            for (AdjacencyListCluster cluster : clusters) {
                for (Vertex vertex : cluster.getCluster()) {
                    writer.print(" " + vertex.getName());
                }
            }

        } catch (Exception e) {
            System.out.println("oh no");
        }
    }

    public Set<AdjacencyListCluster> getClusters() {
        return clusters;
    }

    private void getInput(String fileString) {
        Scanner scanner = null;
        try {
            File file = new File(fileString);
            scanner = new Scanner(file);

            while (scanner.hasNext()) {
                String line = scanner.nextLine().trim();
                String[] split = line.split("\t");

                AdjacencyListVertex v1 = new AdjacencyListVertex(split[0]);
                AdjacencyListVertex v2 = new AdjacencyListVertex(split[1]);

                graph.addEdge(v1, v2);
                graph.addEdge(v2, v1);

            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found!");
        }

    }
}
