package cs250.hw4;

import java.io.*;
import java.util.*;

import javax.management.modelmbean.InvalidTargetObjectTypeException;

public class BTree implements TreeStructure {

    private static final int ORDER = 64;

    private BTreePage root;

    @Override
    public void insert(Integer num) {
        if (root == null) {
            root = new BTreePage(num);
        } else {
            root.insert(num);
        }
    }

    @Override
    public Boolean remove(Integer num) {
        return false;
    }

    @Override
    public Long get(Integer num) {
        return -1L;
    }

    @Override
    public Integer findMaxDepth() {
        return -1;
    }

    @Override
    public Integer findMinDepth() {
        return -1;
    }

    private class BTreePage {

        ArrayList<BTreePage> pages;
        BTreePage parent;

        private Integer key;
        private long timestamp;

        public BTreePage(Integer key) {
            super();
            this.key = key;
            this.timestamp = System.nanoTime();
            this.pages = new ArrayList<>();
            this.parent = null;
        }

        public BTreePage(Integer key, BTreePage parent) {
            super();
            this.key = key;
            this.timestamp = System.nanoTime();
            this.pages = new ArrayList<>();
            this.parent = parent;
        }

        private int getCapacity() {
            return ORDER;
        }

        public void insert(Integer num) {
            if (this.pages.isEmpty()) {
                BTreePage newPage = new BTreePage(num, this);
                this.pages.add(newPage);
                this.pages.sort(comparator);
            } else if (this.pages.size() < getCapacity()) {
                BTreePage newPage = new BTreePage(num, this);
                this.pages.add(newPage);
                for (int n = 0; n < this.pages.size(); n++) {
                    if (n == 0) {
                        if (num < this.pages.get(n).key) {
                            this.pages.get(n).insert(num);
                            break;
                        }
                    } else if (n == this.pages.size() - 1) {

                    } else {
                        if (this.pages.get(n).key <= num && num < this.pages.get(n + 1).key) {
                            this.pages.get(n).insert(num);
                        }
                    }
                }
            } else {
                // TODO: at capacity must split
                if (this.parent == null) {
                    BTreePage newPage = new BTreePage(num, this.parent);

                    // split this.pages
                    this.insert(num);
                } else {

                    Integer targetOccupancy = getCapacity() / 2;

                    List<BTreePage> low = this.pages.subList(0, targetOccupancy);
                    List<BTreePage> high = this.pages.subList(targetOccupancy, this.pages.size() - 1);

                    Integer lowKey = low.get(0).key;
                    Integer highKey = high.get(0).key;

                    if (num >= lowKey && num < highKey) {
                        BTreePage newPage = new BTreePage(num, low.get(0));
                        low.add(newPage);
                        low.sort(comparator);
                    } else if (num >= highKey) {
                        BTreePage newPage = new BTreePage(num, high.get(0));
                        high.add(newPage);
                        high.sort(comparator);
                    }

                    this.pages = new ArrayList<>(low);

                    BTreePage newPage = high.get(0);
                    newPage.pages = new ArrayList<>(high);

                    if (this.parent != null) {
                        int currIndex = this.parent.pages.indexOf(this);
                        this.parent.pages.add(currIndex + 1, newPage);

                        this.parent.insert(num);
                    }
                }
            }
        }

        public Boolean remove(Integer num) {
            return false;
        }

        public Long get(Integer num) {
            return -1L;
        }

        public Integer findMaxDepth() {
            return -1;
        }

        public Integer findMinDepth() {
            return -1;
        }

    }

    Comparator<BTreePage> comparator = new Comparator<BTree.BTreePage>() {

        @Override
        public int compare(BTreePage arg0, BTreePage arg1) {
            return arg0.key - arg1.key;
        }

    };

    public static void main(String[] args) throws FileNotFoundException, IOException {
        File file = new File(args[0]);
        FileReader fReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fReader);
        TreeStructure tree = new BTree();
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