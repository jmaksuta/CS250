package cs250.hw4;

import java.io.File;
import java.util.Scanner;

public class BinaryTree {

    private String fileName;
    private BTree binaryTree;

    private Scanner scanner;

    public static void main(String[] args) {
        try {
            if (args.length != 1) {
                throw new IllegalArgumentException("the argument(s) provided are not valid");
            }
            String fileName = args[0];

            BinaryTree binaryTree = new BinaryTree(fileName);
            binaryTree.run();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public BinaryTree(String fileName) {
        super();
        this.fileName = fileName;
        this.binaryTree = new BTree();
    }

    public void run() throws Exception {
        try {
            scanner = new Scanner(new File(this.fileName));

            while (scanner.hasNextLine()) {
                String value = scanner.nextLine();
                Integer intValue = Integer.parseInt(value);

                this.binaryTree.insert(intValue);
            }

            System.out.println(String.format("Max depth: %d", this.binaryTree.findMaxDepth()));
            System.out.println(String.format("Min depth: %d", this.binaryTree.findMinDepth()));

        } catch (Exception e) {
            throw e;
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

}
