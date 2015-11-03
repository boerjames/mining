package SeedGrowthAlgorithm;

import AdjacencyListGraphImplementation.AdjacencyListGraph;
import AdjacencyListGraphImplementation.AdjacencyListVertex;
import Graph.Vertex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;

/**
 * Created by James on 11/2/15.
 */
public class SeedGrowthAlgorithm {
    private AdjacencyListGraph graph;

    public SeedGrowthAlgorithm(String file) {
        graph = new AdjacencyListGraph();
        getInput(file);

        for (Vertex v : graph.getVertices()) {
            System.out.println(clusteringCoefficient(v));
        }
    }

    private double clusteringCoefficient(Vertex vertex) {
        SortedSet<Vertex> neighbors = ((AdjacencyListVertex)vertex).getEdges();
        if (neighbors.size() == 0) return 0.0;

        int neighborEdges = 0;
        for (Vertex n : neighbors) {
            SortedSet<Vertex> neighborNeighbors = ((AdjacencyListVertex)n).getEdges();

            for (Vertex n2 : neighborNeighbors) {
                if (neighbors.contains(n2)) {
                    neighborEdges++;
                }
            }
        }
        if (neighborEdges == 0) return 0.0;
        return 2.0 * neighborEdges / (neighbors.size() * (neighbors.size() - 1));
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
