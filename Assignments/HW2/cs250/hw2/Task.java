package cs250.hw2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public abstract class Task {

    private String[] commandLineArgs;
    private int size;
    private int experiments;
    private long seed;

    public int getSize() {
        return this.size;
    }
    
    public int getExperiments() {
        return this.experiments;
    }

    public long getSeed() {
        return this.seed;
    }

    public Task() {
        super();
        this.commandLineArgs = new String[] {};
        this.size = 0;
        this.experiments = 0;
        this.seed = 0;
    }

    public Task(String[] args) throws InvalidArgumentException {
        this();
        this.commandLineArgs = clone(args);
        parseArguments();
    }

    private String[] clone(String[] array) {
        return Arrays.copyOf(array, array.length);
    }

    private void parseArguments() throws InvalidArgumentException {
        if (!(this.commandLineArgs.length == 3)) {
            throw new InvalidArgumentException();
        }
        this.size = Integer.parseInt(this.commandLineArgs[0]);
        this.experiments = Integer.parseInt(this.commandLineArgs[1]);
        this.seed = Integer.parseInt(this.commandLineArgs[2]);
    }
    
    public void writeToFile(String fileName, String toWrite) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(toWrite);

            writer.close();
        } catch (IOException e) {
            // do nothing.
        }
    }

    public abstract void run();

}
