package Graph;

import java.io.Serializable;

/**
 * Created by CONNER on 11/2/2015.
 */
public abstract class Vertex implements Comparable<Vertex>, Serializable{

    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        Vertex vertex = (Vertex) o;

        return name.equals(vertex.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
