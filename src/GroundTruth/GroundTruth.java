package GroundTruth;

import AdjacencyListGraphImplementation.AdjacencyListVertex;
import Cluster.AdjacencyListCluster;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class GroundTruth {
    private Set<AdjacencyListCluster> groundTruth;

    public GroundTruth(String truthFile) {
        groundTruth = new HashSet<>();
        buildGroundTruth(truthFile);
    }

    private void buildGroundTruth(String truthFile) {
        Scanner scanner;
        try {
            File file = new File(truthFile);
            scanner = new Scanner(file);

            while (scanner.hasNext()) {
                String line = scanner.nextLine().trim();
                String[] split = line.split(" ");

                AdjacencyListCluster cluster = new AdjacencyListCluster(split[0]);
                for (int i = 1; i < split.length; i++) {
                    AdjacencyListVertex v = new AdjacencyListVertex(split[i]);
                    cluster.add(v);
                }
                groundTruth.add(cluster);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Truth file not found!");
        }

    }
}
