package goldfarb;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/*
http://stackoverflow.com/questions/18241192/implement-heap-using-a-binary-tree
 */
public class Heap {

    private int S[];
    private int last;
    private int capacity;
    private boolean minMax;

    public Heap(boolean max, int cap) {
        S = new int[cap];
        last = 0;
        capacity = cap;
        // true = max
        minMax = max;
    }

    public static void main(String[] args) throws IOException {
        Heap HHigh = new Heap(false, 6000);
        Heap HLow = new Heap(true, 6000);
        BufferedReader reader = new BufferedReader(new FileReader(args[0]));
        String line;
        line = reader.readLine();
        HLow.insert(Integer.parseInt(line));
        int runningSum = Integer.parseInt(line);
        while ((line = reader.readLine()) != null) {
            int num = Integer.parseInt(line);
            // HLow.min is max in HLow
            if (num > HLow.max()) {
                HHigh.insert(num);
            } else {
                HLow.insert(num);
            }
            boolean balanced = false;
            int total = 0;
            while (!balanced) {
                int highSize = HHigh.size();
                int lowSize = HLow.size();
                total = highSize + lowSize;
                if (lowSize > (total + 1) / 2) {
                    HHigh.insert(HLow.removeMin());
                } else if (highSize > total / 2) {
                    HLow.insert(HHigh.removeMin());
                } else {
                    balanced = true;
                }
            }
            runningSum += HLow.min();
            System.out.println(HLow.min() + " " + runningSum);
        }
    }

    //
    // returns the number of elements in the heap
    //
    public int size() {
        return last;
    }

    //
    // is the heap empty?
    //
    public boolean isEmpty() {
        return size() == 0;
    }

    //
    // returns element with smallest (or largest) key, without removal
    //
    public int min() throws HeapException {
        if (isEmpty())
            throw new HeapException("The heap is empty.");
        else
            return S[1];
    }
    public int max() throws HeapException {
        return this.min();
    }

    // inserts e into the heap
    // throws exception if heap overflow
    //
    public void insert(int e) throws HeapException {
        if (size() == capacity)
            throw new HeapException("Heap overflow.");
        else{
            last++;
            S[last] = e;
            upHeapBubble();
        }
    }

    //
    // removes and returns smallest element of the heap
    // throws exception is heap is empty
    //
    public int removeMin() throws HeapException {
        if (isEmpty())
            throw new HeapException("Heap is empty.");
        else {
            int min = min();
            S[1] = S[last];
            last--;
            downHeapBubble();
            return min;
        }
    }

    /**
     * downHeapBubble() method is used after the removeMin() method to reorder the elements
     * in order to preserve the Heap properties
     */
    private void downHeapBubble() {
        int index = 1;
        while (true){
            int child = index*2;
            if (child > size())
                break;
            if (child + 1 <= size()){
                //if there are two children -> take the smalles or
                //if they are equal take the left one
                child = findMin(child, child + 1);
            }
            if (!minMax) {
                if (S[index] <= S[child]) {
                    break;
                }
            } else {
                if (S[index] >= S[child]) {
                    break;
                }
            }
            swap(index,child);
            index = child;
        }
    }

    /**
     * upHeapBubble() method is used after the insert(E e) method to reorder the elements
     * in order to preserve the Heap properties
     */
    private void upHeapBubble(){
        int index = size();
        while (index > 1){
            int parent = index / 2;
            if (!minMax) {
                if (S[index] >= S[parent]) {
                    //break if the parent is greater or equal to the current element
                    break;
                }
            } else {
                if (S[index] <= S[parent]) {
                    //break if the parent is greater or equal to the current element
                    break;
                }
            }
            swap(index,parent);
            index = parent;
        }
    }

    /**
     * Swaps two integers i and j
     * @param i
     * @param j
     */
    private void swap(int i, int j) {
        int temp = S[i];
        S[i] = S[j];
        S[j] = temp;
    }

    /**
     * the method is used in the downHeapBubble() method
     * @param leftChild
     * @param rightChild
     * @return min of left and right child, if they are equal return the left
     */
    private int findMin(int leftChild, int rightChild) {
        if (!minMax) {
            if (S[leftChild] <= S[rightChild]) {
                return leftChild;
            } else {
                return rightChild;
            }
        } else {
            if (S[leftChild] >= S[rightChild]) {
                return leftChild;
            } else {
                return rightChild;
            }
        }
    }

    public String toString() {
        String s = "[";
        for (int i = 1; i <= size(); i++) {
            s += S[i];
            if (i != last)
                s += ",";
        }
        return s + "]";
    }
    //
    // outputs the entries in S in the order S[1] to S[last]
    // in same style as used in ArrayQueue
    //

}

class HeapException extends RuntimeException {
    public HeapException(){};
    public HeapException(String msg){super(msg);}
}
