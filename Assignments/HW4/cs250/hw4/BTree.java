package cs250.hw4;

import java.io.*;
import java.util.*;

public class BTree implements TreeStructure {

    private static final int ORDER = 4; // 64;

    private Integer key;
    private long timestamp;

    private ArrayList<BTree> pages;

    public BTree() {
        super();
    }

    public BTree(Integer key) {
        super();
        this.key = key;
        this.timestamp = System.nanoTime();
        // this.pages = new ArrayList<>();
        // this.pages.add(this);
    }

    private boolean isRoot() {
        return (this.key == null);
    }

    private boolean isRootUninitialized() {
        return (isRoot() && this.pages == null);
    }

    private boolean isInnerNode() {
        return (!isRoot() && this.pages != null);
    }

    private boolean isLeaf() {
        return (!isRoot() && this.pages == null);
    }

    private int getCapacity() {
        return ORDER;
    }

    private int getOccupancy() {
        int result = 0;
        if (this.pages != null) {
            result = this.pages.size();
        }
        return result;
    }

    private Integer[] getSeparatorKeys() {
        List<Integer> list = new ArrayList<>();
        if (this.pages != null) {
            if (this.pages.size() == 1) {
                list.add(this.pages.get(0).key);
            } else if (this.pages.size() == 2) {
                list.add(this.pages.get(1).key);
            } else if (this.pages.size() > 1) {
                for (int n = 1; n < this.pages.size(); n++) {
                    list.add(this.pages.get(n).key);
                }
            }
        }
        return list.toArray(new Integer[] {});
    }

    private void initializeRoot(Integer num) {
        // root node
        BTree newPage = new BTree(num);
        if (this.pages == null) {
            this.pages = new ArrayList<>();
            // add the default node
            this.pages.add(new BTree(null));
        }
        this.pages.add(newPage);
        this.pages.sort(comparator);
    }

    private void addPage(BTree page) {
        if (this.pages == null) {
            this.pages = new ArrayList<>();
            // add the default node
            this.pages.add(new BTree(null));
        }
        this.pages.add(page);
        this.pages.sort(comparator);
        this.key = this.pages.get(1).key;
    }

    @Override
    public void insert(Integer num) {
        if (this.isRootUninitialized()) {
            // root node
            initializeRoot(num);
        } else {
            if (this.isLeaf()) {
                this.addPage(new BTree(this.key));
                this.addPage(new BTree(num));
            } else if (this.getOccupancy() < this.getCapacity()) {
                // add to this.pages

                // its less than capacity, find the separator, and insert it.
                BTree separator = findSeparator(num);
                if (separator == null) {
                    separator = new BTree(num);
                    this.addPage(separator);

                    // } else if (separator.isLeaf()) {
                    // // make this node an inner node
                    // // initialize the pages and insert the value.

                    // this.addPage(new BTree(separator.key));
                    // this.addPage(new BTree(num));

                } else if (separator.getOccupancy() < separator.getCapacity()) {
                    separator.insert(num);
                } else {
                    // must split
                    this.split(separator);
                    this.insert(num);
                }

            } else {
                // must split
                this.split(this);
                this.insert(num);
            }
        }

    }

    private BTree findSeparator(int num) {
        BTree result = null;
        Integer[] separatorKeys = getSeparatorKeys();
        if (num < separatorKeys[0]) {
            if (this.pages.size() > 1 && this.pages.get(1).key == separatorKeys[0]) {
                result = this.pages.get(0);
            }
        } else if (this.pages.size() > 0 && num >= separatorKeys[separatorKeys.length - 1]) {
            result = this.pages.get(this.pages.size() - 1);
        } else {
            result = binarySearch(num, separatorKeys);
        }
        return result;
    }

    private BTree findValue(int num) {
        BTree result = null;
        boolean found = false;

        BTree current = this;
        while (!found || current.isLeaf()) {
            Integer[] separatorKeys = getSeparatorKeys();
            if (num < separatorKeys[0]) {
                current = current.pages.get(0);
            } else if (current.pages.size() > 0 && num >= separatorKeys[separatorKeys.length - 1]) {
                current = current.pages.get(current.pages.size() - 1);
            } else {
                current = binarySearch(num, separatorKeys);
            }
            if (current != null && (current.key != null && current.key.equals(num))) {
                result = current;
                found = true;
            }
        }
        
        return result;
    }

    private BTree binarySearch(int num, Integer[] separatorKeys) {

        return binarySearch(num, separatorKeys, 0, separatorKeys.length - 1);
    }

    private BTree binarySearch(int num, Integer[] separatorKeys, int startIndex, int endIndex) {
        BTree result = null;

        int medianIndex = endIndex - startIndex;
        int medianKey = separatorKeys[medianIndex];

        if (num >= medianKey && num < separatorKeys[medianIndex + 1]) {
            // to find the page index from separator index, add 1 to separator index
            result = this.pages.get(medianIndex + 1);
        } else if (num < medianKey) {
            result = binarySearch(num, separatorKeys, startIndex, medianIndex - 1);
        } else if (num > medianKey) {
            result = binarySearch(num, separatorKeys, medianIndex + 1, endIndex);
        }
        return result;
    }

    private void split(BTree bTree) {
        Integer targetOccupancy = getCapacity() / 2;

        List<BTree> low = bTree.pages.subList(0, targetOccupancy);
        List<BTree> high = bTree.pages.subList(targetOccupancy, bTree.pages.size() - 1);

        bTree.pages = new ArrayList<>(low);

        BTree newPage = high.get(0);
        newPage.pages = new ArrayList<>(high);

        this.addPage(newPage);
    }

    @Override
    public Boolean remove(Integer num) {
        return false;
    }

    @Override
    public Long get(Integer num) {
        Long result = -1L;
        BTree found = findValue(num);
        if (found != null) {
            result = found.timestamp;
        }
        return result;
    }

    @Override
    public Integer findMaxDepth() {
        return -1;
    }

    @Override
    public Integer findMinDepth() {
        return -1;
    }

    Comparator<BTree> comparator = new Comparator<BTree>() {

        @Override
        public int compare(BTree arg0, BTree arg1) {
            int result = 0;
            if (arg1.key == null || arg0.key > arg1.key) {
                result = 1;
            } else if (arg0.key == null || arg0.key < arg1.key) {
                result = -1;
            }
            return result;
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