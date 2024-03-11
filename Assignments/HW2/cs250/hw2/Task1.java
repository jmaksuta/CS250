package cs250.hw2;

import java.util.ArrayList;

public class Task1 extends Task {

    private volatile int loopCounter;

    private double regularAverageElapsed;
    private double volatileAverageElapsed;
    private double regularAverageSum;
    private double volatileAverageSum;

    public Task1(String[] args) throws InvalidArgumentException {
        super(args);
        this.regularAverageElapsed = 0.0;
        this.volatileAverageElapsed = 0.0;
        this.regularAverageSum = 0.0;
        this.volatileAverageSum = 0.0;
    }

    @Override
    public void run() {
        int iterations = this.getExperiments();

        ExperimentResult regularExperimentResult = runRegular(iterations);
        ExperimentResult volatileExperimentResult = runVolatile(iterations);

        this.regularAverageElapsed = regularExperimentResult.getAverageTimePerElementInSeconds();
        this.volatileAverageElapsed = volatileExperimentResult.getAverageTimePerElementInSeconds();
        this.regularAverageSum = regularExperimentResult.getAverageSumPerElement();
        this.volatileAverageSum = volatileExperimentResult.getAverageSumPerElement();

        System.out.println("Task 1");
        System.out.println(String.format("Regular:  %.5f seconds", this.regularAverageElapsed));
        System.out.println(String.format("Volatile: %.5f seconds", this.volatileAverageElapsed));
        System.out.println(String.format("Avg regular sum:  %.2f", this.regularAverageSum));
        System.out.println(String.format("Avg volatile sum: %.2f", this.volatileAverageSum));
        System.out.println("");

        // writeToFile("Task1Regular.csv", regularExperimentResult.toString());
        // writeToFile("Task1Volatile.csv", volatileExperimentResult.toString());
    }

    private ExperimentResult runRegular(int iterations) {
        ExperimentResult result = new ExperimentResult();
        long startTime = 0L;
        long endTime = 0L;

        long experimentTotal = 0L;
        for (int n = 0; n < iterations; n++) {
            startTime = System.nanoTime();
            experimentTotal = getRunningTotal(getSize());
            endTime = System.nanoTime();

            result.elapsedTimes.add((endTime - startTime));
            result.elementValues.add(experimentTotal);
        }
        result.count = iterations;

        return result;
    }

    private long getRunningTotal(int count) {
        long runningTotal = 0;
        for (int n = 0; n < count; n++) {
            if (n % 2 == 0) {
                runningTotal += n;
            } else {
                runningTotal -= n;
            }
        }
        return runningTotal;
    }

    private ExperimentResult runVolatile(int iterations) {
        ExperimentResult result = new ExperimentResult();
        long startTime = 0L;
        long endTime = 0L;

        long experimentTotal = 0L;

        for (int n = 0; n < iterations; n++) {
            startTime = System.nanoTime();
            experimentTotal = getVolatileRunningTotal(getSize());
            endTime = System.nanoTime();

            result.elapsedTimes.add((endTime - startTime));
            result.elementValues.add(experimentTotal);
        }
        result.count = iterations;
        
        return result;
    }

    private long getVolatileRunningTotal(int count) {
        long runningTotal = 0;
        for (loopCounter = 0; loopCounter < count; loopCounter++) {
            int loopVal = loopCounter;
            if (loopVal % 2 == 0) {
                runningTotal += loopVal;
            } else {
                runningTotal -= loopVal;
            }
        }
        return runningTotal;
    }

    private class ExperimentResult {
        long count;
        ArrayList<Long> elapsedTimes;
        ArrayList<Long> elementValues;

        private ExperimentResult() {
            super();
            this.count = 0L;
            elapsedTimes = new ArrayList<>();
            elementValues = new ArrayList<>();
        }

        private double getAverageTimePerElementInSeconds() {
            return this.getElapsedTimeInSeconds() / this.count;
        }

        private double getAverageSumPerElement() {
            return this.getSumTotal() / this.count;
        }

        public double getElapsedTime() {
            double result = 0.0;
            for (Long elapsedTime : this.elapsedTimes) {
                result += elapsedTime;
            }
            return result;
        }

        public double getElapsedTimeInSeconds() {
            return (getElapsedTime() / 1000000000);
        }

        public double getSumTotal() {
            double result = 0.0;
            for (Long elementValue : this.elementValues) {
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
