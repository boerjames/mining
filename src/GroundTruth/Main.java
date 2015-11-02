package GroundTruth;

public class Main {

    public static void main(String[] args) {
        GroundTruthGraph groundTruthGraph = new GroundTruthGraph("assignment5_data.txt");
        GroundTruth groundTruth = new GroundTruth("complex_merged.txt");
    }
}
