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

    public static void readEdgeList(File file) {

    }

    private Graph readEdgeAndDistance(String fileName, int num_nodes) throws IOException {
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
}
