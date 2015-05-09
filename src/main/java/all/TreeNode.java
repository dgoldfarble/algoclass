package all;

/**
 * Created by dgoldfarb on 4/13/15.
 */
public class TreeNode {
    private TreeNode leftChild;
    private TreeNode rightChild;
    private TreeNode parent;
    private int key;
    private double weight;

    // depth
    private int height;

    TreeNode(int key, double weight) {
        leftChild = null;
        rightChild = null;
        parent = null;
        this.key = key;
        this.weight = weight;
    }

    public void setLeftChild(TreeNode child) {
        this.leftChild = child;
    }

    public void setRightChild(TreeNode child) {
        this.rightChild = child;
    }

    public double getWeight() {
        return weight;
    }

    public TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public int getKey() {
        return key;
    }


}
