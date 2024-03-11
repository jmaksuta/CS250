package cs250.hw2;

public class Memory {

    private String[] commandLineArgs;

    public static void main(String[] args) {
        try {
            Memory memory = new Memory(args);
            memory.run();

        } catch (Exception e) {
            writeConsole(e.getMessage());
        }
    }

    public static void writeConsole(String msg) {
        System.out.println(msg);
    }

    public Memory() {
        super();
        this.commandLineArgs = new String[] {};
    }

    public Memory(String[] commandLineArgs) {
        this();
        this.commandLineArgs = commandLineArgs;
    }

    public void run() throws InvalidArgumentException {

        runTask(new Task1(this.commandLineArgs));

        runTask(new Task2(this.commandLineArgs));

        runTask(new Task3(this.commandLineArgs));
    }

    private void runTask(Task theTask) {
        theTask.run();
    }

}