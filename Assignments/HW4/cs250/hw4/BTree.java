package cs250.hw4;

import java.io.*;
import java.util.*;

public class BTree implements TreeStructure {

    private static final int ORDER = 64;

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
        return (this.pages == null);
    }

    private boolean everyChildIsALeaf() {
        boolean result = false;
        if (this.pages == null) {
            result = true;
        } else {
            result = true;
            for (BTree node : this.pages) {
                if (node.pages != null) {
                    result = false;
                    break;
                }
            }
        }
        return result;
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

    private int getThreshold() {
        return (int) (this.getCapacity() / 2);
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
        }
        this.pages.add(newPage);
        this.pages.sort(comparator);
    }

    private void addPage(BTree page) {
        if (this.pages == null) {
            this.pages = new ArrayList<>();
        }
        this.pages.add(page);
        this.pages.sort(comparator);
        this.key = this.pages.get(0).key;
    }

    private boolean removePage(BTree page) {
        boolean result = false;
        if (this.pages != null && this.pages.contains(page)) {
            result = this.pages.remove(page);
            this.pages.sort(comparator);
            if (this.pages.size() > 0) {
                this.key = this.pages.get(0).key;
            }
        }
        if (this.pages != null && this.pages.size() == 0) {
            this.pages = null;
        }
        return result;
    }

    @Override
    public void insert(Integer num) {
        if (this.isRootUninitialized()) {
            // root node
            initializeRoot(num);
        } else {
            BTree insertNode = findInsertionNode(num);

            if (insertNode.everyChildIsALeaf()) {

                if (insertNode.getOccupancy() < insertNode.getCapacity()) {
                    insertNode.addPage(new BTree(num));
                } else {
                    this.split(insertNode);
                    this.insert(num);
                }
            } else {
                insertNode.insert(num);
            }

        }

    }

    private BTree findInsertionNode(Integer num) {
        BTree result = null;

        BTree current = this;

        if (!current.everyChildIsALeaf()) {
            Integer[] separatorKeys = current.getSeparatorKeys();
            if (num < separatorKeys[0]) {
                current = current.pages.get(0);
            } else if (current.pages.size() > 0 && num >= separatorKeys[separatorKeys.length - 1]) {
                current = current.pages.get(current.pages.size() - 1);
            } else {
                current = current.binarySearch(num, separatorKeys);
            }
        }

        result = current;

        return result;
    }

    private BTree getLeafToRemove(Integer num) {
        BTree result = null;

        BTree current = this;

        if (current.pages != null && current.everyChildIsALeaf()) {
            Integer[] separatorKeys = current.getSeparatorKeys();
            if (num < separatorKeys[0]) {
                current = current.pages.get(0);
            } else if (current.pages.size() > 0 && num >= separatorKeys[separatorKeys.length - 1]) {
                current = current.pages.get(current.pages.size() - 1);
            } else {
                current = current.binarySearch(num, separatorKeys);
            }
        }

        result = current;

        return result;
    }

    private BTree findSeparator(int num) {
        BTree result = null;
        Integer[] separatorKeys = getSeparatorKeys();
        if (num < separatorKeys[0]) {
            result = this.pages.get(0);
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
            if (current.pages != null) {
                Integer[] separatorKeys = current.getSeparatorKeys();
                if (num < separatorKeys[0]) {
                    current = current.pages.get(0);
                } else if (current.pages.size() > 0 && num >= separatorKeys[separatorKeys.length - 1]) {
                    current = current.pages.get(current.pages.size() - 1);
                } else {
                    current = current.binarySearch(num, separatorKeys);
                }
            }
            if (current != null && (current.key != null && current.key.equals(num))) {
                result = current;
                found = true;
                break;
            }
        }

        return result;
    }

    private BTree binarySearch(int num, Integer[] separatorKeys) {

        return binarySearch(num, separatorKeys, 0, separatorKeys.length - 1);
    }

    private BTree binarySearch(int num, Integer[] separatorKeys, int startIndex, int endIndex) {
        BTree result = null;

        int medianIndex = (startIndex + endIndex) / 2;
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
        List<BTree> high = bTree.pages.subList(targetOccupancy, bTree.pages.size());

        BTree lowTree = new BTree(low.get(0).key);
        lowTree.pages = new ArrayList<>(low);

        BTree highTree = new BTree(high.get(0).key);
        highTree.pages = new ArrayList<>(high);

        if (this.equals(bTree)) {
            this.pages = new ArrayList<>();
        } else {
            if (this.pages.contains(bTree)) {
                // remove the original tree
                this.pages.remove(bTree);
            }
        }

        if (this.getOccupancy() + 2 > this.getCapacity()) {
            // must insert as as internalNode
            BTree internalNode = new BTree(lowTree.key);
            internalNode.addPage(lowTree);
            internalNode.addPage(highTree);

            this.addPage(internalNode);
        } else {
            // add the new pages to this tree.
            this.addPage(lowTree);
            this.addPage(highTree);
        }

    }

    @Override
    public Boolean remove(Integer num) {
        Boolean result = false;
        BTree removalNode = this.findInsertionNode(num);
        if (!removalNode.everyChildIsALeaf()) {
            result = removalNode.remove(num);
        } else {
            BTree leafToRemove = removalNode.getLeafToRemove(num);
            if (leafToRemove != null) {
                result = removalNode.removePage(leafToRemove);
            }
        }
        if (result) {
            if (shouldPerformMerge(removalNode)) {
                this.merge(removalNode);
            }
            if (removalNode.pages == null || removalNode.pages.size() == 0) {
                this.removePage(removalNode);
            }

            if (this.pages != null) {
                this.pages.sort(comparator);
                this.key = this.pages.get(0).key;
            }
        }
        return result;
    }

    private boolean shouldPerformMerge(BTree target) {
        boolean neighborCanMerge = this.leftNeighborCanMergeTarget(target) || this.rightNeighborCanMergeTarget(target);

        return target.isUnderflowCondition() && neighborCanMerge;
    }

    private boolean leftNeighborCanMergeTarget(BTree target) {
        boolean result = false;
        BTree leftNeighbor = getLeftNeighbor(target);
        if (leftNeighbor != null) {
            result = leftNeighbor.canMergeTarget(target);
        }
        return result;
    }

    private boolean leftNeighborCanMergeOnePage(BTree target) {
        boolean result = false;
        BTree leftNeighbor = getLeftNeighbor(target);
        if (leftNeighbor != null) {
            result = leftNeighbor.canMergeOnePage();
        }
        return result;
    }

    private boolean rightNeighborCanMergeOnePage(BTree target) {
        boolean result = false;
        BTree rightNeighbor = getRightNeighbor(target);
        if (rightNeighbor != null) {
            result = rightNeighbor.canMergeOnePage();
        }
        return result;
    }

    private BTree getLeftNeighbor(BTree target) {
        BTree result = null;
        int targetIndex = this.pages.indexOf(target);
        int leftNeighborIndex = targetIndex - 1;
        if (leftNeighborIndex > -1) {
            result = this.pages.get(leftNeighborIndex);
        }
        return result;
    }

    private boolean rightNeighborCanMergeTarget(BTree target) {
        boolean result = false;
        BTree rightNeighbor = getRightNeighbor(target);
        if (rightNeighbor != null) {
            result = rightNeighbor.canMergeTarget(target);
        }
        return result;
    }

    private BTree getRightNeighbor(BTree target) {
        BTree result = null;
        int targetIndex = this.pages.indexOf(target);
        int rightNeighborIndex = targetIndex + 1;
        if (rightNeighborIndex < this.pages.size()) {
            result = this.pages.get(rightNeighborIndex);
        }
        return result;
    }

    private boolean canMergeTarget(BTree target) {
        return (this.getOccupancy() + target.getOccupancy()) < this.getCapacity();
    }

    private boolean canMergeOnePage() {
        return (this.getOccupancy() + 1) < this.getCapacity();
    }

    private boolean isUnderflowCondition() {
        return this.getOccupancy() < this.getThreshold();
    }

    private void merge(BTree bTree) {
        while (bTree.pages != null && bTree.pages.size() > 0 && this.leftNeighborCanMergeOnePage(bTree)) {
            BTree toRemove = bTree.pages.get(0);
            BTree leftNeighbor = this.getLeftNeighbor(bTree);

            boolean wasRemoved = bTree.removePage(toRemove);
            if (wasRemoved) {
                leftNeighbor.addPage(toRemove);
            }
        }

        while (bTree.pages != null && bTree.pages.size() > 0 && this.rightNeighborCanMergeOnePage(bTree)) {
            BTree toRemove = bTree.pages.get(0);
            BTree rightNeighbor = this.getRightNeighbor(bTree);

            boolean wasRemoved = bTree.removePage(toRemove);
            if (wasRemoved) {
                rightNeighbor.addPage(toRemove);
            }
        }

        if (bTree.pages == null || bTree.pages.size() == 0) {
            bTree = null;
        }
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
        Integer result = 0;

        if (this.everyChildIsALeaf()) {
            result = 1;
        } else {
            Integer maxChildDepth = 0;
            if (this.pages != null && this.pages.size() > 0) {
                for (int n = 0; n < this.pages.size(); n++) {
                    Integer childDepth = this.pages.get(n).findMaxDepth();
                    maxChildDepth = Math.max(childDepth, maxChildDepth);
                }
            }
            result += maxChildDepth + 1;
        }
        return result;
    }

    @Override
    public Integer findMinDepth() {
        Integer result = 0;
        if (this.pages == null || this.everyChildIsALeaf()) {
            result = 1;
        } else {
            Integer minChildDepth = Integer.MAX_VALUE;
            if (this.pages != null && this.pages.size() > 0) {
                for (int n = 0; n < this.pages.size(); n++) {
                    Integer childDepth = this.pages.get(n).findMinDepth();
                    minChildDepth = Math.min(childDepth, minChildDepth);
                }
            }
            result += minChildDepth + 1;
        }
        return result;
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