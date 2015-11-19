package MBSPLAlgorithm;

import AdjacencyMatrixGraphImplementation.AdjacencyMatrixGraph;
import AdjacencyMatrixGraphImplementation.AdjacencyMatrixVertex;
import Cluster.Cluster;
import Graph.Vertex;

import java.io.*;
import java.nio.Buffer;
import java.util.*;
import java.util.function.UnaryOperator;

/**
 * Created by CONNER on 11/2/2015.
 */
public class MBSPLAlgorithm {

    private static final double THRESHOLD = 5;

    private AdjacencyMatrixGraph graph;
    private SortedSet<MBSPLVertexVertexDistance> distances;
    private SortedSet<Cluster<AdjacencyMatrixVertex>> clusters;
    private Map<AdjacencyMatrixVertex, String> vertexClusterID;

    public MBSPLAlgorithm(File dataFile) throws IOException {
        graph = new AdjacencyMatrixGraph();
        distances = new TreeSet<>();
        this.getInput(dataFile);
        clusters = new TreeSet<>();
        vertexClusterID = new HashMap<>();
        File file = new File("shortest-paths.txt");
        if(!file.exists()) {
            file.createNewFile();
            floydWershall();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            ObjectOutput objectOutput = new ObjectOutputStream(bufferedOutputStream);
            for(MBSPLVertexVertexDistance mbsplVertexVertexDistance : distances) {
                objectOutput.writeObject(mbsplVertexVertexDistance);
            }
            objectOutput.flush();
        } else {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            ObjectInput objectInput = new ObjectInputStream(bufferedInputStream);
            try {
                MBSPLVertexVertexDistance distance;
                try {
                    while ((distance = (MBSPLVertexVertexDistance) objectInput.readObject()) != null)
                        distances.add(distance);
                } catch (EOFException e) {}
            } catch (ClassNotFoundException e) {}
        }
    }

    public void run() {
        int clusterid = 0;

        while(distances.size() > 0 && distances.first().getDistance() <= THRESHOLD) {

            MBSPLVertexVertexDistance mbsplVertexVertexDistance = distances.first();
            distances.remove(mbsplVertexVertexDistance);

            Cluster<AdjacencyMatrixVertex> cluster1 = null;
            Cluster<AdjacencyMatrixVertex> cluster2 = null;

            if(vertexClusterID.containsKey(mbsplVertexVertexDistance.getVertex1())) {
                cluster1 = getClusterByID(vertexClusterID.get(mbsplVertexVertexDistance.getVertex1()));
            } if(vertexClusterID.containsKey(mbsplVertexVertexDistance.getVertex2())) {
                cluster2 = getClusterByID(vertexClusterID.get(mbsplVertexVertexDistance.getVertex2()));
            }
            //Check if clusters are the same.

            if(cluster1 != null) {

                if (cluster2 != null && cluster1.compareTo(cluster2) != 0) {
                    cluster1.addAll(cluster2.getCluster());
                    for(AdjacencyMatrixVertex vertex : cluster2.getCluster()) {
                        vertexClusterID.put(vertex, cluster1.getName());
                    }

                    clusters.remove(cluster2);
                } else {
                    cluster1.add(mbsplVertexVertexDistance.getVertex2());
                    vertexClusterID.put(mbsplVertexVertexDistance.getVertex2(), cluster1.getName());
                }

            } else if(cluster2 != null) {

                cluster2.add(mbsplVertexVertexDistance.getVertex1());
                vertexClusterID.put(mbsplVertexVertexDistance.getVertex1(), cluster2.getName());

            } else {
                cluster1 = new Cluster<AdjacencyMatrixVertex>(clusterid+"");
                cluster1.add(mbsplVertexVertexDistance.getVertex1());
                cluster1.add(mbsplVertexVertexDistance.getVertex2());

                clusters.add(cluster1);

                vertexClusterID.put(mbsplVertexVertexDistance.getVertex2(), cluster1.getName());
                vertexClusterID.put(mbsplVertexVertexDistance.getVertex1(), cluster1.getName());

                clusterid++;
            }

            // TODO Minnimum Cut Here
        }

        SortedSet<Cluster<AdjacencyMatrixVertex>> clusters1 = new TreeSet<>();
        clusters1.addAll(clusters);
        for(Cluster<AdjacencyMatrixVertex> cluster : clusters1) {
            if (cluster.getCluster().size() < 3) {
                clusters.remove(cluster);
            }
        }
    }

    private Cluster<AdjacencyMatrixVertex> getClusterByID(String clusterid) {
        Cluster<AdjacencyMatrixVertex> cluster = null;

        for(Cluster<AdjacencyMatrixVertex> cluster1 : clusters) {
            if(cluster1.getName().compareTo(clusterid) == 0) {
                cluster = cluster1;
            }
        }

        return cluster;
    }

    private void floydWershall() {
        ArrayList<ArrayList<Integer>> distances = new ArrayList<>(graph.getAdjacencyMatrix());

//        for(int i = 0; i < graph.getVertices().size(); i++) {
//            distances.add(new ArrayList<>(graph.getVertices().size()));
//            for(int j = 0; j < graph.getVertices().size(); j++) {
//                distances.get(i).add(graph.getEdge(graph.getVertex(i), graph.getVertex(j)));
//            }
//        }

        for(ArrayList<Integer> integers : distances) {
            integers.replaceAll(new UnaryOperator<Integer>() {
                @Override
                public Integer apply(Integer integer) {
                    return integer == 0 ? Integer.MAX_VALUE : integer;
                }
            });
        }

        for(int k = 0;  k < distances.size(); k++) {
            for(int i = 0; i < distances.size(); i++) {
                for(int j = 0; j < distances.size(); j++) {
                    if(distances.get(i).get(k) != Integer.MAX_VALUE && distances.get(k).get(j) != Integer.MAX_VALUE)
                        if(distances.get(i).get(j) > distances.get(i).get(k) + distances.get(k).get(j)) {
                            distances.get(i).set(j, distances.get(i).get(k) + distances.get(k).get(j));
                        }
                }
            }
        }

        convertDistances(distances);

    }

    private void convertDistances(ArrayList<ArrayList<Integer>> distances) {
        for(int i = 0; i < graph.getVertices().size(); i++) {
            for(int j = i+1; j < graph.getVertices().size(); j++) {
                if(distances.get(i).get(j) > 0)
                    this.distances.add(new MBSPLVertexVertexDistance((AdjacencyMatrixVertex)graph.getVertex(i),
                            (AdjacencyMatrixVertex)graph.getVertex(j), distances.get(i).get(j)));
            }
        }
    }

    private void getInput(File dataFile) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(dataFile));

        String line;

        while((line = bufferedReader.readLine()) != null) {
            Scanner scanner = new Scanner(line).useDelimiter("\\t");

            Vertex vertex1 = new AdjacencyMatrixVertex(scanner.next(), -1);
            Vertex vertex2 = new AdjacencyMatrixVertex(scanner.next(), -1);

            graph.addEdge(vertex1, vertex2);
        }
    }

    @Override
    public String toString() {
        SortedSet<Cluster<AdjacencyMatrixVertex>> clusters1 = new TreeSet<>(new Comparator<Cluster>() {
            @Override
            public int compare(Cluster o1, Cluster o2) {
                return o1.getCluster().size() == o2.getCluster().size() ? o1.getName().compareTo(o2.getName()) :
                        o2.getCluster().size() - o1.getCluster().size();
            }
        });
        clusters1.addAll(clusters);
        String ret = clusters1.size() + "\n";

        for (Cluster<AdjacencyMatrixVertex> cluster : clusters1) {

            ret += cluster.getCluster().size();

            for(Vertex vertex : cluster.getCluster()) {
                ret += " " +vertex.getName();
            }

            ret += "\n";
        }

        return ret;
    }
}
