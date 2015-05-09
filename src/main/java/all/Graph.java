package all;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dgoldfarb on 8/9/14.
 */
public class Graph {
    public List<Edge> edges;
    public List<Node> nodes;

    public Graph() {
        edges = new ArrayList<>();
        nodes = new ArrayList<>();
    }

    public Graph(List<Edge> edgeses, List<Node> nodes) {
        this.edges = edgeses;
        this.nodes = nodes;
    }

    public Graph(String arg) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(arg)));
        String line = reader.readLine();
        int num_nodes;
        int num_edges;
        if (line.split(" ").length == 2) {
            num_nodes = Integer.parseInt(line.split(" ")[0]);
            num_edges = Integer.parseInt(line.split(" ")[1]);
        } else {
            assert(line.split(" ").length == 1);
            num_edges = 0;
            num_nodes = Integer.parseInt(line.split(" ")[0]);
        }

        nodes = new ArrayList<Node>();
        for (int i = 0; i < num_nodes; i++) {
            nodes.add(new Node(i));
        }

        edges = new ArrayList<Edge>(num_edges);
        Edge edge;
        while ((line = reader.readLine()) != null) {
            String[] bits = line.split(" ");
            int n1 = Integer.parseInt(bits[0]) - 1;
            int n2 = Integer.parseInt(bits[1]) - 1;
            int cost = 1;
            if (bits.length == 3) {
                cost = Integer.parseInt(bits[2]);
            }
            edge = new Edge(nodes.get(n1), nodes.get(n2), cost, false);
            edges.add(edge);
            nodes.get(n1).addEdge(edge);
            nodes.get(n2).addEdge(edge);
        }
    }

    public Graph readEdgeAndDistance(String fileName, int num_nodes) throws IOException {
        List<Edge> edges = new ArrayList<>();
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < num_nodes; i++) {
            nodes.add(new Node(i));
        }

        BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
        String line;
        while ((line = reader.readLine()) != null) {

            String[] bits = line.split("\t");
            Node sourceNode = nodes.get(Integer.parseInt(bits[0]) - 1);
            for (int i = 1; i < bits.length; i++) {
                Node destinationNode = nodes.get(Integer.parseInt(bits[i].split(",")[0]));
                Edge edge = new Edge(sourceNode, destinationNode, Integer.parseInt(bits[i].split(",")[1]), false);
                sourceNode.addEdge(edge);
                destinationNode.addEdge(edge);
                edges.add(edge);
            }
        }

        Graph graph = new Graph(edges, nodes);
        return graph;
    }

    public Graph copy() {
        List<Node> nodesCopy = new ArrayList<>();
        for (Node node : nodes) {
            nodesCopy.add(node.copy());
        }

        List<Edge> edgesCopy = new ArrayList<>();
        for (Edge edge : edges) {
            Node head = nodesCopy.get(edge.getHead().getNodeId());
            Node tail = nodesCopy.get(edge.getTail().getNodeId());
            boolean directed = edge.isDirected();
            int weight = edge.getWeight();

            Edge edgeCopy = new Edge(tail, head, weight, directed);
            edgesCopy.add(edgeCopy);
            if (directed) {
                tail.addOutgoingEdge(edgeCopy);
                head.addIncomingEdge(edgeCopy);
            } else {
                tail.addEdge(edgeCopy);
                head.addEdge(edgeCopy);
            }
        }
        Graph graph = new Graph(edgesCopy, nodesCopy);
        return graph;
    }

    public void reverse() {
        for (Node node : nodes) {
            List<Edge> incomingEdges = node.getIncomingEdges();
            List<Edge> outgoingEdges = node.getOutgoingEdges();
        }
        for (Edge edge : edges) {
            Node head = edge.getHead();
            Node tail = edge.getTail();
            edge.setHead(tail);
            edge.setTail(head);
        }
    }

    public void DFS(Node source) {
        source.setExplored(true);
        for (Node destination : source.getUnexploredNeighbors()) {
            DFS(destination);
        }
    }
}
