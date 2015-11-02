import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Graph {
    private Map<String, Set<String>> neighborList;

    public Graph(String fileString) {
        neighborList = new HashMap<>();
        readFile(fileString);

        for (String s : neighborList.keySet()) {
            System.out.println(s);
            for (String s1 : neighborList.get(s)) {
                System.out.println("--> " + s1);
            }
        }
    }

    private void readFile(String fileString) {
        Scanner scanner;
        try {
            File file = new File(fileString);
            scanner = new Scanner(file);

            // add nodes to graph
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] split = line.split("\t");
                for (String s : split) {
                    if (!neighborList.containsKey(s)) {
                        neighborList.put(s, new HashSet<>());
                    }
                }
            }

            // add edges to graph
            scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] split = line.split("\t");
                neighborList.get(split[0]).add(split[1]);
                neighborList.get(split[1]).add(split[0]);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found!");
        }

    }
}
