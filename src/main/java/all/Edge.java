package all;

/**
 * Created by dgoldfarb on 7/16/14.
 */
public class Edge {
    private Node tail;
    private Node head;
    private boolean directed;
    private int weight;
    public Edge(Node tail, Node head, int weight, boolean directed) {
        this.tail = tail;
        this.head = head;
        this.weight = weight;
        this.directed = directed;
    }

    public Node getHead() {
        return head;
    }

    public Node getTail() {
        return tail;
    }

    public boolean isDirected() {
        return directed;
    }

    public int getWeight() {
        return weight;
    }
}
