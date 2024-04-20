package cs250.hw4;

import java.io.*;
import java.util.*;

import cs250.hw4.TreeStructure;

public class BinaryTree implements TreeStructure {

    // private String fileName;
    private TreeNode root;

    // private Scanner scanner;

    // public static void main(String[] args) {
    // try {
    // if (args.length != 1) {
    // throw new IllegalArgumentException("the argument(s) provided are not valid");
    // }
    // String fileName = args[0];

    // BinaryTree binaryTree = new BinaryTree(fileName);
    // binaryTree.run();

    // } catch (Exception e) {
    // System.out.println(e.getMessage());
    // }
    // }

    public BinaryTree() {
        super();
        // this.fileName = fileName;
        this.root = new TreeNode();
    }

    // public void run() throws Exception {
    // try {
    // scanner = new Scanner(new File(this.fileName));

    // while (scanner.hasNextLine()) {
    // String value = scanner.nextLine();
    // Integer intValue = Integer.parseInt(value);

    // this.binaryTree.insert(intValue);
    // }

    // System.out.println(String.format("Max depth: %d",
    // this.binaryTree.findMaxDepth()));
    // System.out.println(String.format("Min depth: %d",
    // this.binaryTree.findMinDepth()));

    // } catch (Exception e) {
    // throw e;
    // } finally {
    // if (scanner != null) {
    // scanner.close();
    // }
    // }
    // }

    @Override
    public void insert(Integer num) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public Boolean remove(Integer num) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'remove'");
        return false;
    }

    @Override
    public Long get(Integer num) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'get'");
        return -1L;
    }

    @Override
    public Integer findMaxDepth() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method
        // 'findMaxDepth'");
        return -1;
    }

    @Override
    public Integer findMinDepth() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method
        // 'findMinDepth'");
        return -1;
    }

    private class TreeNode {
    
        private TreeNode leftNode;
        private Integer value;
        private long timestamp;
        private TreeNode rightNode;
        
    }

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
            if (rand < 5)
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
