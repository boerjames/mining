package Graph;

/**
 * Created by CONNER on 11/2/2015.
 */
public abstract class Vertex implements Comparable<Vertex> {

    private String name;

    public Vertex() {}

    public Vertex(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract int getDegree();

    @Override
    public int compareTo(Vertex vertex) {
        return name.compareTo(vertex.getName());
    }
}
