package MCNAlgorithm;

public class Driver {

    public static void main(String[] args) {
        MCNAlgorithm mcn = new MCNAlgorithm("assignment5_data.txt");
        mcn.run(0.1);
        mcn.print();
    }
}
