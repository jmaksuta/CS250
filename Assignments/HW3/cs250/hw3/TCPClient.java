package cs250.hw3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.InvalidParameterException;
import java.util.Random;

public class TCPClient {

    private String hostName;
    private int hostPort;

    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    private int numberOfMessages;
    private int generatorSeed;
    private long senderSum;
    private long receiverSum;
    private int numOfSentMessages;
    private int numOfReceivedMessages;
    private Random random;

    public TCPClient() {
        super();
        this.hostName = "";
        this.hostPort = -1;
        this.numberOfMessages = 0;
        this.generatorSeed = 0;
        this.senderSum = 0;
        this.receiverSum = 0;
        this.numOfSentMessages = 0;
        this.numOfReceivedMessages = 0;
        this.socket = null;
        this.dataOutputStream = null;
        this.dataInputStream = null;
        this.random = new Random(this.generatorSeed);
    }

    public TCPClient(String hostName, int hostPort) {
        this();
        this.hostName = hostName;
        this.hostPort = hostPort;
    }

    public static void main(String[] args) {
        TCPClient client = null;
        try {
            Main.validateCommandLineArgs(args, new IMain() {

                @Override
                public void validateCommandLineArgs() throws Exception {
                    String parameter1Name = "server-host";
                    String parameter2Name = "sever-port";

                    String usageMsg = String.format("Usage is: %s <%s> <%s>", TCPClient.class.getSimpleName(),
                            parameter1Name, parameter2Name);
                    String parameter1Message = String.format("%s is a valid ipv4/ipv6 address or a domain name.",
                            parameter1Name);
                    String parameter2Message = String.format("%s is a valid port number (range is between %d and %d).",
                            parameter2Name, Main.MIN_PORT_NUMBER, Main.MAX_PORT_NUMBER);
                    if (args.length != 2) {
                        throw new InvalidParameterException("The number of parameters is incorrect.\n" + usageMsg);
                    }
                    if (!Common.isValidHost(args[0])) {
                        throw new InvalidParameterException(
                                String.format("the %s provided is invalid.\n", parameter1Name) + parameter1Message);
                    }

                    if (!Common.isInteger(args[1])
                            || !Common.isValidRange(Common.toInteger(args[1]), Main.MIN_PORT_NUMBER,
                                    Main.MAX_PORT_NUMBER)) {
                        throw new InvalidParameterException(
                                String.format("the %s provided is invalid.\n", parameter2Name) + parameter2Message);
                    }
                }

            });

            client = new TCPClient(args[0], Common.toInteger(args[1]));
            client.startup();
            client.run();

        } catch (Exception e) {
            Common.writeLineToConsole(e.getMessage());
        } finally {
            if (client != null) {
                client.cleanup();
            }
        }
    }

    public void startup() throws Exception {
        this.socket = new Socket(this.hostName, this.hostPort);
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        this.dataInputStream = new DataInputStream(socket.getInputStream());

        byte[] registration = receiveRegistration();

        parseRegistration(registration);

        writeRegistrationConfirmationToConsole();
    }

    private void parseRegistration(byte[] registration) {
        this.numberOfMessages = Common.toInteger(Common.subbyte(registration, 0, 4));
        this.generatorSeed = Common.toInteger(Common.subbyte(registration, 4, 4));
        this.random = new Random(this.generatorSeed);
    }

    private void writeRegistrationConfirmationToConsole() {
        String settingMessage = "%s = %d\n";
        StringBuilder builder = new StringBuilder();
        builder.append("Received Config\n");
        builder.append(String.format(settingMessage, "number of messages", this.numberOfMessages));
        builder.append(String.format(settingMessage, "seed", this.generatorSeed));

        Common.writeToConsole(builder.toString());
    }

    public void sendMessage(String message) throws Exception {
        this.dataOutputStream.writeBytes(message);
    }

    public void sendMessage(int toSend) throws Exception {
        byte[] message = new byte[] {};

        message = Common.append(message, Common.intToByteArray(toSend));

        this.dataOutputStream.write(message);
        this.dataOutputStream.flush();
    }

    public int receiveMessage() throws Exception {
        int message = this.dataInputStream.readInt();
        return message;
    }

    public byte[] receiveRegistration() throws Exception {
        byte[] bytesReceived = new byte[] {};
        do {
            bytesReceived = Common.append(bytesReceived, (byte) this.dataInputStream.read());

        } while (this.dataInputStream.available() > 0);

        return bytesReceived;
    }

    public byte[] receive() throws Exception {
        byte[] bytesReceived = new byte[] {};
        do {
            bytesReceived = Common.append(bytesReceived, (byte) this.dataInputStream.read());

        } while (this.dataInputStream.available() > 0);

        return bytesReceived;
    }

    public void run() throws Exception {
        // startReadThread();
        // sleep for 10 seconds.
        Thread.sleep(10000);
        // print status message to console.
        Common.writeLineToConsole("Starting to send messages to server...");
        // clear the counters
        clearCounters();
        // TODO: start read thread
        // startReadThread();
        // send the messages to the server and maintain the sum
        generateAndSendMessages();
        // print the summary to the console.
        writeSummary();
    }

    private void clearCounters() {
        this.senderSum = 0;
        this.numOfSentMessages = 0;
    }

    private void generateAndSendMessages() throws Exception {
        for (int n = 0; n < this.numberOfMessages; n++) {
            // generate a number
            int toSend = this.random.nextInt();
            this.senderSum += toSend;
            sendMessage(toSend);
            this.numOfSentMessages++;
            int receivedMessage = receiveMessage();
            this.receiverSum += receivedMessage;
            this.numOfReceivedMessages++;
        }
    }

    private void writeSummary() {
        Common.writeLineToConsole("Finished sending messages to server.");
        Common.writeLineToConsole(String.format("Total messages sent: %d", this.numOfSentMessages));
        Common.writeLineToConsole(String.format("Sum of messages sent: %d", this.senderSum));
    }

    private void startReadThread() {

        Reader reader = new Reader(new ReaderListener() {

            @Override
            public void OnBytesReceived(byte[] bytesReceived) {
                try {
                    int messageReceived = Common.toInteger(bytesReceived);
                    receiverSum += messageReceived;

                    numOfReceivedMessages++;

                } catch (Exception e) {
                    // do nothing.
                    System.out.println("DEBUG:" + e.getMessage());
                }
            }

        });
        reader.setPriority(Thread.NORM_PRIORITY);
        reader.start();

    }

    public void cleanup() {
        try {
            if (this.socket != null) {
                this.socket.close();
            }
            if (this.dataOutputStream != null) {
                this.dataOutputStream.close();
            }
            if (this.dataInputStream != null) {
                this.dataInputStream.close();
            }

        } catch (Exception e) {
            // do nothing.
        }
    }

    private interface ReaderListener {
        void OnBytesReceived(byte[] bytesReceived);
    }

    private class Reader extends Thread {
        boolean isRunning;

        private ReaderListener listener;

        private Reader() {
            super();
            this.isRunning = false;
        }

        public Reader(ReaderListener listener) {
            this();
            this.listener = listener;
        }

        @Override
        public void run() {
            // super.run();

            isRunning = true;
            while (isRunning) {
                try {
                    byte[] bytesReceived = receive();
                    raiseOnBytesReceived(bytesReceived);
                } catch (IOException e) {
                    isRunning = false;
                } catch (Exception e) {
                    isRunning = false;
                }
            }
        }

        private void raiseOnBytesReceived(byte[] bytesReceived) {
            if (this.listener != null) {
                this.listener.OnBytesReceived(bytesReceived);
            }
        }

    }

}
