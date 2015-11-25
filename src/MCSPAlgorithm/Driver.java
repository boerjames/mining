package MCSPAlgorithm;

import AdjacencyMatrixGraphImplementation.AdjacencyMatrixVertex;
import Cluster.Cluster;
import GroundTruth.GroundTruth;
import MBSPLAlgorithm.MBSPLAlgorithm;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by conner on 11/19/15.
 */
public class Driver {
    public static void main(String[] args) throws IOException {

        File file = new File("assignment5_data.txt");

        MBSPLAlgorithm algorithm = new MBSPLAlgorithm(file);
        algorithm.run();

        System.out.println(algorithm+"\n\n");

        MinimumCut minimumCut = new MinimumCut(algorithm.getClusters(), algorithm.getGraph(), algorithm.getClusterid());
        minimumCut.run();

        System.out.println(minimumCut);

        GroundTruth groundTruth = new GroundTruth("assignment5_data.txt", "complex_merged.txt");
        Set<Cluster<AdjacencyMatrixVertex>> clusters = new HashSet<>(algorithm.getClusters());
        System.out.println(groundTruth.findFScoreM(clusters));

    }
}
