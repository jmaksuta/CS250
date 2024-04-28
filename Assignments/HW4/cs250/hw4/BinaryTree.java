package cs250.hw4;

import java.io.*;
import java.util.*;

public class BinaryTree implements TreeStructure {

    private TreeNode root;

    public BinaryTree() {
        super();
    }

    @Override
    public void insert(Integer num) {

        if (this.root == null) {
            this.root = new TreeNode(null, num);
        } else {
            this.root.insert(num);
        }
    }

    @Override
    public Boolean remove(Integer num) {
        Boolean result = false;
        if (this.root != null) {
            if (this.root.value.equals(num)) {
                result = removeRoot();
            } else {
                result = this.root.remove(num);
            }
        }
        return result;
    }

    private Boolean removeRoot() {
        Boolean result = false;
        // remove the root node.
        TreeNode predecessor = this.root;
        TreeNode successor = null;
        if (predecessor.hasNoChildren()) {
            // update parent
            this.root = null;
            predecessor = null;
            result = true;
        } else if (predecessor.hasOneChild()) {
            if (predecessor.leftNode != null) {
                this.root = predecessor.leftNode;
                this.root.parentNode = null;
                result = true;
            } else if (predecessor.rightNode != null) {
                this.root = predecessor.rightNode;
                this.root.parentNode = null;
                result = true;
            }
        } else if (predecessor.hasTwoChildren()) {
            // find nodes successor
            if (predecessor.rightNode != null) {
                successor = findSuccessorLeftMost(predecessor.rightNode);
            } else {
                successor = findSuccessorRightMost(predecessor.leftNode);
            }
            // successor has at most 1 child
            if (successor.parentNode != null) {
                successor.parentNode.pop(successor);
            }

            successor.attachLeftChild(predecessor.leftNode);

            successor.attachRightChild(predecessor.rightNode);

            successor.parentNode = null;
            this.root = successor;

            result = true;
        }
        return result;
    }

    private TreeNode findSuccessorRightMost(TreeNode current) {
        if (current != null) {
            while (current.rightNode != null) {
                if (current.rightNode != null) {
                    current = current.rightNode;
                }
            }
        }
        return current;
    }

    private TreeNode findSuccessorLeftMost(TreeNode current) {
        if (current != null) {
            while (current.leftNode != null) {
                if (current.leftNode != null) {
                    current = current.leftNode;
                }
            }
        }
        return current;
    }

    @Override
    public Long get(Integer num) {
        Long result = -1L;
        if (this.root != null) {
            result = this.root.get(num);
        }
        return result;
    }

    @Override
    public Integer findMaxDepth() {
        Integer result = 0;
        result = this.root.findMaxDepth();
        return result;
    }

    @Override
    public Integer findMinDepth() {
        Integer result = 0;
        result = this.root.findMinDepth();
        return result;
    }

    private class TreeNode {

        private TreeNode parentNode;
        private TreeNode leftNode;
        private Integer value;
        private long timestamp;
        private TreeNode rightNode;

        public TreeNode(TreeNode parentNode, Integer num) {
            super();
            this.parentNode = parentNode;
            this.value = num;
            this.timestamp = System.nanoTime();
        }

        public boolean hasNoChildren() {
            return (this.leftNode == null && this.rightNode == null);
        }

        public boolean hasOneChild() {
            return getChildCount() == 1;
        }

        public boolean hasTwoChildren() {
            return getChildCount() == 2;
        }

        public boolean isLeftChild() {
            return (this.parentNode != null && this.parentNode.leftNode == this);
        }

        public boolean isRightChild() {
            return (this.parentNode != null && this.parentNode.rightNode == this);
        }

        public boolean isOnlyLeftChild() {
            return this.isLeftChild() && this.parentNode.rightNode == null;
        }

        public boolean isOnlyRightChild() {
            return this.isRightChild() && this.parentNode.leftNode == null;
        }

        public void detachChild(TreeNode node) {
            if (this.leftNode == node) {
                node.parentNode = null;
                this.leftNode = null;

            } else if (this.rightNode == node) {
                node.parentNode = null;
                this.rightNode = null;
            }
        }

        /**
         * Removes a target child node a parent, attaching the target's children
         * to the target's parent.
         * 
         * @param node
         */
        public void pop(TreeNode node) {
            if (this.leftNode == node) {
                TreeNode predecessor = this.leftNode;
                TreeNode successor = null;
                if (predecessor.hasOneChild()) {
                    if (predecessor.leftNode != null) {
                        successor = predecessor.leftNode;
                    } else {
                        successor = predecessor.rightNode;
                    }
                }

                predecessor.parentNode = null;
                this.attachLeftChild(successor);

            } else if (this.rightNode == node) {
                TreeNode predecessor = this.rightNode;
                TreeNode successor = null;
                if (predecessor.hasOneChild()) {
                    if (predecessor.leftNode != null) {
                        successor = predecessor.leftNode;
                    } else {
                        successor = predecessor.rightNode;
                    }

                }
                predecessor.parentNode = null;
                this.attachRightChild(successor);
            }
        }

        public void attachLeftChild(TreeNode node) {
            if (node != null) {
                TreeNode oldParent = node.parentNode;
                if (oldParent != null) {
                    oldParent.detachChild(node);
                }
                node.parentNode = this;
            }
            this.leftNode = node;
        }

        public void attachRightChild(TreeNode node) {
            if (node != null) {
                TreeNode oldParent = node.parentNode;
                if (oldParent != null) {
                    oldParent.detachChild(node);
                }
                node.parentNode = this;
            }
            this.rightNode = node;
        }

        @SuppressWarnings("unused")
        public void pivot() {
            if (this.hasNoChildren()) {
                if (this.isOnlyLeftChild() && this.parentNode.isOnlyLeftChild()
                        && this.parentNode.parentNode.isLeftChild()) {
                    // can pivot right
                    pivotRight();
                } else if (this.isOnlyRightChild() && this.parentNode.isOnlyRightChild()
                        && this.parentNode.parentNode.isRightChild()) {
                    // can pivot left
                    pivotLeft();
                }
            }
        }

        public void pivotRight() {
            TreeNode pivot = this.parentNode;
            TreeNode newRightnode = this.parentNode.parentNode;
            TreeNode newParentNode = this.parentNode.parentNode.parentNode;
            // attach pivot to new right node;
            pivot.rightNode = newRightnode;
            // detach pivot from new right node
            newRightnode.leftNode = null;
            // attach new right node to pivot
            newRightnode.parentNode = pivot;
            // attach pivot to new parent
            pivot.parentNode = newParentNode;
            // attach new parent to pivot
            newParentNode.leftNode = pivot;
        }

        public void pivotLeft() {
            TreeNode pivot = this.parentNode;
            TreeNode newLeftNode = this.parentNode.parentNode;
            TreeNode newParentNode = this.parentNode.parentNode.parentNode;
            // attach pivot to new left node;
            pivot.leftNode = newLeftNode;
            // detach pivot from new left node
            newLeftNode.leftNode = null;
            // attach new left node to pivot
            newLeftNode.parentNode = pivot;
            // attach pivot to new parent
            pivot.parentNode = newParentNode;
            // attach new parent to pivot
            newParentNode.rightNode = pivot;
        }

        public int getChildCount() {
            int result = 0;
            if (this.leftNode != null) {
                result++;
            }
            if (this.rightNode != null) {
                result++;
            }
            return result;
        }

        public Integer findMaxDepth() {
            Integer result = 0;
            try {
                int leftDepth = 0;
                if (this.leftNode != null) {
                    leftDepth += this.leftNode.findMaxDepth();
                }

                int rightDepth = 0;
                if (this.rightNode != null) {
                    rightDepth += this.rightNode.findMaxDepth();
                }

                result += Math.max(leftDepth, rightDepth) + 1;

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return result;
        }

        public Integer findMinDepth() {
            Integer result = 0;

            Integer leftDepth = 0;
            if (this.leftNode != null) {
                leftDepth = this.leftNode.findMinDepth();
            }

            Integer rightDepth = 0;
            if (this.rightNode != null) {
                rightDepth = this.rightNode.findMinDepth();
            }

            result += Math.min(leftDepth, rightDepth) + 1;

            return result;
        }

        public void insert(int num) {
            if (num < this.value) {
                if (this.leftNode == null) {
                    this.leftNode = new TreeNode(this, num);
                } else {
                    this.leftNode.insert(num);
                }
            } else if (num > this.value) {
                if (this.rightNode == null) {
                    this.rightNode = new TreeNode(this, num);
                } else {
                    this.rightNode.insert(num);
                }
            }
        }

        public Long get(Integer num) {
            Long result = -1L;
            if (num.equals(this.value)) {
                result = this.timestamp;
            } else if (this.leftNode != null && num < this.value) {
                result = this.leftNode.get(num);
            } else if (this.rightNode != null && num > this.value) {
                result = this.rightNode.get(num);
            }
            return result;
        }

        public boolean remove(Integer num) {
            boolean result = false;
            if (this.leftNode != null && num <= this.value) {
                if (this.leftNode.value.equals(num)) {
                    result = this.removeLeftNode();
                } else {
                    result = this.leftNode.remove(num);
                }
            } else if (this.rightNode != null && num >= this.value) {
                if (this.rightNode.value.equals(num)) {
                    result = this.removeRightNode();
                } else {
                    result = this.rightNode.remove(num);
                }
            }
            return result;
        }

        private boolean removeLeftNode() {
            boolean result = false;
            TreeNode predecessor = this.leftNode;
            TreeNode successor = null;
            if (predecessor.hasNoChildren()) {
                result = true;
            } else if (predecessor.hasOneChild()) {
                if (predecessor.leftNode != null) {
                    this.attachLeftChild(predecessor.leftNode);
                    result = true;

                } else if (predecessor.rightNode != null) {
                    this.attachLeftChild(predecessor.rightNode);
                    result = true;
                }

            } else if (predecessor.hasTwoChildren()) {
                // find nodes successor
                successor = findSuccessorRightMost(predecessor.leftNode);
                if (successor == null) {
                    successor = predecessor.leftNode;
                }
                // detach the successor
                // successor has at most 1 child
                if (successor.parentNode != null) {
                    successor.parentNode.pop(successor);
                }
                successor.attachLeftChild(predecessor.leftNode);
                successor.attachRightChild(predecessor.rightNode);
                
                this.attachLeftChild(successor);
                result = true;
            }
            predecessor.parentNode = null;
            this.leftNode = successor;

            return result;
        }

        private boolean removeRightNode() {
            boolean result = false;
            TreeNode predecessor = this.rightNode;
            TreeNode successor = null;
            if (predecessor.hasNoChildren()) {
                result = true;
            } else if (predecessor.hasOneChild()) {
                if (predecessor.leftNode != null) {
                    this.attachRightChild(predecessor.leftNode);
                    result = true;
                } else if (predecessor.rightNode != null) {
                    this.attachRightChild(predecessor.rightNode);
                    result = true;
                }
            } else if (predecessor.hasTwoChildren()) {
                // find nodes successor
                successor = findSuccessorLeftMost(predecessor.rightNode);
                if (successor == null) {
                    successor = predecessor.leftNode;
                }
                // detach the successor
                if (successor.parentNode != null) {
                    successor.parentNode.pop(successor);
                }

                successor.attachLeftChild(predecessor.leftNode);

                successor.attachRightChild(predecessor.rightNode);

                this.attachRightChild(successor);
                result = true;
            }
            predecessor.parentNode = null;
            this.rightNode = successor;

            return result;
        }

        private TreeNode findSuccessorRightMost(TreeNode current) {
            if (current != null) {
                while (current.rightNode != null) {
                    if (current.rightNode != null) {
                        current = current.rightNode;
                    }
                }
            }
            return current;
        }

        private TreeNode findSuccessorLeftMost(TreeNode current) {
            if (current != null) {
                while (current.leftNode != null) {
                    if (current.leftNode != null) {
                        current = current.leftNode;
                    }
                }
            }
            return current;
        }

    }

    // public static void main(String[] args) throws FileNotFoundException,
    // IOException {
    // File file = new File(args[0]);
    // FileReader fReader = new FileReader(file);
    // BufferedReader bufferedReader = new BufferedReader(fReader);
    // TreeStructure tree = new BinaryTree();
    // Random rng = new Random(42);
    // ArrayList<Integer> nodesToRemove = new ArrayList<>();
    // ArrayList<Integer> nodesToGet = new ArrayList<>();
    // String line = bufferedReader.readLine();
    // while (line != null) {
    // Integer lineInt = Integer.parseInt(line);
    // tree.insert(lineInt);
    // Integer rand = rng.nextInt(10);
    // if (rand < 5)
    // nodesToRemove.add(lineInt);
    // else if (rand >= 5)
    // nodesToGet.add(lineInt);
    // line = bufferedReader.readLine();
    // }
    // bufferedReader.close();
    // for (int i = 0; i < 10; i++) {
    // System.out.println(nodesToGet.get(i) + " inserted at " +
    // tree.get(nodesToGet.get(i)));
    // }
    // System.out.println("Max depth: " + tree.findMaxDepth());
    // System.out.println("Min depth: " + tree.findMinDepth());
    // for (Integer num : nodesToRemove) {
    // tree.remove(num);
    // }
    // for (int i = 0; i < 10; i++) {
    // System.out.println(nodesToGet.get(i) + " inserted at " +
    // tree.get(nodesToGet.get(i)));
    // }
    // System.out.println("Max depth: " + tree.findMaxDepth());
    // System.out.println("Min depth: " + tree.findMinDepth());
    // }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        File file = new File(args[0]);
        FileReader fReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fReader);
        TreeStructure tree = new BinaryTree();
        Random rng = new Random(42);
        ArrayList<Integer> nodesToRemove = new ArrayList<>();
        ArrayList<Integer> nodesToGet = new ArrayList<>();
        String line = bufferedReader.readLine();
        while (line != null) {
            Integer lineInt = Integer.parseInt(line);
            tree.insert(lineInt);
            Integer rand = rng.nextInt(10);
            if (rand < 5 && nodesToRemove.size() < 5)
                nodesToRemove.add(lineInt);
            else if (rand >= 5)
                nodesToGet.add(lineInt);
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
        for (int i = 0; i < 10; i++) {
            System.out.println(nodesToGet.get(i) + " inserted at " + tree.get(nodesToGet.get(i)));
        }
        System.out.println("Max depth: " + tree.findMaxDepth());
        System.out.println("Min depth: " + tree.findMinDepth());
        for (Integer num : nodesToRemove) {
            tree.remove(num);
        }
        for (int i = 0; i < 10; i++) {
            System.out.println(nodesToGet.get(i) + " inserted at " + tree.get(nodesToGet.get(i)));
        }
        System.out.println("Max depth: " + tree.findMaxDepth());
        System.out.println("Min depth: " + tree.findMinDepth());
    }

}
