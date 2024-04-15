package cs250.hw4;

public class BTree implements TreeStructure {

    private BTree leftNode;
    private Integer value;
    private long timestamp;
    private BTree rightNode;

    @Override
    public void insert(Integer num) {
        if (this.value == null) {
            this.value = num;
            this.timestamp = System.nanoTime();
            System.out.println(String.format("%d inserted at %d", this.value, this.timestamp));
        } else {
            insertIntoLeafNodes(num);
        }
    }

    private void insertIntoLeafNodes(Integer num) {
        if (num < this.value) {
            // insert left tree
            this.leftNode = instantiateOrInsertIntoNode(this.leftNode, num);
        } else if (num > this.value) {
            // insert right tree
            this.rightNode = instantiateOrInsertIntoNode(this.rightNode, num);
        }
    }

    private BTree instantiateOrInsertIntoNode(BTree node, Integer num) {
        if (node == null) {
            node = new BTree();
        }
        node.insert(num);
        return node;
    }

    @Override
    public Boolean remove(Integer num) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public Long get(Integer num) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public Integer findMaxDepth() {
        Integer result = 0;
        if (this.value != null) {
            result += 1;
        }

        Integer leftDepth = 0;
        if (this.leftNode != null) {
            leftDepth = this.leftNode.findMaxDepth();
        }

        Integer rightDepth = 0;
        if (this.rightNode != null) {
            rightDepth = this.rightNode.findMaxDepth();
        }

        result += Math.max(leftDepth, rightDepth);

        return result;
    }

    @Override
    public Integer findMinDepth() {
        Integer result = 0;
        if (this.value != null) {
            result += 1;
        }

        Integer leftDepth = 0;
        if (this.leftNode != null) {
            leftDepth = this.leftNode.findMaxDepth();
        }

        Integer rightDepth = 0;
        if (this.rightNode != null) {
            rightDepth = this.rightNode.findMaxDepth();
        }

        result += Math.min(leftDepth, rightDepth);

        return result;
    }

}