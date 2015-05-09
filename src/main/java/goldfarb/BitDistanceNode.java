package goldfarb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by dgoldfarb on 7/16/14.
 */
public class BitDistanceNode {
    class ImplicitNode {
        int num_bits;
        int[] node;
        int value;
        int clusterId;

        ImplicitNode(int num, int[] node, int value, int cluster) {
            this.num_bits = num;
            this.node = node;
            this.value = value;
            this.clusterId = cluster;
        }

        int distance(ImplicitNode that) {
            int result = 0;
            if (num_bits != that.num_bits) {
                return -1;
            }
            for (int i = 0; i < num_bits; i++) {
                if (this.node[i] != that.node[i]) {
                    result++;
                }
            }
            return result;
        }

        void print() {
            System.out.print("[");
            for (int i = 0; i < num_bits - 1; i++) {
                System.out.print(node[i] + ", ");
            }
            System.out.println(node[num_bits - 1] + "]");
        }
    }

    List<ImplicitNode> nodeList;
    Hashtable<Integer, ImplicitNode> nodes;
    int bits;
    int num_nodes;
    int cluster_distance = 3;

    public BitDistanceNode(String arg)throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(arg)));
        String line = reader.readLine();
        bits = Integer.parseInt(line.split(" ")[1]);
        num_nodes = Integer.parseInt(line.split(" ")[0]);

        nodes = new Hashtable<Integer, ImplicitNode>();
        nodeList = new ArrayList<ImplicitNode>();
        int j = 0;
        while ((line = reader.readLine()) != null) {
            int value = Integer.parseInt(line.replaceAll(" ", ""),2);
            String[] split = line.split(" ");
            int[] one = new int[bits];
            for (int i = 0; i < bits; i++) {
                one[i] = Integer.parseInt(split[i]);
            }
            ImplicitNode node = new ImplicitNode(bits, one, value, j);
            nodes.put(value, node);
            j++;
        }

        nodeList.addAll(nodes.values());
    }

    public static void main(String[] args) throws IOException {
        BitDistanceNode bitDistanceNode = new BitDistanceNode(args[0]);
        bitDistanceNode.run();
    }

    private void printNodes() {
        for (ImplicitNode node : nodeList) {
            System.out.println(node.clusterId);
        }
    }

    private void run() {
        // repeat as as long as necessary
        //while (max_distance < cluster_distance) {

        //for (int d = 0; d < 2; d++ ) {
        // for each node, check for every permutation with distance
        // less than or equal 2
        // if found, consolidate clusters
        int iterations = 0;
        for (ImplicitNode node : nodeList) {
            if (iterations % 1000 == 0) {
                System.out.println(iterations);
            }
            iterations++;
            int[] original = node.node;

            // permute clusters 1 time
            for (int i = 0; i < node.num_bits; i++) {
                int[] permutation = original.clone();
                permutation[i] = flip(permutation[i]);
                // if this permutation is in the hash table
                if (nodes.get(value(permutation)) != null) {
                    // merge the clusters
                    mergeClusters(node.clusterId, nodes.get(value(permutation)).clusterId);
                }
            }

            // permute clusters 2 times
            for (int i = 0; i < node.num_bits - 1; i++) {
                int[] permutation = original.clone();
                permutation[i] = flip(permutation[i]);
                for (int j = i + 1; j < node.num_bits; j++) {
                    int[] perm2 = permutation.clone();
                    perm2[j] = flip(perm2[j]);
                    if (nodes.get(value(perm2)) != null) {
                        mergeClusters(node.clusterId, nodes.get(value(perm2)).clusterId);
                    }
                }
            }
        // }
        }
        // count clusters
        HashMap<Integer, Integer> clusterCounts = new HashMap<Integer, Integer>();
        for (ImplicitNode node : nodeList) {
            if (clusterCounts.get(node.clusterId) != null) {
                clusterCounts.put(node.clusterId, clusterCounts.get(node.clusterId) + 1);
            } else {
                clusterCounts.put(node.clusterId, 1);
            }
        }
        System.out.println(clusterCounts.keySet().size());
        System.out.println(clusterCounts.values());

    }

    private void mergeClusters(int clusterId, int clusterId1) {
        for (ImplicitNode node : nodeList) {
            if (node.clusterId == clusterId1) {
                node.clusterId = clusterId;
            }
        }
    }

    private int flip(int i) {
        if (i == 0) {
            return 1;
        } else {
            return 0;
        }
    }

    private int value(int[] i) {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < i.length; j++) {
            sb.append(i[j]);
        }
        return Integer.parseInt(sb.toString(), 2);
    }


}
