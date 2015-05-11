import goldfarb.Edge;
import goldfarb.Graph;
import goldfarb.Node;
import goldfarb.TwoSAT;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dgoldfarb on 5/9/15.
 */
public class testTwoSAT {

    @Test
    public void testCase1() throws Exception {
        TwoSAT twoSAT = new TwoSAT(new File("src/test/resources/testCase6.1.satisfiable.txt").getAbsolutePath());
        assert(twoSAT.run());
    }

    @Test
    public void testCase2() throws Exception {
        TwoSAT twoSAT = new TwoSAT(new File("src/test/resources/testCase6.2.unsatisfiable.txt").getAbsolutePath());
        assert(!twoSAT.run());
    }

    @Test
    public void testCase3() throws Exception {
        TwoSAT twoSAT = new TwoSAT(new File("src/test/resources/testCase6.3.satisfiable.txt").getAbsolutePath());
        assert(twoSAT.run());
    }

    @Test
    public void testCase4() throws Exception {
        TwoSAT twoSAT = new TwoSAT(new File("src/test/resources/testCase6.4.unsatisfiable.txt").getAbsolutePath());
        assert(!twoSAT.run());
    }

    @Test
    public void testCase5() throws Exception {
        TwoSAT twoSAT = new TwoSAT(new File("src/test/resources/testCase6.5.satisfiable.txt").getAbsolutePath());
        assert(twoSAT.run());
    }

    @Test
    public void testCase6() throws Exception {
        TwoSAT twoSAT = new TwoSAT(new File("src/test/resources/testCase6.6.satisfiable.txt").getAbsolutePath());
        assert(twoSAT.run());
    }

    @Test
    public void testCase7() throws Exception {
        TwoSAT twoSAT = new TwoSAT(new File("src/test/resources/testCase6.7.unsatisfiable.txt").getAbsolutePath());
        twoSAT.dumpGraph(new File("testCase7.txt"));
        assert(!twoSAT.run());
    }

    @Test
    public void testCase8() throws Exception {
        TwoSAT twoSAT = new TwoSAT(new File("src/test/resources/testCase6.8.satisfiable.txt").getAbsolutePath());
        assert(twoSAT.run());
    }

    @Test
    public void testCase9() throws Exception {
        TwoSAT twoSAT = new TwoSAT(new File("src/test/resources/testCase6.9.unsatisfiable.txt").getAbsolutePath());
        assert(!twoSAT.run());
    }

    @Test
    public void testGenerateFailureCase() throws Exception {
        for (int i = 6; i < 3000; i = i + 10000) {
            TwoSAT twoSAT = new TwoSAT();
            twoSAT.setGraph(createUnsatisfiableSizeN(i));
            assert(!twoSAT.run());
        }
    }

    private Graph createUnsatisfiableSizeN(int n) {
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
