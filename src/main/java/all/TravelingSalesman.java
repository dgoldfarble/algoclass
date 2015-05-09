package all;

import org.apache.commons.math3.util.CombinatoricsUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.apache.commons.math3.util.Combinations;

/**
 * Created by dgoldfarb on 8/9/14.
 */
public class TravelingSalesman {
    private final Logger LOG = Logger.getLogger(TravelingSalesman.class);

    class Point {
        float x;
        float y;
        public Point( float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float distance (Point other) {
            float x1 = (this.x - other.x) * (this.x - other.x);
            float x2 = (this.y - other.y) * (this.y - other.y);
            return (float) Math.sqrt(x1 + x2);
        }
    }

    int num_nodes;
    Point[] points;
    Point referencePoint;

    float[][] prevSubproblems;
    int[][] prevInvertedIndex;
    int[][] invertedIndex;
    float[][] subproblems;

    TravelingSalesman(String arg) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(arg)));
        String line = reader.readLine();
        num_nodes = Integer.parseInt(line) - 1;
        line = reader.readLine();
        referencePoint = new Point(Float.parseFloat(line.split(" ")[0]), Float.parseFloat(line.split(" ")[1]));
        points = new Point[num_nodes];
        LOG.info("Reading file " + arg);
        int i = 0;
        while ((line = reader.readLine()) != null) {
            points[i] = new Point(Float.parseFloat(line.split(" ")[0]), Float.parseFloat(line.split(" ")[1]));
            i++;
        }
    }

    public static void main(String[] args) throws IOException {
        TravelingSalesman ts = new TravelingSalesman(args[0]);
        ts.LOG.setLevel(Level.DEBUG);
        ts.run();
    }

    private void run() {
        LOG.info("Starting TSP Algorithm");

        // initialize prev stuff
        prevSubproblems = new float[num_nodes][1];// new HashMap<>();
        prevInvertedIndex = new int[num_nodes][1];
        for (int i = 0; i < num_nodes; i++) {
            prevSubproblems[i][0] = referencePoint.distance(points[i]);
            prevInvertedIndex[i] = new int[]{i};
        }
        Combinations prevCombinations = new Combinations(num_nodes, 1);
        int combinationIndex;
        int count;
        int potentialDestination;
        int index;

        for (int subproblem_size = 2; subproblem_size < num_nodes + 1; subproblem_size++) {
            LOG.debug("Subproblem size " + subproblem_size);

            // initialize LOCAL variables
            invertedIndex = new int[(int) CombinatoricsUtils.binomialCoefficient(num_nodes, subproblem_size)][subproblem_size];
            subproblems = new float[(int) CombinatoricsUtils.binomialCoefficient(num_nodes, subproblem_size)][subproblem_size];
            Combinations combinationGenerator = new Combinations(num_nodes, subproblem_size);
            Iterator iterator = combinationGenerator.iterator();
            int[] temp = new int[subproblem_size - 1];
            while (iterator.hasNext()) {
                int[] set = (int[]) iterator.next();
                combinationIndex = getIndex(set, num_nodes);

                for (count = 0; count < set.length; count++) {
                    potentialDestination = set[count];
                    for (int i = 0; i < set.length - 1; i++) {
                        if (i < count) {
                            temp[i] = set[i];
                        } else {
                            temp[i] = set[i + 1];
                        }
                    }

                    Float best = Float.MAX_VALUE;
                    index = getIndex(temp, num_nodes);
                    float[] smallerSetResults = prevSubproblems[index];
                    for (int k = 0; k < smallerSetResults.length; k++) {
                        if (smallerSetResults[k] + points[prevInvertedIndex[index][k]].distance(points[potentialDestination]) < best) {
                            best = smallerSetResults[k] + points[prevInvertedIndex[index][k]].distance(points[potentialDestination]);
                        }
                    }
                    subproblems[combinationIndex][count] = best;
                }
                //
                invertedIndex[combinationIndex] = set;
            }
            prevSubproblems = subproblems;
            prevInvertedIndex = invertedIndex;
        }

        float returnValue = Float.MAX_VALUE;
        for (int j = 0; j < num_nodes; j++) {
            if (subproblems[0][j] + points[j].distance(referencePoint) < returnValue) {
                returnValue = subproblems[0][j] + points[j].distance(referencePoint);
            }
        }
        LOG.info(returnValue);
    }

    private int getIndex(int[] set, int max_value) {
        int result = 0;
        for (int i = 0; i < set.length; i++) {
            for (int j = i; j < set[i]; j++) {
                result += CombinatoricsUtils.binomialCoefficient(max_value - j - 2, set.length - i - 1);
            }
        }
        return result;
    }
}
