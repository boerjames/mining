package MCNAlgorithm;

import GroundTruth.GroundTruth;

public class Driver {

    public static void main(String[] args) {
        GroundTruth groundTruth = new GroundTruth("assignment5_data.txt", "complex_merged.txt");

        MCNAlgorithm mcn = new MCNAlgorithm("assignment5_data.txt");
        mcn.run(0.2);
        mcn.print();
        mcn.save();

        System.out.println(groundTruth.findFScoreL(mcn.getClusters()));
    }

    // results
    // 0.1 --> 0.28

}
