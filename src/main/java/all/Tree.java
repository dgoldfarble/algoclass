package all;

import com.sun.javafx.tools.packager.Log;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by dgoldfarb on 4/13/15.
 */
public class Tree {
    private final Logger LOG = Logger.getLogger(Tree.class);
    private TreeNode root;
    public Tree() {

    }

    public static void main(String[] args) throws Exception {
        Tree tree = new Tree();
        // tree.LOG.getRootLogger().setLevel(Level.DEBUG);
        tree.LOG.info(tree.minAverageSearchTime(args[0]));
    }

    // loads a list of node weights
    // calculates the average search time for an optimal
    // binary search tree
    private double minAverageSearchTime(String file) throws IOException {
        List<TreeNode> nodeList = new ArrayList<TreeNode>();

        LOG.debug("Starting OBST algorithm");
        // read the node weights and create Nodes
        // can't initialize our result array until
        // we know how big it needs to be
        BufferedReader reader = new BufferedReader(new FileReader(new File(file)));
        String line;
        int nodeCounter = 0;
        while ((line = reader.readLine()) != null) {
            double nodeWeight = Double.parseDouble(line);
            TreeNode node = new TreeNode(nodeCounter, nodeWeight);
            nodeList.add(node);
            nodeCounter++;
        }

        int n = nodeList.size();
        double[][] A = new double[n][n];
        int[][] B = new int[n][n];

        nodeCounter = 0;
        for (TreeNode node : nodeList) {
            A[nodeCounter][nodeCounter] = node.getWeight();
            B[nodeCounter][nodeCounter] = nodeCounter;
            nodeCounter++;
        }

        // for all possible sequences in a tree
        for (int s = 1; s < n; s++) {
            LOG.debug("Range: " + s);
            // whose indices start at i
            for (int i = 0; i < n - s; i++) {
                LOG.debug("Start of range: " + i);
                // find the optimal search time in terms of its children
                // find minimum of cost(all children leafs)
                //                       + cost associated with considering node r as the new root
                int optimalRootForRange = 0;
                double costOfOptimalTreeForRange = Double.MAX_VALUE;
                double sumOfChildrenLeafWeights = 0;
                for (int r = i; r <= i + s; r++) {
                    sumOfChildrenLeafWeights += nodeList.get(r).getWeight();
                    double costOfRAsRoot = 0;
                    LOG.debug("r: " + r);
                    // add left subtree, if it exists
                    if (r - 1 >= i) {
                        costOfRAsRoot += A[i][r - 1];
                        LOG.debug("cost of left subtree: " + A[i][r - 1]);
                    }
                    // add right subtree, if it exists
                    if (r + 1 <= i + s) {
                        costOfRAsRoot += A[r + 1][i + s];
                        LOG.debug("cost of right subtree: " + A[r + 1][i + s]);
                    }

                    // compare to previous best
                    if (costOfRAsRoot < costOfOptimalTreeForRange) {
                        LOG.debug("new cost of " + costOfRAsRoot + " is less than previous min of " + costOfOptimalTreeForRange);
                        optimalRootForRange = r;
                        costOfOptimalTreeForRange = costOfRAsRoot;
                    }
                }
                A[i][i + s] = costOfOptimalTreeForRange + sumOfChildrenLeafWeights;
                B[i][i + s] = optimalRootForRange;

                LOG.debug("cost of optimal tree = " + costOfOptimalTreeForRange + " + " + sumOfChildrenLeafWeights + " = " + A[i][i + s]);
            }
        }

        return A[0][n - 1];
        // todo: add backtracking algorithm to produce the Tree
    }
}
