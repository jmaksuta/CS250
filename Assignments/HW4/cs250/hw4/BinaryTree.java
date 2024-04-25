package cs250.hw4;

import java.io.*;
import java.lang.invoke.ConstantBootstraps;
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
                removeRoot();
            } else {
                this.root.remove(num);
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
                predecessor.parentNode = null;
                result = true;
            } else if (predecessor.rightNode != null) {
                this.root = predecessor.rightNode;
                predecessor.parentNode = null;
                result = true;
            }
        } else if (predecessor.hasTwoChildren()) {
            // find nodes successor
            successor = findSuccessorRight(predecessor.rightNode);

            successor.parentNode.attachLeftChild(successor.rightNode);
            
            successor.attachLeftChild(predecessor.leftNode);
            
            successor.attachRightChild(predecessor.rightNode);

            successor.parentNode = null;
            this.root = successor;

            result = true;
        }
        return result;
    }

    public TreeNode findSuccessorLeft(TreeNode current) {
        while (current.rightNode != null) {
            current = current.rightNode;
        }
        return current;
    }

    public TreeNode findSuccessorRight(TreeNode current) {
        while (current.leftNode != null) {
            current = current.leftNode;
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

    public TreeNode search(Integer num) {
        TreeNode current = this.root;
        while (current != null && current.value != num) {
            if (num < current.value) {
                current = current.leftNode;
            } else if (num > current.value) {
                current = current.rightNode;
            }
        }
        return current;
    }

    public static void inorder(TreeStructure tree) {
        inorder(tree);
    }

    // public void inorder() {
    // inorder(this.root);
    // }

    public void inorder(TreeNode node) {
        if (node != null) {
            inorder(node.leftNode);
            System.out.println(node.value);
            inorder(node.rightNode);
        }
    }

    // @Override
    // public Integer findMaxDepth() {
    // Integer result = 0;
    // try {
    // result = findMaxDepth(this.root);
    // } catch (Exception e) {
    // System.out.println(e.getMessage());
    // e.printStackTrace();
    // }
    // return result;
    // }

    // private Integer findMaxDepth(TreeNode current) throws Exception {
    // int result = 0;
    // try {
    // if (current.leftNode == null && current.rightNode == null) {
    // result = 1;
    // } else {
    // current.pivot();
    // int leftDepth = 0;
    // int rightDepth = 0;
    // if (current.leftNode != null) {
    // TreeNode lefTreeNode = current.leftNode;
    // leftDepth = findMaxDepth(lefTreeNode);
    // }
    // if (current.rightNode != null) {
    // TreeNode rightTreeNode = current.rightNode;
    // rightDepth = findMaxDepth(rightTreeNode);
    // }
    // result += Math.max(leftDepth, rightDepth) + 1;
    // }
    // } catch (Exception e) {
    // throw e;
    // }
    // return result;
    // }

    @Override
    public Integer findMaxDepth() {
        int result = 0;

        Stack<TreeNode> stack = new Stack<TreeNode>();

        TreeNode current = this.root;

        stack.push(current);

        while (!stack.isEmpty()) {

            while (current != null) {
                stack.push(current);
                current = current.leftNode;
            }
            if (current == null && !stack.isEmpty()) {
                TreeNode popped = stack.pop();
                if (popped.hasNoChildren()) {
                    int depth = popped.getDepth();
                    if (depth > result) {
                        result = depth;
                    }
                }
                // System.out.print(popped.toString());
                current = popped.rightNode;
            }
        }

        return result;
    }

    @Override
    public Integer findMinDepth() {
        int result = Integer.MAX_VALUE;

        Stack<TreeNode> stack = new Stack<TreeNode>();
        TreeNode current = this.root;
        stack.push(current);

        while (!stack.isEmpty()) {
            while (current != null) {
                stack.push(current);
                current = current.leftNode;
            }
            if (current == null && !stack.isEmpty()) {
                TreeNode popped = stack.pop();
                if (popped.hasNoChildren()) {
                    int depth = popped.getDepth();
                    if (depth < result) {
                        result = depth;
                    }
                }
                // System.out.print(popped.toString());
                current = popped.rightNode;
            }
        }

        return result;
    }

    // @Override
    // public Integer findMinDepth() {
    // Integer result = 0;
    // if (this.root != null) {
    // result = findMinDepth(this.root);
    // }
    // return result;
    // }

    private Integer findMinDepth(TreeNode current) {
        int result = 0;
        if (current.leftNode == null && current.rightNode == null) {
            result = 1;
        } else {
            int leftDepth = 0;
            int rightDepth = 0;
            if (current.leftNode != null) {
                TreeNode lefTreeNode = current.leftNode;
                leftDepth = findMinDepth(lefTreeNode);
            }
            if (current.rightNode != null) {
                TreeNode rightTreeNode = current.rightNode;
                rightDepth = findMinDepth(rightTreeNode);
            }
            result += Math.min(leftDepth, rightDepth) + 1;
        }
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

        public int getDepth() {
            int result = 0;
            TreeNode current = this;
            while (current != null) {
                result += 1;
                current = current.parentNode;
            }
            return result;
        }

        public void swapNodes(TreeNode oldNode, TreeNode newNode) {
            if (this.leftNode == oldNode) {

                detachChild(oldNode);
                attachLeftChild(newNode);
                swapChildren(oldNode, newNode);

            } else if (this.rightNode == oldNode) {
                detachChild(oldNode);
                attachRightChild(newNode);
                swapChildren(oldNode, newNode);
            }
        }

        private void swapChildren(TreeNode oldNode, TreeNode newNode) {

            TreeNode oldLeftNode = oldNode.leftNode;
            TreeNode oldRightNode = oldNode.rightNode;

            TreeNode newLeftNode = newNode.leftNode;
            TreeNode newRightNode = newNode.rightNode;

            oldNode.detachChild(oldLeftNode);
            oldNode.detachChild(oldRightNode);

            newNode.detachChild(newLeftNode);
            newNode.detachChild(newRightNode);

            oldNode.attachLeftChild(newLeftNode);
            oldNode.attachRightChild(newRightNode);

            newNode.attachLeftChild(oldLeftNode);
            newNode.attachRightChild(oldRightNode);

        }

        public void detachChild(TreeNode node) {
            if (this.leftNode == node) {
                this.leftNode.parentNode = null;
                this.leftNode = null;
            } else if (this.rightNode == node) {
                this.rightNode.parentNode = null;
                this.rightNode = null;
            }
        }

        public void attachLeftChild(TreeNode node) {
            if (node != null) {
                node.parentNode = this;
            }
            this.leftNode = node;
        }

        public void attachRightChild(TreeNode node) {
            if (node != null) {
                node.parentNode = this;
            }
            this.rightNode = node;
        }

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
                result += 1;

                Integer leftDepth = 0;
                if (this.leftNode != null) {
                    leftDepth += this.leftNode.findMaxDepth();
                }

                Integer rightDepth = 0;
                if (this.rightNode != null) {
                    rightDepth += this.rightNode.findMaxDepth();
                }

                result += Math.max(leftDepth, rightDepth);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return result;
        }

        public Integer findMinDepth() {
            Integer result = 0;
            result += 1;

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

        public void insert(int num) {
            if (num < this.value) {
                if (this.leftNode == null) {
                    this.leftNode = new TreeNode(this, num);
                    pivot();
                } else {
                    this.leftNode.insert(num);
                }
            } else if (num > this.value) {
                if (this.rightNode == null) {
                    this.rightNode = new TreeNode(this, num);
                    pivot();
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
            if (this.leftNode != null && num < this.value) {
                if (this.leftNode.value.equals(num)) {
                    result = this.removeLeftNode();
                } else {
                    this.leftNode.remove(num);
                }
            } else if (this.rightNode != null && num > this.value) {
                if (this.rightNode.value.equals(num)) {
                    result = this.removeRightNode();
                } else {
                    this.rightNode.remove(num);
                }
            }
            return result;
        }

        // private boolean removeNode(TreeNode predecessor) {
        // boolean result = false;
        // TreeNode parent = predecessor.parentNode;
        // if (parent == null) {
        // // TODO: root node
        // this.removeNode(predecessor);
        // } else if (predecessor == parent.leftNode) {
        // // left node
        // result = parent.removeLeftNode();
        // } else if (predecessor == parent.rightNode) {
        // // right node
        // result = parent.removeRightNode();
        // }
        // return result;
        // }

        private boolean removeLeftNode() {
            boolean result = false;
            TreeNode predecessor = this.leftNode;
            TreeNode successor = null;
            if (predecessor.hasNoChildren()) {
                // update parent
                this.leftNode = null;
                predecessor.parentNode = null;
                // pivot();
                result = true;
            } else if (predecessor.hasOneChild()) {
                if (predecessor.leftNode != null) {
                    this.attachRightChild(predecessor.leftNode);
                    // pivot();
                    result = true;
                } else if (predecessor.rightNode != null) {
                    this.attachLeftChild(predecessor.rightNode);
                    // pivot();
                    result = true;
                }
            } else if (predecessor.hasTwoChildren()) {
                // find nodes successor
                successor = findSuccessorLeft(predecessor.rightNode);
                // detach the successor
                successor.parentNode.attachRightChild(successor.leftNode);

                successor.attachLeftChild(predecessor.leftNode);
                successor.attachRightChild(predecessor.rightNode);

                this.attachLeftChild(successor);
                // pivot();
                result = true;
            }
            return result;
        }

        private boolean removeRightNode() {
            boolean result = false;
            TreeNode predecessor = this.rightNode;
            TreeNode successor = null;
            if (predecessor.hasNoChildren()) {
                // update parent
                this.rightNode = null;
                predecessor.parentNode = null;

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
                successor = findSuccessorRight(predecessor.rightNode);
                // detach the successor
                successor.parentNode.attachLeftChild(successor.rightNode);

                successor.attachLeftChild(predecessor.leftNode);

                successor.attachRightChild(predecessor.rightNode);

                this.attachRightChild(successor);
                result = true;
            }
            return result;
        }

        private TreeNode findSuccessorLeft(TreeNode current) {
            while (current.rightNode != null) {
                current = current.rightNode;
            }
            return current;
        }

        private TreeNode findSuccessorRight(TreeNode current) {
            while (current.leftNode != null) {
                current = current.leftNode;
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
