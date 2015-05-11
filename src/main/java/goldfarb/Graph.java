package goldfarb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

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
            List<Edge> inEdges = node.getIncomingEdges();
            List<Edge> outEdges = node.getOutgoingEdges();
            node.setIncomingEdges(outEdges);
            node.setOutgoingEdges(inEdges);
        }
        for (Edge edge : edges) {
            Node head = edge.getHead();
            Node tail = edge.getTail();
            edge.setHead(tail);
            edge.setTail(head);
        }
    }

    public void DFS(Node source, boolean reverse) {
        LinkedList<Node> q = new LinkedList<>();
        q.add(0, source);
        while (q.size() > 0) {
            Node v = q.remove(0);
            v.setExplored(true);
            if (reverse) {
                for (Edge e : v.getIncomingEdges()) {
                    if (!e.getTail().isExplored()) {
                        q.add(0, e.getTail());
                    }
                }
            } else {
                for (Edge e : v.getOutgoingEdges()) {
                    if (!e.getHead().isExplored()) {
                        q.add(0, e.getHead());
                    }
                }
            }
        }
    }

    public void DFS(Node source, boolean reverse, int leader, int[][] finishConnected, int[] t) {
        source.setExplored(true);
        finishConnected[1][source.getNodeId()] = leader;


        if (reverse) {
            for (Edge edge : source.getIncomingEdges()) {
                Node node = edge.getTail();
                if (!node.isExplored()) {
                    DFS(node, reverse, leader, finishConnected, t);
                }
            }
        } else {
            for (Edge edge : source.getOutgoingEdges()) {
                Node node = edge.getHead();
                if (!node.isExplored()) {
                    DFS(node, reverse, leader, finishConnected, t);
                }
            }
        }

        finishConnected[0][t[0]] = source.getNodeId();
        t[0]++;
    }

    public int[][] DFS_Loop(Graph g, int[] order, boolean reverse) {
        int[] t = new int[]{0};
        int[][] finishingTimes = new int[2][g.nodes.size()];
        for (int iter = g.nodes.size() - 1; iter >= 0; iter--) {
            int i = order[iter];
            if (!g.nodes.get(i).isExplored()) {
                DFS(g.nodes.get(i), reverse, i, finishingTimes, t);
            }
        }

        return finishingTimes;
    }

    public int[][] connectedGroups() {
        int[] iterOrder = new int[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            iterOrder[i] = i;
        }
        int[][] finishingTime = DFS_Loop(this, iterOrder, true);

        for (int i = 0; i < nodes.size(); i++) {
            nodes.get(i).setExplored(false);
        }
        int[][] connectedComponent = DFS_Loop(this, finishingTime[0], false);

        return connectedComponent;
    }

    public List<Integer> connectedComponents() {
        int[][] connectedComponent = connectedGroups();

        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int i = 0; i < connectedComponent[0].length; i++) {
            int component = connectedComponent[1][i];
            if (map.get(component) != null) {
                map.put(component, map.get(component) + 1);
            } else {
                map.put(component, 1);
            }
        }

        Collection<Integer> unsorted = map.values();
        List<Integer> sorted = new ArrayList<Integer>();
        for (Integer blah : unsorted) {
            sorted.add(blah);
        }
        Collections.sort(sorted);

        return sorted;
    }

    public void readAdjacencyList(File file, boolean directed) throws IOException {
        List<Node> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        int counter = 0;
        while ((line = reader.readLine()) != null) {
            int node1 = Integer.parseInt(line.split(" ")[0]);
            int node2 = Integer.parseInt(line.split(" ")[1]);
            int max = Math.max(node1, node2);
            if (max > nodes.size()) {
                counter = nodes.size();
                for (int i = counter; i < max; i++) {
                    Node node = new Node(i);
                    nodes.add(node);
                }
            }
            Node n1 = nodes.get(node1 - 1);
            Node n2 = nodes.get(node2 - 1);
            Edge edge = new Edge(n1, n2, 1, directed);
            edges.add(edge);
            if (directed) {
                n1.addOutgoingEdge(edge);
                n2.addIncomingEdge(edge);
            } else {
                n1.addEdge(edge);
                n2.addEdge(edge);
            }
            this.nodes = nodes;
            this.edges = edges;
        }
    }
}
