import goldfarb.Dickstra;
import goldfarb.Graph;
import goldfarb.Node;
import org.junit.Test;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by dgoldfarb on 5/10/15.
 */
public class testGraph {
    String[] fileList = new String[]{"src/test/resources/testCase6.1.satisfiable.txt",
            "src/test/resources/testCase6.2.txt",
            "src/test/resources/testCase6.3.txt"};

    @Test
    public void testDFS1() throws Exception {
        Dickstra dickstra = new Dickstra();
        dickstra.readEdgeList("src/test/resources/testCase1.4.1.txt", "9");
        int[][] finishingTimes = new int[2][dickstra.size];
        dickstra.explored = new boolean[dickstra.size];
        dickstra.DFS(dickstra.reverseEdges, 9, finishingTimes);

        System.out.println("  ");

        int[][] finishingTimes2 = new int[2][dickstra.size];
        int[] t = new int[]{0};
        Graph graph = new Graph();
        graph.readAdjacencyList(new File("src/test/resources/testCase1.4.1.txt"), true);
        Node node = graph.nodes.get(graph.nodes.size() - 1);
        graph.DFS(node, true, graph.nodes.size() - 1, finishingTimes2, t);

        for (int i = 0; i < dickstra.size; i++) {
            if (dickstra.explored[i] != graph.nodes.get(i).isExplored()) {
                System.out.println("what");
            }
        }
    }

    @Test
    public void testDFS2() throws Exception {
        Dickstra dickstra = new Dickstra();
        dickstra.readEdgeList("src/test/resources/testCase1.4.2.txt", "8");
        int[][] finishingTimes = new int[2][dickstra.size];
        dickstra.explored = new boolean[dickstra.size];
        dickstra.DFS(dickstra.reverseEdges, 8, finishingTimes);

        System.out.println("  ");

        int[][] finishingTimes2 = new int[2][dickstra.size];
        int[] t = new int[]{0};
        Graph graph = new Graph();
        graph.readAdjacencyList(new File("src/test/resources/testCase1.4.2.txt"), true);
        Node node = graph.nodes.get(graph.nodes.size() - 1);
        graph.DFS(node, true, graph.nodes.size() - 1, finishingTimes2, t);

        for (int i = 0; i < dickstra.size; i++) {
            if (dickstra.explored[i] != graph.nodes.get(i).isExplored()) {
                System.out.println("what");
            }
        }
    }

    @Test
    public void testDFS2sat() throws Exception {
        Dickstra dickstra = new Dickstra();
        dickstra.readEdgeList("testCase7.txt", "66702");

        List<Integer> finishingTimes = dickstra.connectedComponents();


        Graph graph = new Graph();
        graph.readAdjacencyList(new File("testCase7.txt"), true);
        List<Integer> finishingTimes2 = graph.connectedComponents();


        for (int i = 0; i < finishingTimes.size(); i++) {
            System.out.println(finishingTimes.get(i) + " " + finishingTimes2.get(i));
        }
    }
}