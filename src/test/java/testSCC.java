import goldfarb.Dickstra;
import goldfarb.Edge;
import goldfarb.Graph;
import goldfarb.Node;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dgoldfarb on 5/9/15.
 */
public class testSCC {

    @Test
    public void testCaseLecture() throws Exception {
        int[] results = new int[]{3, 3, 3};
        Graph graph = new Graph();
        graph.readAdjacencyList(new File("src/test/resources/testCase1.4.lecture.txt"), true);
        List<Integer> connectedComponents = graph.connectedComponents();

        assert(connectedComponents.size() == results.length);
        for (int i = 0; i < connectedComponents.size(); i++) {
            assert(connectedComponents.get(i) == results[i]);
        }
    }

    @Test
    public void testCase1() throws Exception {
        int[] results = new int[]{3, 3, 3};
        Graph graph = new Graph();
        graph.readAdjacencyList(new File("src/test/resources/testCase1.4.1.txt"), true);
        List<Integer> connectedComponents = graph.connectedComponents();
        assert(connectedComponents.size() == results.length);
        for (int i = 0; i < connectedComponents.size(); i++) {
            assert(connectedComponents.get(i) == results[i]);
        }
    }

    @Test
    public void testCase2() throws Exception {
        int[] results = new int[]{2, 3, 3};
        Graph graph = new Graph();
        graph.readAdjacencyList(new File("src/test/resources/testCase1.4.2.txt"), true);
        List<Integer> connectedComponents = graph.connectedComponents();

        Dickstra dickstra = new Dickstra();
        dickstra.readEdgeList("src/test/resources/testCase1.4.2.txt", new String("8"));
        dickstra.connectedComponents();


        assert(connectedComponents.size() == results.length);
        for (int i = 0; i < connectedComponents.size(); i++) {
            assert(connectedComponents.get(i) == results[i]);
        }
    }

    @Test
    public void testCase3() throws Exception {
        int[] results = new int[]{1, 1, 3, 3};
        Graph graph = new Graph();
        graph.readAdjacencyList(new File("src/test/resources/testCase1.4.3.txt"), true);
        List<Integer> connectedComponents = graph.connectedComponents();
        assert(results.length == connectedComponents.size());
        for (int i = 0; i < results.length; i++) {
            assert(connectedComponents.get(connectedComponents.size() - 1 - i) == results[i]);
        }
    }

    // @Ignore
    @Test
    public void assignment() throws Exception {
        int[] results = new int[]{434821, 968, 459, 313, 211};
        Graph graph = new Graph();
        graph.readAdjacencyList(new File("SCC.txt"), true);
        List<Integer> connectedComponents = graph.connectedComponents();
        for (int i = 0; i < results.length; i++) {
            assert(connectedComponents.get(connectedComponents.size() - 1 - i) == results[i]);
        }
    }

    @Test
    public void testeGenerateSizeN() throws Exception {
        for (int i = 6; i < 3000; i = i + 10) {
            Graph graph = createConnectedSizeN(i);
            List<Integer> connected = graph.connectedComponents();
            assert (connected.size() == 1);
        }
    }

    private Graph createConnectedSizeN(int n) {
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            nodes.add(new Node(i));
        }

        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < n - 2; i = i + 4) {
            Node x1 = nodes.get(i);
            Node notX1 = nodes.get(i + 1);
            Node x2 = nodes.get(i + 2);
            Node notX2 = nodes.get(i + 3);
            Edge edge1 = new Edge(x1, x2, 1, true);
            Edge edge2 = new Edge(notX2, notX1, 1, true);
            edges.add(edge1);
            edges.add(edge2);
        }
        Node x1 = nodes.get(0);
        Node x2 = nodes.get(n - 4);
        Node notX1 = nodes.get(1);
        Node notX2 = nodes.get(n - 3);
        Edge edge1 = new Edge(x2, notX1, 1, true);
        Edge edge2 = new Edge(x1, notX2, 1, true);
        edges.add(edge1);
        edges.add(edge2);

        x2 = nodes.get(n - 2);
        notX2 = nodes.get(n - 1);
        edge1 = new Edge(notX1, x2, 1, true);
        edge2 = new Edge(notX2, x1, 1, true);
        edges.add(edge1);
        edges.add(edge2);

        edge1 = new Edge(x2, x1, 1, true);
        edge2 = new Edge(notX1, notX2, 1, true);
        edges.add(edge1);
        edges.add(edge2);

        Graph graph = new Graph(edges, nodes);
        return graph;
    }
}
