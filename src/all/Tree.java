package all;

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
    private TreeNode root;
    public Tree() {

    }

    public static void main(String[] args) throws Exception {
        Tree tree = new Tree();
        System.out.println(tree.minAverageSearchTime(args[0]));
    }

    // reads a
    private long minAverageSearchTime(String file) throws IOException {
        List<TreeNode> nodeList = new ArrayList<TreeNode>();

        BufferedReader reader = new BufferedReader(new FileReader(new File(file)));
        String line;
        while ((line = reader.readLine()) != null) {
            int nodeNumber = Integer.parseInt(line.split("\t")[0]);
            long nodeWeight = Long.parseLong(line.split("\t")[1]);
            TreeNode node = new TreeNode(nodeNumber, nodeWeight);
            nodeList.add(node);
        }

        int n = nodeList.size();
        long[][] A = new long[n][n];
        for (int s = 0; s < n; s++) {
            for (int i = 0; i < n; i++) {
                for (int r = i; r < i + s; r++) {

                }
                A[i][i+s] =
            }
        }
    }
}
