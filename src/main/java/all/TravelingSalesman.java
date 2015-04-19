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
public class TravelingSalesman {
    class Point extends Node {
        float x;
        float y;
        public Point(int i, float x, float y) {
            super(i);
            this.x = x;
            this.y = y;
        }

        public float distance (Point other) {
            double x1 = (this.x - other.x) * (this.x - other.x);
            double x2 = (this.y - other.y) * (this.y - other.y);
            return (float) Math.sqrt(x1 + x2);
        }
    }

    int num_nodes;
    List<Point> points;
    float[][] subproblems;

    List<List<List<Integer>>> bigcombinations;
    List<List<Integer>> combinations;

    TravelingSalesman(String arg) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(arg)));
        String line = reader.readLine();
        num_nodes = Integer.parseInt(line);
        points = new ArrayList<Point>();
        int i = 0;

        System.out.println("Reading file " + arg);
        while ((line = reader.readLine()) != null) {
            points.add(new Point(i, Float.parseFloat(line.split(" ")[0]), Float.parseFloat(line.split(" ")[1])));
            i++;
        }
        for (i = 0; i < 4; i++) {
            System.out.println(points.get(i).x + ", " + points.get(i).y);
        }

        System.out.println("\nInitializing combinations");
        combinations = new ArrayList<List<Integer>>();
        for (i = 0; i < num_nodes; i++) {
            int[] buf = new int[i];
            System.out.println(i);
            cmb(0, 0, num_nodes, i, buf);
        }
        List<Integer> all = new ArrayList<Integer>();
        for (i = 0; i < num_nodes; i++) {
            all.add(i);
        }
        combinations.add(all);

        subproblems = new float[combinations.size()][num_nodes];
        List<Integer> simple_set = new ArrayList<Integer>();
        simple_set.add(0);
        for (i = 0; i < combinations.size(); i++) {
            if (combinations.get(i).equals(simple_set)) {
                subproblems[i][0] = 0;
            } else {
                subproblems[i][0] = Float.MAX_VALUE;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        TravelingSalesman ts = new TravelingSalesman(args[0]);


        ts.run();
    }

    private void run() {
        // iterate over subproblem size
        for (int subproblem_size = 2; subproblem_size < num_nodes + 1; subproblem_size++) {
            System.out.println("Starting subproblems size " + subproblem_size);

            int i = 0;
            // iterate over every subset of that size (hard)
            for (List<Integer> combination : combinations) {

                if (combination.size() == subproblem_size) {
                    for (int xyz = 0; xyz < num_nodes; xyz++) {
                        subproblems[i][xyz] = Float.MAX_VALUE;
                    }
                    // iterate over every possible final node
                    for (int xzz = 1; xzz < combination.size(); xzz++) {
                        int j = combination.get(xzz);
                        // subproblems[this set][possible final node in this tour]

                        // equals
                        // minimum of
                        // all final nodes of
                        // this set with the final node removed
                        List<Integer> matchme = new ArrayList<Integer>();
                        matchme.addAll(combination);
                        matchme.remove(xzz);
                        // plus the cost of the path from that node to this one


                        float best_yet = Float.MAX_VALUE;
                        int index;
                        for (index = 0; index < combinations.size(); index++) {
                            if (combinations.get(index).equals(matchme)) {
                                break;
                            }
                        }

                        for (int kzz = 0; kzz < matchme.size(); kzz++) {
                            if (subproblems[index][kzz] < Float.MAX_VALUE) {
                                float distance = subproblems[index][kzz] + points.get(j).distance(points.get(matchme.get(kzz)));
                                if (distance < best_yet) {
                                    best_yet = distance;
                                }
                            }
                        }
                        subproblems[i][xzz] = best_yet;
                    }
                }
                i++;
            }
        }
        List<Integer> allOfThem = new ArrayList<Integer>();
        for (int i = 0; i < num_nodes; i++) {
            allOfThem.add(i);
        }
        int index;
        for (index = 0; index < combinations.size(); index++) {
            if (combinations.get(index).equals(allOfThem)) {
                break;
            }
        }
        float best_yet = Float.MAX_VALUE;
        for (Integer j : allOfThem) {
            if (j == 0) {
                continue;
            }
            if (subproblems[index][j] < Float.MAX_VALUE) {
                float distance = subproblems[index][j] + points.get(j).distance((points.get(0)));
                if (distance < best_yet) {
                    best_yet = distance;
                }
            }
        }
        System.out.println(best_yet);
    }


    void cmb (int depth, int index, int n, int m, int[] buffer) {
        if (depth > m-1) {
            List<Integer> combination = new ArrayList<Integer>();
            long num = 0;
            for (int j=0; j < m; j++)  {
                combination.add(buffer[j] - 1);
                num = num | (1<<(buffer[j]-1));
            }
            //combination.add((int) num);
            if (combination.contains(0)) {
                combinations.add(combination);
            }
            //System.out.println(combination);
        } else {
            for (int p=index; p < n-m+1 + depth; p++) {
                buffer[depth] = p+1;
                cmb(depth+1, p+1, n, m, buffer);
            }
        }
    }

    int factorial(int j) {
        int result = 1;
        for (int k = j; k > 1; k--) {
            result = result * k;
        }
        return result;
    }

    int combine(int n, int k) {
        return factorial(n) / (factorial(k) * factorial(n - k));
    }
}
