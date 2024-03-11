package cs250.hw3;

public class Configuration {

    private int numberOfMessages;
    private int seed;

    public int getNumberOfMessages() {
        return numberOfMessages;
    }

    public int getSeed() {
        return seed;
    }

    public Configuration() {
        super();
        this.numberOfMessages = 0;
        this.seed = 0;
    }

    public Configuration(int numberOfMessages, int seed) {
        super();
        this.numberOfMessages = numberOfMessages;
        this.seed = seed;
    }

    @Override
    public String toString() {
        return String.format("%d %d", this.numberOfMessages, this.seed);
    }

}
