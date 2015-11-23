package MBSPLAlgorithm;

import GroundTruth.GroundTruth;

import java.io.File;
import java.io.IOException;

/**
 * Created by CONNER on 11/2/2015.
 */
public class Driver {

    public static void main(String[] args) throws IOException {

        GroundTruth groundTruth = new GroundTruth("assignment5_data.txt", "complex_merged.txt");

        File file = new File("assignment5_data.txt");

        MBSPLAlgorithm algorithm = new MBSPLAlgorithm(file);
        algorithm.run();

        System.out.println(algorithm);

        System.out.println(groundTruth.findFScoreM(algorithm.getClusters()));

    }
}
