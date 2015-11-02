import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Graph {
    private Map<String, Set<String>> neighborList;

    public Graph(String graphFile) {
        neighborList = new HashMap<>();
        buildGraph(graphFile);
    }

    private void buildGraph(String graphFile) {
        Scanner scanner;
        try {
            File file = new File(graphFile);
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
            System.err.println("Graph file not found!");
        }

    }
}
