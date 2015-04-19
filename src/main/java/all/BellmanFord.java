package all;

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
    private final Logger LOG = Logger.getLogger(Tree.class);
    List<Edge> edges;
    List<Node> nodes;

    public static void main(String[] args) throws IOException {
        BellmanFord bf = new BellmanFord();
        bf.readEdgeList(args[0]);
        bf.run();
    }

    private void run() {
        LOG.info("Starting Bellman-Ford algorithm");
    }

    private void readEdgeList(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));

        String line = reader.readLine();
        int numNodes = Integer.parseInt(line.split(" ")[0]);
        int numEdges = Integer.parseInt(line.split(" ")[1]);

        nodes = new ArrayList<>();
        for (int i = 0; i < numNodes; i++) {
            nodes.add(new Node(i));
        }

        edges = new ArrayList<>();
        // every line has one edge
        // input list is sorted by tail edge
        while ((line = reader.readLine()) != null) {
            Node tail = nodes.get(Integer.parseInt(line.split(" ")[0]));
            Node head = nodes.get(Integer.parseInt(line.split(" ")[1]));
            int weight = Integer.parseInt(line.split(" ")[2]);
            edges.add(new Edge(tail, head, weight));
        }

        if (numEdges != edges.size()) {
            LOG.error("numEdges != edges.size()");
        }
    }
}
