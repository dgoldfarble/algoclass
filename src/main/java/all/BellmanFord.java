package all;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dgoldfarb on 4/19/15.
 */
public class BellmanFord {
    private final Logger LOG = Logger.getLogger(BellmanFord.class);
    List<Edge> edges;
    List<Node> nodes;

    public static void main(String[] args) throws IOException {
        BellmanFord bf = new BellmanFord();
        bf.LOG.setLevel(Level.DEBUG);
        bf.readEdgeList(args[0]);
        bf.run();
    }

    private void readDijkstraList(String fileName) throws IOException {
        nodes = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            nodes.add(new Node(i));
        }

        BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
        String line;
        while ((line = reader.readLine()) != null) {
            int node1 = Integer.parseInt(line.split("\t")[0]);
            for (String bit : line.split("\t")) {
                if (!bit.contains(",")) {
                    continue;
                }
                int node2 = Integer.parseInt(bit.split(",")[0]);
                int weight = Integer.parseInt(bit.split(",")[1]);
                edges.add(new Edge(nodes.get(node1), nodes.get(node2), weight, true));
            }
        }
    }

    private void run() {
        LOG.info("Starting Bellman-Ford algorithm");
        // LOG.info("")

        int shortestPath = Integer.MAX_VALUE;
        boolean cycleDetected = false;
        // pick first node as source vertex
        for (Node sourceNode : nodes) {
            LOG.debug("Source: " + sourceNode.getNodeId() + " / " + nodes.size());

            int[][] A = new int[nodes.size() + 1][nodes.size()];
            for (int i = 0; i < nodes.size(); i++) {
                if (i == sourceNode.getNodeId()) {
                    A[0][i] = 0;
                } else {
                    A[0][i] = Integer.MAX_VALUE;
                }
            }

            for (int i = 1; i <= nodes.size(); i++) {
                for (Node v : nodes) {
                    // take the minimum of {A[i - 1][v.getNodeId()], (the incoming edge that has the smallest (source weight + distance))}
                    int costOfBestIncomingEdge = Integer.MAX_VALUE;
                    for (Edge e : v.getIncomingEdges()) {
                        Node source = e.getTail();
                        if (A[i - 1][source.getNodeId()] < Integer.MAX_VALUE &&
                                A[i - 1][source.getNodeId()] + e.getWeight() < costOfBestIncomingEdge) {
                            costOfBestIncomingEdge = A[i - 1][source.getNodeId()] + e.getWeight();
                        }
                    }
                    A[i][v.getNodeId()] = Math.min(A[i - 1][v.getNodeId()], costOfBestIncomingEdge);
                }
            }

            for (int i = 0; i < nodes.size(); i++) {
                if (A[nodes.size() - 1][i] != A[nodes.size()][i]) {
                    cycleDetected = true;
                }
                if (A[nodes.size()][i] < shortestPath) {
                    shortestPath = A[nodes.size()][i];
                    LOG.debug(sourceNode.getNodeId() + " " + i + " " + shortestPath);
                }
            }
        }

        if (cycleDetected) { LOG.error("cycle detected"); }

        LOG.info("The shortest shortest path is " + shortestPath);
    }

    private void readEdgeList(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));

        String line = reader.readLine();
        int numNodes = Integer.parseInt(line.split(" ")[0]);
        int numEdges = Integer.parseInt(line.split(" ")[1]);
        LOG.debug("Nodes: " + numNodes + " Edges: " + numEdges);

        nodes = new ArrayList<>();
        for (int i = 0; i < numNodes; i++) {
            nodes.add(new Node(i));
        }

        edges = new ArrayList<>();
        // every line has one edge
        // input list is sorted by tail edge
        while ((line = reader.readLine()) != null) {
            Node tail = nodes.get(Integer.parseInt(line.split(" ")[0]) - 1);
            Node head = nodes.get(Integer.parseInt(line.split(" ")[1]) - 1);
            int weight = Integer.parseInt(line.split(" ")[2]);
            Edge edge = new Edge(tail, head, weight, true);
            tail.addOutgoingEdge(edge);
            head.addIncomingEdge(edge);
            edges.add(edge);
            LOG.debug(tail.getNodeId() + " " + head.getNodeId() + " " + weight);
        }

        if (numEdges != edges.size()) {
            LOG.error("numEdges != edges.size()");
        }
    }
}
