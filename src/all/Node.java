package all;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dgoldfarb on 7/16/14.
 */
public class Node {
    int nodeId;
    List<Edge> edges;
    boolean explored;
    public Node(int i) {
        this.nodeId = i;
        this.explored = false;
        edges = new ArrayList<Edge>();
    }

    public List<Edge> notExplored() {
        List<Edge> returnEdges = new ArrayList<Edge>();
        for (Edge edge : edges) {
            if (this == edge.node1 && !edge.node2.explored) {
                returnEdges.add(edge);
            } else if (!edge.node1.explored) {
                returnEdges.add(edge);
            }
        }
        return returnEdges;
    }

    public int distance;
}
