package GroundTruth;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import Cluster.Cluster;

public class GroundTruth {
    private Set<Cluster> groundTruth;

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

                Cluster cluster = new Cluster(split[0]);
                for (int i = 1; i < split.length; i++) {
                    cluster.add(split[i]);
                }
                groundTruth.add(cluster);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Truth file not found!");
        }

    }
}
