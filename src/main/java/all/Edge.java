package all;

/**
 * Created by dgoldfarb on 7/16/14.
 */
public class Edge {
    Node tail;
    Node head;
    int weight;
    public Edge(Node tail, Node head, int weight) {
        this.tail = tail;
        this.head = head;
        this.weight = weight;
    }
}
