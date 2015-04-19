package all;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Created by dgoldfarb on 5/23/14.
 */
public class Quicksort {
    public static void main(String[] args) {
        int size = Integer.parseInt(args[1]);
        int[] inputArray = new int[size];
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(args[0])));
            String line;
            for (int i = 0; i < size; i++) {
                line = reader.readLine();
                inputArray[i] = Integer.parseInt(line);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //int size = 10;
        //int[] inputArray = new int[]{0, 9, 8, 7, 6, 5, 4, 3, 2, 1};

        for (int i = 0; i < 10; i++) {
            System.out.println(inputArray[i]);
        }

        int numCompares = Quicksort.run(inputArray, 0, size);

        for (int i = 0; i < 10; i++) {
            System.out.println(inputArray[i]);
        }

        for (int i = 0; i < size; i++) {
            if (inputArray[i] != i + 1) {
                //System.out.println("error: " + i);
            }
        }
        System.out.println("Number of compares: " + numCompares);
    }

    // return number of comparison needed
    private static int run(int[] inputArray, int left, int right) {
        if (right - left <= 1) {
            return 0;
        }
        // first question
        // int p = inputArray[left];

        // second question
        /* int p = inputArray[right - 1];
        inputArray[right - 1] = inputArray[left];
        inputArray[left] = p; */

        // third question
        int p = chooseMedian(inputArray, left, right);
        int i = left + 1;
        for (int j = left + 1; j < right; j++) {
            if (inputArray[j] < p) {
                int temp = inputArray[j];
                inputArray[j] = inputArray[i];
                inputArray[i] = temp;
                i++;
            }
        }
        int temp = inputArray[i - 1];
        inputArray[i - 1] = p;
        inputArray[left] = temp;
        int leftCompares = run(inputArray, left, i - 1);
        int rightCompares = run(inputArray, i, right);
        return leftCompares + rightCompares + (right - left - 1);
    }

    private static int chooseMedian(int[] inputArray, int left, int right) {
        int size = right - left;
        int middleIndex;
        if (size % 2 == 0) {
            middleIndex = size/2 - 1 + left;
        } else {
            middleIndex = (size - 1)/2 + left;
        }

        //System.out.println("The median of the following array is " + inputArray[middleIndex]);
        //printSubarray(inputArray, left, right);

        if ((inputArray[left] > inputArray[middleIndex] && inputArray[left] < inputArray[right - 1]) ||
                inputArray[left] < inputArray[middleIndex] && inputArray[left] > inputArray[right - 1]) {
            // left is median
        } else {
            if (inputArray[left] > inputArray[middleIndex]) { // left is maximum
                if (inputArray[middleIndex] < inputArray[right - 1]) {
                    // right is median
                    int temp = inputArray[right - 1];
                    inputArray[right - 1] = inputArray[left];
                    inputArray[left] = temp;
                } else {
                    // middle is median
                    int temp = inputArray[middleIndex];
                    inputArray[middleIndex] = inputArray[left];
                    inputArray[left] = temp;
                }
            } else { // left is minimum
                if (inputArray[middleIndex] < inputArray[right - 1]) {
                    // middle is median
                    int temp = inputArray[middleIndex];
                    inputArray[middleIndex] = inputArray[left];
                    inputArray[left] = temp;
                } else {
                    // right is median
                    int temp = inputArray[right - 1];
                    inputArray[right - 1] = inputArray[left];
                    inputArray[left] = temp;
                }
            }
        }

        //printSubarray(inputArray, left, right);

        return inputArray[left];
    }

    public static void printSubarray(int[] inputArray, int left, int right){
        System.out.print("[" + inputArray[left]);
        for (int i = left + 1; i < right; i++) {
            System.out.print(", " + inputArray[i]);
        }
        System.out.println("]");
    }
}
