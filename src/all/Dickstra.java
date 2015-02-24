package all;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by dgoldfarb on 6/1/14.
 */
public class Dickstra {

    private List<List<Integer>> edges;
    private List<List<Integer>> reverseEdges;
    private List<List<Integer>> distances;
    private int size;
    private boolean[] explored;
    private int[] shortestPath;
    private String[] path;
    private int t;
    private int s;

    public Dickstra() {
        edges = new ArrayList<List<Integer>>();
        reverseEdges = new ArrayList<List<Integer>>();
        distances = new ArrayList<List<Integer>>();
    }

    public static void main(String[] args) throws IOException {
        boolean debug = false;
        Dickstra dickstra = new Dickstra();

        dickstra.readEdgeAndDistance(args[0]);
        dickstra.dijkstra();

        /*
        dickstra.readEdgeList(args[0], args[1]);
        dickstra.connectedComponents();
        */

        /*
        dickstra.readAdjacencyList(args[0]);
        int minCut = dickstra.minCut(debug);
        */
    }

    private void dijkstra() {
        explored = new boolean[size];
        shortestPath = new int[size];
        path = new String[size];
        explored[0] = true;
        while (!explored()) {
            int minScore = Integer.MAX_VALUE;
            int nodeTail = -1;
            int nodeHead = -1;
            for (int i = 0; i < size; i++) {
                if (explored[i]) {
                    for (int j = 0; j < edges.get(i).size(); j++) {
                        if (!explored[edges.get(i).get(j)]) {
                            if (shortestPath[i] + distances.get(i).get(j) < minScore) {
                                minScore = shortestPath[i] + distances.get(i).get(j);
                                nodeTail = i;
                                nodeHead = edges.get(i).get(j);
                            }
                        }
                    }
                }
            }
            if (nodeHead != -1) {
                explored[nodeHead] = true;
                shortestPath[nodeHead] = minScore;
                path[nodeHead] = new String(path[nodeTail] + Integer.toString(nodeTail));
            }
        }
        int[] solution = new int[]{7,37,59,82,99,115,133,165,188,197};
        for (Integer i : solution) {
            System.out.println(shortestPath[i - 1]);
        }
    }

    private boolean explored() {
        for (int i = 0; i < size; i++) {
            if (!explored[i]) {
                return false;
            }
        }
        return true;
    }

    private void readEdgeAndDistance(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
        String line;
        while ((line = reader.readLine()) != null) {
            List<Integer> localEdgeList = new ArrayList<Integer>();
            List<Integer> localDistanceList = new ArrayList<Integer>();
            String[] bits = line.split("\t");
            localEdgeList.add(Integer.parseInt(bits[0]) - 1);
            localDistanceList.add(Integer.parseInt(bits[0]) - 1);
            for (int i = 1; i < bits.length; i++) {
                localEdgeList.add(Integer.parseInt(bits[i].split(",")[0]) - 1);
                localDistanceList.add(Integer.parseInt(bits[i].split(",")[1]));
            }
            edges.add(localEdgeList);
            distances.add(localDistanceList);
            size = edges.size();
        }
    }

    private void connectedComponents() {

        int[] iterOrder = new int[size];
        for (int i = 0; i < size; i++) {
            iterOrder[i] = i + 1;
        }
        // run DFS_Loop on Grev to find finishing Times
        int[][] finishingTime = DFS_Loop(reverseEdges, iterOrder);



        int[][] connected_component =  DFS_Loop(edges, finishingTime[0]);

        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int i = 0; i < connected_component[0].length; i++) {
            int component = connected_component[1][i];
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

        for (int i = 0; i < sorted.size(); i++) {
            System.out.println(sorted.get(i));
        }
    }

    // DFS_Loop should return an n x 2 array
    private int[][] DFS_Loop(List<List<Integer>> edgeList, int[] order) {
        t = 0;

        explored = new boolean[size];
        int[][] finishingTimes_connectedComponents = new int[2][size];
        for (int iter = size; iter > 0; iter--) {
            int i = order[iter - 1];
            if (!explored[i - 1]) {
                s = edgeList.get(i - 1).get(0);
                DFS(edgeList, s, finishingTimes_connectedComponents);
            }
        }

        return finishingTimes_connectedComponents;
    }

    private void DFS(List<List<Integer>> edgeList, int i, int[][] finishingTimes_connectedComponents) {
        explored[i - 1] = true;
        finishingTimes_connectedComponents[1][i - 1] = s;

        List<Integer> nodesFromi = edgeList.get(i - 1);
        for (int node = 1; node < nodesFromi.size(); node++) {
            int j = nodesFromi.get(node);
            if (!explored[j - 1]) {
                DFS(edgeList, j, finishingTimes_connectedComponents);
            }
        }
        t++;
        finishingTimes_connectedComponents[0][t - 1] = i;
    }

    private void readEdgeList(String fileName, String numberOfVertices) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));

        size = Integer.parseInt(numberOfVertices);

        List<Integer> localIntList;
        for (int i = 0; i < size; i++) {
            localIntList = new ArrayList<Integer>();
            localIntList.add(i + 1);
            edges.add(localIntList);
        }
        for (int i = 0; i < size; i++) {
            localIntList = new ArrayList<Integer>();
            localIntList.add(i + 1);
            reverseEdges.add(localIntList);
        }

        // every line has one edge
        // input list is sorted by tail edge
        String line;
        while ((line = reader.readLine()) != null) {
            int tail = Integer.parseInt(line.split(" ")[0]);
            int head = Integer.parseInt(line.split(" ")[1]);

            localIntList = edges.get(tail - 1);
            localIntList.add(head);
            edges.set(tail - 1, localIntList);

            localIntList = reverseEdges.get(head - 1);
            localIntList.add(tail);
            reverseEdges.set(head - 1, localIntList);
        }
    }

    private int minCut(boolean debug) {
        int larger;
        int smaller;
        // upper bound
        int bestCutSize = this.edges.size() * (this.edges.size() - 1) / 2;
        System.out.println("Worst case bestCutSize: " + bestCutSize);

        Random random = new Random();

        for (int j = 0; j < 4000; j++) {
            Dickstra dickstraCopy = this.copy();
            for (int i = dickstraCopy.edges.size(); i > 2; i--) {
                larger = random.nextInt(i);
                List<Integer> templist = dickstraCopy.edges.get(larger);
                smaller = larger;
                while (smaller == larger) {
                    smaller = templist.get(random.nextInt(dickstraCopy.edges.get(larger).size() - 1) + 1);
                }
                if (smaller > larger) {
                    int temp = smaller;
                    smaller = larger;
                    larger = temp;
                }

                if (debug) {
                    System.out.print("Size: " + dickstraCopy.edges.size() + " ");
                    System.out.print("Meld " + smaller + " + " + larger + "\n");

                    dickstraCopy.printAdjacencyList(smaller, smaller + 1);
                    dickstraCopy.printAdjacencyList(larger, larger + 1);
                }
                dickstraCopy.contract(smaller, larger);
                if (debug) {
                    dickstraCopy.printAdjacencyList(smaller, smaller + 1);
                }
            }
            if (dickstraCopy.edges.get(0).size() - 1 < bestCutSize) {
                bestCutSize = dickstraCopy.edges.get(0).size() - 1;
            }

            if (dickstraCopy.edges.get(0).size() != dickstraCopy.edges.get(1).size()) {
                System.out.println("Is this allowed?");
            }

            System.out.println("Final node");
            if (debug) {
                dickstraCopy.printAdjacencyList(0, 2);
            }
            System.out.println(dickstraCopy.edges.get(0).size() - 1 + " best so far: " + bestCutSize);
        }
        return bestCutSize;
    }

    private Dickstra copy() {
        Dickstra that = new Dickstra();
        List<List<Integer>> newAdjacencies = new ArrayList<List<Integer>>();
        for (int i = 0; i < edges.size(); i++) {
            List<Integer> localAdjacencies = new ArrayList<Integer>();
            for (int j = 0; j < edges.get(i).size(); j++) {
                localAdjacencies.add(edges.get(i).get(j));
            }
            newAdjacencies.add(localAdjacencies);
        }
        that.edges = newAdjacencies;
        return that;
    }

    private Dickstra fakeGraph() {
        Dickstra returnDickstra = new Dickstra();
        List<Integer> row = new ArrayList<Integer>();
        row.add(0);
        row.add(1);
        row.add(2);
        returnDickstra.edges.add(row);
        row = new ArrayList<Integer>();
        row.add(1);
        row.add(0);
        row.add(2);
        returnDickstra.edges.add(row);
        row = new ArrayList<Integer>();
        row.add(2);
        row.add(1);
        row.add(0);
        returnDickstra.edges.add(row);
        return returnDickstra;
    }

    private void printAdjacencyList(int start, int end) {
        for (int i = start; i < end; i++ ){
            System.out.println(edges.get(i));
        }
    }

    private void readAdjacencyList(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
        String line;
        while ((line = reader.readLine()) != null) {
            List<Integer> localIntList = new ArrayList<Integer>();
            for (String bits : line.split("\t")) {
                localIntList.add(Integer.parseInt(bits) - 1);
            }
            edges.add(localIntList);
        }
    }

    private void contract(int vertex1, int vertex2) {
        if (vertex2 > edges.size()) {
            System.out.print("what");
        }
        // first, remove vert2 from vert1, because it's a self loop
        List<Integer> vert1 = edges.get(vertex1);
        List<Integer> vert2 = edges.get(vertex2);
        vert1.addAll(vert2);
        for (int i = 1; i < vert1.size();) {
            if (vert1.get(i) == vertex2 || vert1.get(i) == vertex1) {
                vert1.remove(i);
            } else {
                i++;
            }
        }
        edges.set(vertex1, vert1);

        // remove vert2 altogether
        edges.remove(vertex2);

        // replace any mentions of vert2 with vert1
        for (int i = 0; i < edges.size(); i++) {
            List<Integer> everyVertex = edges.get(i);
            for (int j = 1; j < everyVertex.size(); j++) {
                int everyElement = everyVertex.get(j);
                if (everyElement == vertex2) {
                    everyVertex.set(j, vertex1);
                }
            }
            edges.set(i, everyVertex);
        }

        // take everything greater than vertex2 and reduce it by 1
        for (int i = 0; i < edges.size(); i++) {
            List<Integer> everyVertex = edges.get(i);
            for (int j = 0; j < everyVertex.size(); j++) {
                int everyElement = everyVertex.get(j);
                if (everyElement > vertex2) {
                    everyVertex.set(j, everyElement - 1);
                }
            }
        }
    }

    int max(List<List<Integer>> adjacent) {
        int returnVal = 0;
        for (int i = 0; i < adjacent.size(); i++){
            for (int j = 0; j < adjacent.get(i).size(); j++) {
                if (adjacent.get(i).get(j) > returnVal) {
                    returnVal = adjacent.get(i).get(j);
                }
            }
        }
        return returnVal;
    }

    boolean sanityTest() {
        // check that no nodes connect to a node that doesn't exist
        int max = max(this.edges);
        if (max > this.edges.size()) {
            return false;
        }

        // check that for every node
            // for each connection the other node lists it
        for (List<Integer> node : edges) {
            for (Integer i : node) {
                if (!edges.get(i).contains(node.get(0))) {
                    return false;
                }
            }
        }

        return true;
    }
}
