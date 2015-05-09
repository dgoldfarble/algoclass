package goldfarb;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

/**
 * Created by dgoldfarb on 6/22/14.
 */
public class HashTables {
    public static void main(String[] args) throws IOException {
        new HashTables().TwoSum(args[0]);
    }

    private void TwoSum(String filename) throws IOException {
        HashSet<Long> table = new HashSet<Long>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            table.add(Long.parseLong(line));
        }
        long count = 0;
        for (int i = -10000; i <= 10000; i++) {
            boolean found = false;
            for (Long j : table) {
                if (table.contains(i - j) && !(i - j == j)) {
                    found = true;
                    break;
                }
            }
            if (found) {
                count++;
            }
            System.out.println(i);
        }
        System.out.println(count);
    }
}
