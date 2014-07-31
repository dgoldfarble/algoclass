package all;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dgoldfarb on 7/22/14.
 */
public class Knapsack {
    class Item {
        float value;
        int weight;
        Item(int w, float v) {
            this.value = v;
            this.weight = w;
        }
    }

    public static boolean debug = false;
    HashMap<Pair<Integer,Integer>,Float> subproblems;
    List<Item> itemList;
    //float[][] subproblems;
    int size;
    int num_items;


    public Knapsack(String arg) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(arg)));
        String line = reader.readLine();
        size = Integer.parseInt(line.split(" ")[0]);
        num_items = Integer.parseInt(line.split(" ")[1]);
        itemList = new ArrayList<Item>();
        subproblems = new HashMap<Pair<Integer, Integer>, Float>();

        Item item;
        while ((line = reader.readLine()) != null) {
            String[] split = line.split(" ");
            item = new Item(Integer.parseInt(split[1]), Float.parseFloat(split[0]));
            itemList.add(item);
        }
    }

    public static void main(String[] args) throws IOException {
        Knapsack knapsack = new Knapsack(args[0]);
        knapsack.run();
    }

    private void run() {
        System.out.println("Running knapsack algorithm...");
        for (int i = 0; i < size + 1; i++) {
            Pair pair = new Pair<Integer,Integer>(0, i);
            if (itemList.get(0).weight < i) {
                subproblems.put(pair, itemList.get(0).value);
            } else {
                subproblems.put(pair, (float) 0);
            }
        }
        Pair pair = new Pair<Integer, Integer>(0, 1);
        float answer = recurse(num_items, size);
        System.out.println(answer);
    }

    public float recurse(int num, int weight) {
        Pair<Integer,Integer> pair = new Pair<Integer, Integer>(num, weight);

        if (subproblems.get(pair) != null) {
            return subproblems.get(pair);

        } else {

            // A[i - 1, x]
            Pair<Integer,Integer> pair2 = new Pair<Integer, Integer>(num - 1, weight);
            // A[i - 1, x - wi]
            Pair<Integer,Integer> pair3 = new Pair<Integer, Integer>(num - 1, Math.max(weight - itemList.get(num - 1).weight, 0));

            if (subproblems.get(pair2) == null) {
                recurse(num - 1, weight);
            }
            if (subproblems.get(pair3) == null) {
                recurse(num - 1, Math.max(weight - itemList.get(num - 1).weight,0));
            }

            float answer;
            if (weight < itemList.get(num - 1).weight) {
                // edge case if weight of this item > knapsack capacity
                answer = subproblems.get(pair2);
            } else {
                answer = Math.max(subproblems.get(pair2), subproblems.get(pair3) + itemList.get(num - 1).value);
            }
            subproblems.put(pair, answer);
            if (debug) {
                System.out.println("Solved subproblem i,w = " + num + "," + weight + " : value = " + answer);
            }
            return answer;
        }
    }
}
