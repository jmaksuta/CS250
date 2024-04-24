package cs250.hw4;

import java.io.*;
import java.util.*;

import cs250.hw4.TreeStructure;

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
        // if (this.root == null) {
        //     this.root = new TreeNode(null, num);
        // } else {
        //     TreeNode current = this.root;
        //     while (current != null) {
        //         if (num < current.value) {
        //             if (current.leftNode == null) {
        //                 current.leftNode = new TreeNode(current, num);
        //                 break;
        //             } else {
        //                 current = current.leftNode;
        //             }
        //         } else if (num > current.value) {
        //             if (current.rightNode == null) {
        //                 current.rightNode = new TreeNode(current, num);
        //                 break;
        //             } else {
        //                 current = current.rightNode;
        //             }
        //         }
        //     }
        // }
    }

    @Override
    public Boolean remove(Integer num) {
        Boolean result = false;
        if (this.root != null) {
            if (this.root.value == num) {
                // remove the root node.
                TreeNode predecessor = this.root;
                TreeNode successor = null;
                if (predecessor.hasNoChildren()) {
                    // update parent
                    this.root = null;
                    result = true;
                } else if (predecessor.hasOneChild()) {
                    if (predecessor.leftNode != null) {
                        this.root = predecessor.leftNode;
                        result = true;
                    } else if (predecessor.rightNode != null) {
                        this.root = predecessor.rightNode;
                        result = true;
                    }
                } else if (predecessor.hasTwoChildren()) {
                    // find nodes successor
                    successor = findSuccessorRight(predecessor.rightNode);
                    if (successor.rightNode != null) {
                        successor.parentNode.leftNode = successor.rightNode;
                    }
                    successor.leftNode = predecessor.leftNode;
                    successor.rightNode = predecessor.rightNode;
                    successor.parentNode = null;
                    this.root = successor;
                    result = true;
                }
            } else {
                TreeNode current = this.root;

                while (current != null) {
                    if (num < current.value) {
                        if (current.leftNode != null && current.leftNode.value.equals(num)) {
                            // result = removeNode(current.leftNode);
                            TreeNode predecessor = current.leftNode;
                            TreeNode successor = null;
                            if (predecessor.hasNoChildren()) {
                                // update parent
                                current.leftNode = null;
                                result = true;
                                break;
                            } else if (predecessor.hasOneChild()) {
                                if (predecessor.leftNode != null) {
                                    current.leftNode = predecessor.leftNode;
                                    result = true;
                                    break;
                                } else if (predecessor.rightNode != null) {
                                    current.leftNode = predecessor.rightNode;
                                    result = true;
                                    break;
                                }
                            } else if (predecessor.hasTwoChildren()) {
                                // find nodes successor
                                successor = findSuccessorRight(predecessor.rightNode);
                                if (successor.rightNode != null) {
                                    successor.parentNode.leftNode = successor.rightNode;
                                }
                                successor.leftNode = predecessor.leftNode;
                                // successor.rightNode = predecessor.rightNode;
                                successor.parentNode = current;
                                current.leftNode = successor;
                                result = true;
                                break;
                            }
                        } else {
                            current = current.leftNode;
                        }
                    } else if (num > current.value) {
                        if (current.rightNode != null && current.rightNode.value.equals(num)) {
                            // result = removeNode(current.rightNode);
                            TreeNode predecessor = current.rightNode;
                            TreeNode successor = null;
                            if (predecessor.hasNoChildren()) {
                                // update parent
                                current.rightNode = null;
                                result = true;
                                break;
                            } else if (predecessor.hasOneChild()) {
                                if (predecessor.leftNode != null) {
                                    current.rightNode = predecessor.leftNode;
                                    result = true;
                                    break;
                                } else if (predecessor.rightNode != null) {
                                    current.rightNode = predecessor.rightNode;
                                    result = true;
                                    break;
                                }
                            } else if (predecessor.hasTwoChildren()) {
                                // find nodes successor
                                successor = findSuccessorRight(predecessor.rightNode);
                                if (successor.rightNode != null) {
                                    successor.parentNode.leftNode = successor.rightNode;
                                }
                                successor.leftNode = predecessor.leftNode;
                                successor.rightNode = predecessor.rightNode;
                                successor.parentNode = current;
                                current.rightNode = successor;
                                result = true;
                                break;
                            }
                        } else {
                            current = current.rightNode;
                        }
                    }
                }
            }
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
        // Long result = -1L;
        // TreeNode current = this.root;
        // while (current != null && current.value != num) {
        //     // if (current.value.equals(num)) {
        //     // break;
        //     // } else
        //     if (num < current.value) {
        //         current = current.leftNode;
        //     } else if (num > current.value) {
        //         current = current.rightNode;
        //     }
        // }
        // if (current != null) {
        //     result = current.timestamp;
        // }
        // return result;
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

    @Override
    public Integer findMaxDepth() {
        Integer result = 0;
        if (this.root != null) {
            // result = getMaxDepth(this.root);
            result = this.root.findMaxDepth();
        }
        return result;
    }

    private int getMaxDepth(TreeNode node) {
        int result = 0;
        if (node != null) {
            result += 1;
            int leftDepth = 0;
            if (node.leftNode != null) {
                leftDepth = getMaxDepth(node.leftNode);
            }
            int rightDepth = 0;
            if (node.leftNode != null) {
                rightDepth = getMaxDepth(node.rightNode);
            }
            result += Math.max(leftDepth, rightDepth);
        }
        return result;
    }

    @Override
    public Integer findMinDepth() {
        Integer result = 0;
        if (this.root != null) {
            // result = getMinDepth(this.root);
            result = this.root.findMinDepth();
        }
        return result;
    }

    private int getMinDepth(TreeNode node) {
        int result = 0;
        if (node != null) {
            result += 1;
            int leftDepth = 0;
            if (node.leftNode != null) {
                leftDepth = getMinDepth(node.leftNode);
            }
            int rightDepth = 0;
            if (node.leftNode != null) {
                rightDepth = getMinDepth(node.rightNode);
            }
            result += Math.min(leftDepth, rightDepth);
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

        public void pivot() {
            boolean canPivot = false;
            if (this.hasOneChild() && this.parentNode.hasOneChild()) {
                if (this.leftNode == null) {

                } else if (this.rightNode == null) {

                }
            }
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
            } else if (num < this.value) {
                result = this.leftNode.get(num);
            } else if (num > this.value) {
                result = this.rightNode.get(num);
            }
            return result;
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
