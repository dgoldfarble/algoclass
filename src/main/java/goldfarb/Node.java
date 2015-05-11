package goldfarb;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by dgoldfarb on 7/16/14.
 */
public class Node {
    private int nodeId;
    private boolean explored;
    private List<Edge> inEdges;
    private List<Edge> outEdges;

    public Node(int i) {
        this.nodeId = i;
        this.explored = false;
        this.inEdges = new ArrayList<>();
        this.outEdges = new ArrayList<>();
    }

    public int getNodeId() {
        return nodeId;
    }

    public boolean isExplored() {
        return explored;
    }

    public void setExplored(boolean explored) {
        this.explored = explored;
    }

    public void addEdge(Edge edge) {
        inEdges.add(edge);
        outEdges.add(edge);
    }

    public void addIncomingEdge(Edge edge) {
        inEdges.add(edge);
    }

    public void addOutgoingEdge(Edge edge) {
        outEdges.add(edge);
    }

    public List<Edge> getIncomingEdges() {
        return inEdges;
    }

    public List<Edge> getOutgoingEdges() {
        return outEdges;
    }

    public Set<Edge> getEdgesToUnexploredNeighbors() {
        Set<Edge> returnEdges = new HashSet<>();
        for (Edge e : outEdges) {
            if (e.isDirected()) {
                Node v = e.getHead();
                if (!v.equals(this) && !v.isExplored()) {
                    returnEdges.add(e);
                }
            } else {
                Node v1 = e.getHead();
                Node v2 = e.getTail();
                if (!(v1.equals(this) || v2.equals(this))) {
                    System.err.println("Neither node matches this edge");
                } else {
                    if (!v1.equals(this) && !v1.isExplored()) {
                        returnEdges.add(e);
                    } else if (!v2.isExplored()) {
                        returnEdges.add(e);
                    }
                }
            }
        }
        return returnEdges;
    }

    public Set<Node> getUnexploredNeighbors() {
        return getUnexploredNeighbors(false);
    }

    // supports reverse DFS
    public Set<Node> getUnexploredNeighbors(boolean backward) {
        if (backward) {
            Set<Node> returnNodes = new HashSet<>();
            for (Edge e : inEdges) {
                if (e.isDirected()) {
                    Node v = e.getTail();
                    assert (!v.equals(this));
                    if (!v.isExplored()) {
                        returnNodes.add(v);
                    }
                } else {
                    Node v1 = e.getHead();
                    Node v2 = e.getTail();
                    assert (v1.equals(this) || v2.equals(this));
                    if (!v1.equals(this) && !v1.isExplored()) {
                        returnNodes.add(v1);
                    } else if (!v2.isExplored()) {
                        returnNodes.add(v2);
                    }
                }
            }
            return returnNodes;
        } else {
            Set<Node> returnNodes = new HashSet<>();
            for (Edge e : outEdges) {
                if (e.isDirected()) {
                    Node v = e.getHead();
                    assert (!v.equals(this));
                    if (!v.isExplored()) {
                        returnNodes.add(v);
                    }
                } else {
                    Node v1 = e.getHead();
                    Node v2 = e.getTail();
                    assert (v1.equals(this) || v2.equals(this));
                    if (!v1.equals(this) && !v1.isExplored()) {
                        returnNodes.add(v1);
                    } else if (!v2.isExplored()) {
                        returnNodes.add(v2);
                    }
                }
            }
            return returnNodes;
        }
    }

    public Node copy() {
        Node node = new Node(this.getNodeId());
        node.explored = false;
        return node;
    }

    public void setIncomingEdges(List<Edge> incomingEdges) {
        this.inEdges = incomingEdges;
    }

    public void setOutgoingEdges(List<Edge> outgoingEdges) {
        this.outEdges = outgoingEdges;
    }
}
