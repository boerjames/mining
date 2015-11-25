package GroundTruth;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import AdjacencyListGraphImplementation.AdjacencyListGraph;
import AdjacencyListGraphImplementation.AdjacencyListVertex;
import AdjacencyMatrixGraphImplementation.AdjacencyMatrixVertex;
import Cluster.*;
import Graph.Vertex;

public class GroundTruth {
    private int clusterID = 0;
    private Set<AdjacencyListCluster> groundTruth;
    private AdjacencyListGraph graph;

    public GroundTruth(String graphFile, String truthFile) {
        graph = new AdjacencyListGraph();
        buildGraph(graphFile);

        groundTruth = new HashSet<>();
        buildGroundTruth(truthFile);
    }

    public double findFScoreWeightedL(Set<AdjacencyListCluster> clusters) {
        if (clusters.size() < 1) return 0.0;

        double scores = 0.0;

        for (AdjacencyListCluster cluster : clusters) {
            double score = findMaxFScoreL(cluster) * cluster.getCluster().size() / graph.getVertices().size();
            scores += score;
        }

        return scores / clusters.size();
    }

    public double findFScoreWeightedM(Set<Cluster<AdjacencyMatrixVertex>> clusters) {
        if (clusters.size() < 1) return 0.0;

        double scores = 0.0;

        for (Cluster<AdjacencyMatrixVertex> cluster : clusters) {
            double score = findMaxFScoreM(cluster) * cluster.getCluster().size() / graph.getVertices().size();
            scores += score;
        }

        return scores / clusters.size();
    }

    public double findFScoreL(Set<AdjacencyListCluster> clusters) {
        if (clusters.size() < 1) return 0.0;

        double scores = 0.0;

        for (AdjacencyListCluster cluster : clusters) {
            double score = findMaxFScoreL(cluster);
            scores += score;
        }

        return scores / clusters.size();
    }

    public double findFScoreM(Set<Cluster<AdjacencyMatrixVertex>> clusters) {
        if (clusters.size() < 1) return 0.0;

        double scores = 0.0;

        for (Cluster<AdjacencyMatrixVertex> cluster : clusters) {
            double score = findMaxFScoreM(cluster);
            scores += score;
        }

        return scores / clusters.size();
    }

    private double findMaxFScoreL(AdjacencyListCluster cluster) {
        double max = 0.0;

        for (AdjacencyListCluster truthCluster : groundTruth) {
            Set<AdjacencyListVertex> tp = new HashSet<>(cluster.getCluster());
            tp.retainAll(truthCluster.getCluster());
            if (tp.size() == 0) continue;

            double precision = ((double) tp.size()) / cluster.getCluster().size();
            double recall = ((double) tp.size()) / truthCluster.getCluster().size();

            double f = 2 * (precision * recall) / (precision + recall);

            max = Math.max(max, f);
        }

        return !Double.isNaN(max) ? max : 0.0;
    }

    private double findMaxFScoreM(Cluster<AdjacencyMatrixVertex> cluster) {
        double max = 0.0;

        for (AdjacencyListCluster truthCluster : groundTruth) {
            Set<Vertex> tp = new HashSet<>(cluster.getCluster());
            tp.retainAll(truthCluster.getCluster());
            if (tp.size() == 0) continue;

            double precision = ((double) tp.size()) / cluster.getCluster().size();
            double recall = ((double) tp.size()) / truthCluster.getCluster().size();
            double f = 2 * (precision * recall) / (precision + recall);

            max = Math.max(max, f);
        }

        return max;
    }

    private void buildGraph(String graphFile) {
        Scanner scanner;
        try {
            File file = new File(graphFile);
            scanner = new Scanner(file);

            // add nodes to graph
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] split = line.split("\t");
                for (String s : split) {
                    AdjacencyListVertex vertex = new AdjacencyListVertex(s);
                    graph.addVertex(vertex);
                }
            }

            // add edges to graph
            scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] split = line.split("\t");
                AdjacencyListVertex v1 = new AdjacencyListVertex(split[0]);
                AdjacencyListVertex v2 = new AdjacencyListVertex(split[1]);

                graph.addEdge(v1, v2);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Graph file not found!");
        }

    }
    private void buildGroundTruth(String truthFile) {
        Scanner scanner;
        try {
            File file = new File(truthFile);
            scanner = new Scanner(file);

            while (scanner.hasNext()) {
                String line = scanner.nextLine().trim();
                String[] split = line.split(" ");

                AdjacencyListCluster cluster = new AdjacencyListCluster(clusterID++);
                for (int i = 1; i < split.length; i++) {
                    cluster.add(new AdjacencyListVertex(split[i]));
                }
                groundTruth.add(cluster);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Truth file not found!");
        }

    }
}
