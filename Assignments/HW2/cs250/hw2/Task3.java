package cs250.hw2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

public class Task3 extends Task {

    private TreeSet<Integer> treeSet;
    private LinkedList<Integer> linkedList;

    public Task3(String[] args) throws InvalidArgumentException {
        super(args);
        ArrayList<Integer> list = new ArrayList<>();
        loadList(list, this.getSize());

        this.treeSet = new TreeSet<>(new ArrayList<Integer>(list));
        this.linkedList = new LinkedList<>(new ArrayList<Integer>(list));
    }

    public void loadList(List<Integer> list, int size) {
        list.clear();
        Random random = new Random();
        for (int n = 0; n < size; n++) {
            list.add(random.nextInt(getSize()));
        }
    }

    @Override
    public void run() {

        ExperimentResult avgTimeInSet = runSetExperiment();
        ExperimentResult avgTimeInList = runListExperiment();

        System.out.println("Task 3");
        System.out.println(String.format("Avg time to find in set: %.2f nanoseconds",
                avgTimeInSet.getAverageTime()));
        System.out.println(String.format("Avg time to find in list: %.2f nanoseconds",
                avgTimeInList.getAverageTime()));

        // writeToFile("Task3Set.csv", avgTimeInSet.toString());
        // writeToFile("Task3List.csv", avgTimeInList.toString());
    }

    private ExperimentResult runSetExperiment() {
        ExperimentResult result = new ExperimentResult();

        for (int n = 0; n < this.getExperiments(); n++) {
            Integer toFind = getNumbertoFind();
            long startTime = 0L;
            long endTime = 0L;

            startTime = System.nanoTime();
            this.treeSet.contains(toFind);
            endTime = System.nanoTime();

            result.elapsedTimes.add((endTime - startTime));
        }
        result.count = this.getExperiments();
        return result;
    }

    private ExperimentResult runListExperiment() {
        ExperimentResult result = new ExperimentResult();

        for (int n = 0; n < this.getExperiments(); n++) {
            Integer toFind = getNumbertoFind();
            long startTime = 0L;
            long endTime = 0L;

            startTime = System.nanoTime();
            this.linkedList.contains(toFind);
            endTime = System.nanoTime();

            result.elapsedTimes.add((endTime - startTime));
        }
        result.count = this.getExperiments();
        return result;
    }

    private Integer getNumbertoFind() {
        Random random = new Random();
        return random.nextInt(this.getSize());
    }

    private class ExperimentResult {
        long count;
        ArrayList<Long> elapsedTimes;

        private ExperimentResult() {
            super();
            this.count = 0L;
            elapsedTimes = new ArrayList<>();
        }

        private double getAverageTime() {
            return this.getElapsedTime() / this.count;
        }

        public double getElapsedTime() {
            double result = 0.0;
            for (Long elapsedTime : this.elapsedTimes) {
                result += elapsedTime;
            }
            return result;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append(String.format("%s\n", "elapsed time"));
            for (int n = 0; n < count; n++) {
                builder.append(String.format("%d\n", this.elapsedTimes.get(n)));
            }
            return builder.toString();
        }

    }

}
