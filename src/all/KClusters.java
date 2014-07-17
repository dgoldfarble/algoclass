package all;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dgoldfarb on 7/16/14.
 */
public class KClusters {

    public List<Edge> edges;
    public List<Node> nodes;
    public int num_nodes;
    public int num_clusters;
    public int[] clusters;


    public static final int k = 4;

    public KClusters(String arg) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(arg)));
        String line = reader.readLine();
        num_nodes = Integer.parseInt(line);
        num_clusters = num_nodes;
        clusters = new int[num_nodes];

        nodes = new ArrayList<Node>(num_nodes);
        for (int i = 0; i < num_nodes; i++) {
            nodes.add(new Node(i));
            clusters[i] = i;
        }

        edges = new ArrayList<Edge>();
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
        KClusters kClusters = new KClusters(args[0]);
        kClusters.run();
        kClusters.print();
    }

    private void print() {
        for (int i = 0; i < num_nodes; i++) {
            System.out.println(clusters[i]);
        }
    }

    private void run() {
        Edge smallest_edge;
        while (num_clusters > k) {
            // find closest pair of nodes in a different cluster
            smallest_edge = new Edge(new Node(6), new Node(6), Integer.MAX_VALUE);
            for (Edge edge : edges) {
                if (clusters[edge.node1.nodeId] != clusters[edge.node2.nodeId]
                        && edge.weight < smallest_edge.weight) {
                    smallest_edge = edge;
                }
            }
            // merge the clusters
            int cluster1 = clusters[smallest_edge.node1.nodeId];
            int cluster2 = clusters[smallest_edge.node2.nodeId];
            for (int i = 0; i < num_nodes; i++) {
                if (clusters[i] == cluster2) {
                    clusters[i] = cluster1;
                }
            }
            num_clusters--;
        }
        int distance = Integer.MAX_VALUE;
        for (Edge edge : edges) {
            if (clusters[edge.node1.nodeId] != clusters[edge.node2.nodeId]
                    && edge.weight < distance) {
                distance = edge.weight;
            }
        }
        System.out.println(distance);
    }
}
