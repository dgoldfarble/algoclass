package all;

/**
 * Created by dgoldfarb on 4/13/15.
 */
public class TreeNode {
    private TreeNode leftChild;
    private TreeNode rightChild;
    private TreeNode parent;
    private int key;
    private long weight;

    TreeNode(int key, long weight) {
        leftChild = null;
        rightChild = null;
        parent = null;
        this.key = key;
        this.weight = weight;
    }

    void setLeftChild(TreeNode child) {
        this.leftChild = child;
    }

    void setRightChild(TreeNode child) {
        this.rightChild = child;
    }
}
