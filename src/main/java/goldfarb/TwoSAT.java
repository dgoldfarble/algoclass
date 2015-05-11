package goldfarb;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;

/**
 * Created by dgoldfarb on 5/1/15.
 */
public class TwoSAT {
    private final Logger LOG = Logger.getLogger(TwoSAT.class);
    Graph graph;

    public TwoSAT(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(file)));
        String line = reader.readLine();
        int numEdges = 2 * Integer.parseInt(line.split(" ")[0]);
        int numNodes = numEdges;
        // (1, -8) pair will give you edges (-1 => -8) and (8 => 1)

        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < numNodes; i++) {
            Node node = new Node(i);
            nodes.add(node);
        }

        List<Edge> edges = new ArrayList<>();
        while ((line = reader.readLine()) != null){
            int x1 = Integer.parseInt(line.split(" ")[0]);
            int x2 = Integer.parseInt(line.split(" ")[1]);
            int max = Math.max(Math.abs(x1) * 2, Math.abs(x2) * 2);
            if (max > nodes.size()) {
                for (int i = nodes.size(); i < max; i++) {
                    Node node = new Node(i);
                    nodes.add(node);
                }
            }


            Node nx1;
            Node nx2;
            Node notX1;
            Node notX2;
            if (x1 < 0 && x2 < 0) {
                x1 = Math.abs(x1) * 2 - 1;
                x2 = Math.abs(x2) * 2 - 1;
                nx1 = nodes.get(x1 - 1);
                notX1 = nodes.get(x1);
                nx2 = nodes.get(x2 - 1);
                notX2 = nodes.get(x2);
                Edge edge = new Edge(nx1, notX2, 1, true);
                Edge edge1 = new Edge(nx2, notX1, 1, true);
                edges.add(edge);
                edges.add(edge1);
                nx1.addOutgoingEdge(edge);
                notX2.addIncomingEdge(edge);
                nx2.addOutgoingEdge(edge1);
                notX1.addIncomingEdge(edge1);
            } else if (x1 < 0 && x2 > 0) {
                x1 = Math.abs(x1) * 2 - 1;
                x2 = (x2 - 1) * 2;
                nx1 = nodes.get(x1 - 1);
                notX1 = nodes.get(x1);
                nx2 = nodes.get(x2);
                notX2 = nodes.get(x2 + 1);
                Edge edge = new Edge(nx1, nx2, 1, true);
                Edge edge1 = new Edge(notX2, notX1, 1, true);
                edges.add(edge);
                edges.add(edge1);
                nx1.addOutgoingEdge(edge);
                nx2.addIncomingEdge(edge);
                notX2.addOutgoingEdge(edge1);
                notX1.addIncomingEdge(edge1);
            } else if (x1 > 0 && x2 < 0) {
                x1 = (x1 - 1) * 2;
                x2 = Math.abs(x2) * 2 - 1;
                nx1 = nodes.get(x1);
                notX1 = nodes.get(x1 + 1);
                nx2 = nodes.get(x2 - 1);
                notX2 = nodes.get(x2);
                Edge edge = new Edge(notX1, notX2, 1, true);
                Edge edge1 = new Edge(nx2, nx1, 1, true);
                edges.add(edge);
                edges.add(edge1);
                notX1.addOutgoingEdge(edge);
                notX2.addIncomingEdge(edge);
                nx2.addOutgoingEdge(edge1);
                nx1.addIncomingEdge(edge1);
            } else {
                x1 = (x1 - 1) * 2;
                x2 = (x2 - 1) * 2;
                nx1 = nodes.get(x1);
                notX1 = nodes.get(x1 + 1);
                nx2 = nodes.get(x2);
                notX2 = nodes.get(x2 + 1);
                Edge edge = new Edge(notX1, nx2, 1, true);
                Edge edge1 = new Edge(notX2, nx1, 1, true);
                edges.add(edge);
                edges.add(edge1);
                notX1.addOutgoingEdge(edge);
                nx2.addIncomingEdge(edge);
                notX2.addOutgoingEdge(edge1);
                nx1.addIncomingEdge(edge1);
            }
        }

        this.graph = new Graph(edges, nodes);
    }

    public TwoSAT() {

    }

    public void setGraph(Graph g) {
        this.graph = g;
    }

    public static void main(String[] args) throws IOException {
        for (int i = 0; i < args.length; i++) {
            TwoSAT sat = new TwoSAT(args[i]);
            sat.run();
        }
    }

    /*
    private void preproccess() {
        List<Integer> removed = new ArrayList<>();
        for (Clause clause : clauses) {
            List<Integer> contradictx1 = index.get(clause.x1);
            List<Integer> contradictx2 = index.get(clause.x2);
            if (index.get(clause.x1).size() == 1
                    && index.get(clause.x2).size() == 1) {

            }
        }
    }*/

    public boolean run() {
        int[][] comps = graph.connectedGroups();
        // List<Integer> connected = graph.connectedComponents();
        for (int i = 0; i < comps[0].length; i = i + 2) {
            if (comps[1][i] == comps[1][i + 1]) {
                LOG.info("Unsatisfiable");
                return false;
            }
        }
        LOG.info("Satisfiable");
        return true;
    }

    public void dumpGraph(File outfile) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(outfile, "UTF-8");
        writer.println(graph.nodes.size() + " " + graph.edges.size());
        for (Edge edge : graph.edges) {
            Node n1 = edge.getTail();
            Node n2 = edge.getHead();
            int x1;
            int x2;
            if (n1.getNodeId() % 2 == 0) {
                x1 = n1.getNodeId() / 2;
            } else {
                x1 = -1 * (n1.getNodeId() - 1) / 2;
            }
            if (n2.getNodeId() % 2 == 0) {
                x2 = n2.getNodeId() / 2;
            } else {
                x2 = -1 * (n2.getNodeId() - 1) / 2;
            }
            writer.println((n1.getNodeId() + 1) + " " + (n2.getNodeId() + 1));
        }
        writer.close();
    }
}

