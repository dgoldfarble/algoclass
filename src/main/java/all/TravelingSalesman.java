package all;

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

    class Point extends Node {
        float x;
        float y;
        public Point(int i, float x, float y) {
            super(i);
            this.x = x;
            this.y = y;
        }

        public float distance (Point other) {
            float x1 = (this.x - other.x) * (this.x - other.x);
            float x2 = (this.y - other.y) * (this.y - other.y);
            return (float) Math.sqrt(x1 + x2);
        }
    }

    class SubsetPlusLastCityKey {
        Set<Integer> subset;
        int terminalCity;

        SubsetPlusLastCityKey(Set subset, int terminalCity) {
            this.subset = subset;
            this.terminalCity = terminalCity;
        }

        @Override
        public int hashCode() {
            return subset.hashCode() * 31 + terminalCity;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            SubsetPlusLastCityKey that = (SubsetPlusLastCityKey) o;
            if (that.subset.equals(this.subset) && that.terminalCity == this.terminalCity) {
                return true;
            }
            return false;
        }
    }

    int num_nodes;
    List<Point> points;

    HashMap<SubsetPlusLastCityKey, Float> prevSubproblems;
    List<Set> combinationsList;
    HashMap<SubsetPlusLastCityKey, Float> subproblems;

    TravelingSalesman(String arg) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(arg)));
        String line = reader.readLine();
        num_nodes = Integer.parseInt(line);
        points = new ArrayList<Point>();
        int i = 0;

        LOG.info("Reading file " + arg);
        while ((line = reader.readLine()) != null) {
            points.add(new Point(i, Float.parseFloat(line.split(" ")[0]), Float.parseFloat(line.split(" ")[1])));
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
        prevSubproblems = new HashMap<>();
        Set<Integer> b = new HashSet<Integer>();
        b.add(0);
        prevSubproblems.put(new SubsetPlusLastCityKey(b, 0), (float) 0.0);

        for (int subproblem_size = 2; subproblem_size < num_nodes + 1; subproblem_size++) {
            LOG.debug("Generating set size " + subproblem_size);

            // initialize LOCAL variables
            subproblems = new HashMap<>();
            combinationsList = new ArrayList<>();
            Combinations combinationGenerator = new Combinations(num_nodes, subproblem_size);
            Iterator iterator = combinationGenerator.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                int[] set = (int[]) iterator.next();
                // set must contain 0
                if (set[0] == 0) {
                    Set<Integer> combinations = new HashSet<>();
                    for (int i1 : set) {
                        combinations.add(i1);
                    }
                    combinationsList.add(combinations);
                    i++;
                }
            }

            LOG.debug("Running subroutine size " + subproblem_size + ", " + combinationsList.size() + " combinations");
            // for each set at this problem size, iterate through the members
            // remove the member, then iterate over the remaining members
            for (Set s : combinationsList) {
                iterator = s.iterator();
                int[] temp = new int[s.size()];
                int index = 0;
                while (iterator.hasNext()) {
                    temp[index] = (int) iterator.next();
                    index++;
                }
                for (int potentialDestination : temp) {
                    if (potentialDestination == 0) {
                        continue;
                    } else {
                        Float best = Float.MAX_VALUE;
                        s.remove(potentialDestination);
                        Iterator localIterator = s.iterator();
                        while (localIterator.hasNext()) {
                            int k = (int) localIterator.next();
                            SubsetPlusLastCityKey key = new SubsetPlusLastCityKey(s, k);
                            if (prevSubproblems.get(key) != null) {
                                if (prevSubproblems.get(key) + points.get(k).distance(points.get(potentialDestination)) < best) {
                                    best = prevSubproblems.get(key) + points.get(k).distance(points.get(potentialDestination));
                                }
                            }
                        }
                        s.add(potentialDestination);
                        SubsetPlusLastCityKey key = new SubsetPlusLastCityKey(s, potentialDestination);
                        subproblems.put(key, best);
                    }
                }
            }
            prevSubproblems = subproblems;
        }

        float returnValue = Float.MAX_VALUE;
        for (int j = 1; j < num_nodes; j++) {
            SubsetPlusLastCityKey key = new SubsetPlusLastCityKey(combinationsList.get(0), j);
            if (subproblems.get(key) < returnValue) {
                returnValue = subproblems.get(key) + points.get(j).distance(points.get(0));
            }
        }
        LOG.info(returnValue);
    }
}
