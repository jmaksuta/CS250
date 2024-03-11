package cs250.hw2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Task2 extends Task {

    public Task2(String[] args) throws InvalidArgumentException {
        super(args);
    }

    @Override
    public void run() {
        int iterations = getExperiments();

        Integer[] array = new Integer[this.getSize()];

        loadArray(array);

        int tenPercentSize = (int) (((double) array.length) * 0.10);

        int bottomTenPercentUpperLimit = tenPercentSize;
        int topTenPercentLowerLimit = array.length - tenPercentSize;

        SummaryResult summaryResultKnown = runKnownExperiment(array, iterations, bottomTenPercentUpperLimit);
        SummaryResult summaryResultUnknown = runUnknownExperiment(array, iterations, topTenPercentLowerLimit);

        System.out.println("Task 2");
        System.out.println(String.format("Avg time to access known element: %.2f nanoseconds",
                summaryResultKnown.getAverageTimePerExperiment()));
        System.out.println(String.format("Avg time to access random element: %.2f nanoseconds",
                summaryResultUnknown.getAverageTimePerExperiment()));
        System.out.println(
                String.format("Sum: %.2f",
                        summaryResultKnown.getSumOfAllElements() + summaryResultUnknown.getSumOfAllElements()));

        System.out.println("");
        // summaryResultKnown.writeToFile("Task2Known.csv");
        // summaryResultUnknown.writeToFile("Task2Unknown.csv");
    }

    private SummaryResult runKnownExperiment(Integer[] array, int iterations, int limit) {
        SummaryResult result = new SummaryResult();

        ExperimentResult experimentResult;

        for (int n = 0; n < iterations; n++) {
            experimentResult = accessEachElement(array, limit);
            result.experimentResults.add(experimentResult);
            ;
        }

        return result;
    }

    private ExperimentResult accessEachElement(Integer[] array, int limit) {
        ExperimentResult result = new ExperimentResult();
        long startTime = 0L;
        long endTime = 0L;
        Integer elementValue;

        for (int n = 0; n < limit; n++) {
            startTime = System.nanoTime();
            elementValue = accessElement(array, n);
            endTime = System.nanoTime();
            result.elapsedTimes.add((endTime - startTime));
            result.elementValues.add(elementValue);
            result.count += 1;
        }
        return result;
    }

    private SummaryResult runUnknownExperiment(Integer[] array, int iterations, int limit) {
        SummaryResult result = new SummaryResult();

        int size = array.length - limit;

        for (int n = 0; n < iterations; n++) {
            ExperimentResult experimentResult = accessRandomElement(array, size, limit);

            result.experimentResults.add(experimentResult);
        }

        return result;
    }

    private ExperimentResult accessRandomElement(Integer[] array, int size, int limit) {
        ExperimentResult experimentResult = new ExperimentResult();
        Random random = new Random();
        int elementIndex = random.nextInt(size) + limit;

        long startTime = 0L;
        long endTime = 0L;
        Integer elementValue;

        startTime = System.nanoTime();
        elementValue = accessElement(array, elementIndex);
        endTime = System.nanoTime();

        experimentResult.count = 1;
        experimentResult.elementValues.add(elementValue);
        experimentResult.elapsedTimes.add((endTime - startTime));

        return experimentResult;
    }

    private Integer accessElement(Integer[] array, int index) {
        return array[index];
    }

    private void loadArray(Integer[] array) {
        Random random = new Random(this.getSeed());
        for (int n = 0; n < array.length; n++) {
            array[n] = random.nextInt();
        }
    }

    private class SummaryResult {
        ArrayList<ExperimentResult> experimentResults;

        private SummaryResult() {
            super();
            this.experimentResults = new ArrayList<>();
        }

        public double getAverageTimePerExperiment() {
            double result = 0;
            for (ExperimentResult experimentResult : this.experimentResults) {
                result += experimentResult.getAverageTimePerElement();
            }
            return result / this.experimentResults.size();
        }

        public double getSumOfAllElements() {
            double result = 0;
            for (ExperimentResult experimentResult : this.experimentResults) {
                result += experimentResult.getSumTotal();
            }
            return result;
        }

        @SuppressWarnings("unused")
        public void writeToFile(String fileName) {

            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(fileName));

                for (ExperimentResult result : this.experimentResults) {
                    writer.write(String.format("%f,%f\n", result.getAverageTimePerElement(),
                            result.getSumTotal()));
                }

                writer.close();
            } catch (IOException e) {
                // do nothing.
            }
        }

    }

    private class ExperimentResult {
        long count;
        ArrayList<Long> elapsedTimes;
        ArrayList<Integer> elementValues;

        private ExperimentResult() {
            super();
            this.count = 0L;
            elapsedTimes = new ArrayList<>();
            elementValues = new ArrayList<>();
        }

        private double getAverageTimePerElement() {
            return this.getElapsedTime() / this.count;
        }

        public double getElapsedTime() {
            double result = 0.0;
            for (Long elapsedTime : this.elapsedTimes) {
                result += elapsedTime;
            }
            return result;
        }

        public double getSumTotal() {
            double result = 0.0;
            for (Integer elementValue : this.elementValues) {
                result += elementValue;
            }
            return result;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append(String.format("%s,%s\n", "elapsed time", "value"));
            for (int n = 0; n < count; n++) {
                builder.append(String.format("%d,%d\n", this.elapsedTimes.get(n), this.elementValues.get(n)));
            }
            return builder.toString();
        }

    }

}
