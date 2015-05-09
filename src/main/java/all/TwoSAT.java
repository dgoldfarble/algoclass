package all;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by dgoldfarb on 5/1/15.
 */
public class TwoSAT {
    class Clause {
        int x1;
        int x2;
        boolean result;
        //
        final int mode;

        Clause(int x1, int x2, int mode) {
            this.x1 = x1;
            this.x2 = x2;
            this.mode = mode;
        }

        void update() {
            if (x1 >= x.length || x2 >= x.length) {
                result = true;
            } else {
                switch (mode) {
                    case 0:
                        result = x[x1] || x[x2];
                        break;
                    case 1:
                        result = (!x[x1]) || x[x2];
                        break;
                    case 2:
                        result = x[x1] || (!x[x2]);
                        break;
                    case 3:
                        result = (!x[x1]) || (!x[x2]);
                }
            }
        }
    }
    boolean[] x;
    List<List<Integer>> index;
    List<Clause> clauses;

    private final Logger LOG = Logger.getLogger(TwoSAT.class);

    TwoSAT(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(file)));
        String line = reader.readLine();
        int numClauses;
        if (line.split(" ").length > 1) {
            x = new boolean[Integer.parseInt(line.split(" ")[0])];
            numClauses = Integer.parseInt(line.split(" ")[1]);
        } else {
            x = new boolean[Integer.parseInt(line)];
            numClauses = x.length;
        }
        index = new ArrayList<>();
        for (int i = 0; i < x.length; i++) {
            index.add(new ArrayList<Integer>());
        }

        clauses = new ArrayList<>();
        for (int i = 0; i < numClauses; i++) {
            line = reader.readLine();
            int x1 = Integer.parseInt(line.split(" ")[0]);
            int x2 = Integer.parseInt(line.split(" ")[1]);
            int mode;
            if (x1 > 0 && x2 > 0) {
                mode = 0;
            } else if (x1 < 0 && x2 > 0) {
                mode = 1;
            } else if (x1 > 0 && x2 < 0) {
                mode = 2;
            } else {
                mode = 3;
            }
            x1 = Math.abs(x1) - 1;
            x2 = Math.abs(x2) - 1;
            clauses.add(new Clause(x1, x2, mode));
            clauses.get(i).update();
            if (x1 < x.length) {
                index.get(x1).add(i);
            }
            if (x2 < x.length) {
                index.get(x2).add(i);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        for (int i = 0; i < args.length; i++) {
            TwoSAT sat = new TwoSAT(args[i]);
            sat.LOG.setLevel(Level.DEBUG);
            sat.run();
        }
    }

    private void run() {
        boolean satisfiable = false;
        int limit = (int) Math.floor(Math.log(x.length) / Math.log(2));
        for (int i = 0; i < limit; i++) {
            LOG.debug("outer loop " + i + " / " + limit);
            if (subroutine()) {
                satisfiable = true;
                break;
            }
        }
        LOG.info("Satisfiable: " + satisfiable);
    }

    boolean subroutine() {
        Random generator = new Random();

        Set<Integer> failure = new HashSet<>();
        // initialize random assignment
        LOG.debug("randomizing");
        for (int i = 0; i < x.length; i++) {
            boolean y = Math.random() < 0.5;
            if (x[i] != y) {
                x[i] = y;
                for (int j : index.get(i)) {
                    clauses.get(j).update();
                }
            }
        }
        for (int i = 0; i < x.length; i++) {
            if (!clauses.get(i).result) {
                failure.add(i);
            }
        }
        LOG.debug("randomized");
        // at first, just select random numbers and check if they're false
        for (long i = 0; i < x.length; i++) {
            // LOG.debug("failures: " + failure.size());

            boolean x1orx2 = generator.nextInt(2) == 1;
            int xToUpdate;
            // do we really need to look through the entire list?
            int j = 0;

            Clause clause;
            if (failure.size() > x.length) {

                while ((clause = clauses.get(generator.nextInt(clauses.size()))).result) {
                    // when this while loop finishes, clause is a random failure case
                }
                if (x1orx2) {
                    xToUpdate = clause.x1;
                } else {
                    xToUpdate = clause.x2;
                }

                x[xToUpdate] = !x[xToUpdate];
                for (int k : index.get(xToUpdate)) {
                    clauses.get(k).update();
                    if (clauses.get(k).result) {
                        failure.remove(k);
                    } else {
                        failure.add(k);
                    }
                }
            }
            else if (failure.size() > 0) {

                // This is a terrible, terrible, ugly way to get a random result
                Iterator iterator = failure.iterator();
                int max = generator.nextInt(failure.size()) - 1;
                for (int k = 0; k < max; k++) {
                    iterator.next();
                }
                clause = clauses.get((int) iterator.next());
                if (x1orx2) {
                    xToUpdate = clause.x1;
                } else {
                    xToUpdate = clause.x2;
                }

                x[xToUpdate] = !x[xToUpdate];
                for (int k : index.get(xToUpdate)) {
                    clauses.get(k).update();
                    if (clauses.get(k).result) {
                        failure.remove(k);
                    } else {
                        failure.add(k);
                    }
                }
            } else {
                return true;
            }
        }
        return false;
    }

    void dump(int n) {
        for (int i = 0; i < n; i++) {
            Clause clause = clauses.get(i);
            switch (clause.mode) {
                case 0: LOG.debug(i + ": " + x[clause.x1] + " || " + x[clause.x2] + " = " + clauses.get(i).result);
                    break;
                case 1: LOG.debug(i + ": !" + x[clause.x1] + " || " + x[clause.x2] + " = " + clauses.get(i).result);
                    break;
                case 2: LOG.debug(i + ": " + x[clause.x1] + " || !" + x[clause.x2] + " = " + clauses.get(i).result);
                    break;
                case 3: LOG.debug(i + ": !" + x[clause.x1] + " || !" + x[clause.x2] + " = " + clauses.get(i).result);
                    break;
            }
        }
    }
}

