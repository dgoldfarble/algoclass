package all;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dgoldfarb on 7/12/14.
 */
public class PrimMinSpanningTree {

    public List<Edge> edges;
    public List<Node> nodes;
    public int num_edges;
    public int num_nodes;

    public PrimMinSpanningTree(String arg) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(arg)));
        String line = reader.readLine();
        num_nodes = Integer.parseInt(line.split(" ")[0]);
        num_edges = Integer.parseInt(line.split(" ")[1]);

        nodes = new ArrayList<Node>(num_nodes);
        for (int i = 0; i < num_nodes; i++) {
            nodes.add(new Node(i));
        }

        edges = new ArrayList<Edge>(num_edges);
        Edge edge;
        while ((line = reader.readLine()) != null) {
            String[] bits = line.split(" ");
            int n1 = Integer.parseInt(bits[0]) - 1;
            int n2 = Integer.parseInt(bits[1]) - 1;
            int cost = Integer.parseInt(bits[2]);
            edge = new Edge(nodes.get(n1), nodes.get(n2), cost);
            edges.add(edge);
            nodes.get(n1).edges.add(edge);
            nodes.get(n2).edges.add(edge);
        }

    }

    public static void main(String[] args) throws IOException {
        PrimMinSpanningTree pmst = new PrimMinSpanningTree(args[0]);
        List<Edge> minSpanningTree = pmst.minSpanningTree();
        int cost = 0;
        for (Edge edge : minSpanningTree) {
            cost += edge.weight;
        }
        System.out.println(cost);
    }

    private List<Edge> minSpanningTree() {
        List<Edge> tree = new ArrayList<Edge>();
        nodes.get(0).explored = true;
        while (!explored()) {
            int lowestCost = Integer.MAX_VALUE;
            Edge bestedge = new Edge(new Node(-1), new Node(-1), Integer.MAX_VALUE);
            for (Node node : nodes) {
                if (node.explored) {
                    for (Edge edge : node.notExplored()) {
                        if (edge.weight < lowestCost) {
                            lowestCost = edge.weight;
                            bestedge = edge;
                        }
                    }
                }
            }
            tree.add(bestedge);
            bestedge.node2.explored = true;
            bestedge.node1.explored = true;
        }
        return tree;
    }

    private boolean explored() {
        for (Node node : nodes) {
            if (!node.explored) {
                return false;
            }
        }
        return true;
    }
}
