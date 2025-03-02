package goldfarb;

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
        if (directed) {
            tail.addOutgoingEdge(this);
            head.addIncomingEdge(this);
        } else {
            tail.addEdge(this);
            head.addEdge(this);
        }
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

    public void setHead(Node head) {
        this.head = head;
    }

    public void setTail(Node tail) {
        this.tail = tail;
    }
}
