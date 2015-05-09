package goldfarb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dgoldfarb on 5/8/14.
 */
public class CountInversions {

    private class InversionsAndArray {
        int[] numbers;
        int inversions;

        InversionsAndArray(int x, int[] y) {
            numbers = y;
            inversions = x;
        }

        void printInversionsAndArray() {
            System.out.println("Inversions = " + inversions);
            System.out.println("numbers:\n" + Arrays.toString(numbers));
        }
    }

    int[] numbers;

    CountInversions(String filename) throws IOException {
        File inFile = new File(filename);
        BufferedReader reader = new BufferedReader(new FileReader(inFile));
        List<Integer> intList = new ArrayList();
        String line;
        while ((line = reader.readLine()) != null) {
            intList.add(Integer.parseInt(line));
            //System.out.println(line);
        }
        numbers = new int[intList.size()];
        for (int i = 0; i < intList.size(); i++) {
            numbers[i] = intList.get(i);
        }
    }

    long recurse(int[] intArray, int offset, int length) {
        if (length == 1) {
            return 0;
        } else {

            int half = length / 2;
            long firstHalf = recurse(intArray, offset, half);
            long secondHalf = recurse(intArray, offset + half, length - half);
            long thirdHalf = mergeAndCount(intArray, offset, length);

            assert(firstHalf >= 0);
            assert(secondHalf >= 0);
            assert(thirdHalf >= 0);

            return firstHalf + secondHalf + thirdHalf;
        }
    }

    long mergeAndCount(int[] intArray, int offset, int length) {
        int[] returnArray = new int[length];
        int i = offset;
        int j = offset + length / 2;
        long runningCount = 0;
        for (int k = 0; k < length; k++) {
            if (i == offset + length / 2) {
                returnArray[k] = intArray[j];
                j++;
            } else if (j == offset + length) {
                returnArray[k] = intArray[i];
                i++;
            } else if (intArray[i] > intArray[j]) {
                returnArray[k] = intArray[j];
                j++;
                runningCount = runningCount + length / 2 + offset - i;
            } else if (intArray[i] <= intArray[j]) {
                returnArray[k] = intArray[i];
                i++;
            } else {
                System.out.println("Something bad happened");
            }
        }
        for (int k = 0; k < length; k++) {
            intArray[offset + k] = returnArray[k];
        }
        if (runningCount < 0) {
        //    System.out.p
        }
        return runningCount;
    }

    public static void main(String[] args) {
        try {
            CountInversions ci = new CountInversions(args[0]);

            long answer = ci.recurse(ci.numbers, 0, ci.numbers.length);
            System.out.println(answer);
            //System.out.println(Arrays.toString(ci.numbers));

        } catch (IOException ex) {
            System.err.println("countInversions failed");
            ex.printStackTrace();
        }
    }
}
