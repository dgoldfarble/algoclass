package all;

/**
 * Created by dgoldfarb on 7/16/14.
 */
public class Edge {
    Node node1;
    Node node2;
    int weight;
    public Edge(Node node1, Node node2, int weight) {
        this.node1 = node1;
        this.node2 = node2;
        this.weight = weight;
    }
}
