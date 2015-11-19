package MCSPAlgorithm;

import MBSPLAlgorithm.MBSPLAlgorithm;

import java.io.File;
import java.io.IOException;

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

    }
}
