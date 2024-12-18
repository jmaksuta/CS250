package cs250.hw3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.security.InvalidParameterException;
import java.util.ArrayList;
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

        receiveRegistration();

        writeRegistrationConfirmationToConsole();
    }

    private void writeRegistrationConfirmationToConsole() {
        String settingMessage = "%s = %d\n";
        StringBuilder builder = new StringBuilder();
        builder.append("Received Config\n");
        builder.append(String.format(settingMessage, "number of messages", this.numberOfMessages));
        builder.append(String.format(settingMessage, "seed", this.generatorSeed));

        Common.writeToConsole(builder.toString());
    }

    public void sendMessage(int toSend) throws Exception {
        this.dataOutputStream.writeInt(toSend);
        this.dataOutputStream.flush();
    }

    public int receiveMessage() throws Exception {
        int message = this.dataInputStream.readInt();
        return message;
    }

    public void receiveRegistration() throws Exception {
        ArrayList<Integer> received = new ArrayList<>();
        for (int n = 0; n < 2; n++) {
            received.add(this.dataInputStream.readInt());
        }
        this.numberOfMessages = received.get(0);
        this.generatorSeed = received.get(1);
        this.random = new Random(this.generatorSeed);
    }

    public byte[] receive() throws Exception {
        byte[] bytesReceived = new byte[] {};
        do {
            bytesReceived = Common.append(bytesReceived, (byte) this.dataInputStream.read());

        } while (this.dataInputStream.available() > 0);

        return bytesReceived;
    }

    public void run() throws Exception {
        // sleep for 10 seconds.
        Thread.sleep(10000);
        // print status message to console.
        Common.writeLineToConsole("Starting to send messages to server...");
        // clear the counters
        clearCounters();
        // send the messages to the server and maintain the sum
        generateAndSendMessages();
        // print the summary to the console.
        writeSendSummary();
        // listen for messages
        listenAndReceiveMessages();
        // print the summary to the console
        writeReceiveSummary();
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
        }
    }

    private void writeSendSummary() {
        Common.writeLineToConsole("Finished sending messages to server.");
        Common.writeLineToConsole(String.format("Total messages sent: %d", this.numOfSentMessages));
        Common.writeLineToConsole(String.format("Sum of messages sent: %d", this.senderSum));
    }

    private void listenAndReceiveMessages() throws Exception {
        Common.writeLineToConsole("Starting to listen for messages from server...");
        receiveMessages();
        Common.writeLineToConsole("Finished listening for messages from server.");
    }

    private void receiveMessages() throws Exception {
        for (int n = 0; n < this.numberOfMessages; n++) {
            int receivedNumber = this.dataInputStream.readInt();
            this.receiverSum += receivedNumber;
            this.numOfReceivedMessages++;
        }
    }

    private void writeReceiveSummary() {
        Common.writeLineToConsole(String.format("Total messages received: %d", this.numOfReceivedMessages));
        Common.writeLineToConsole(String.format("Sum of messages received: %d", this.receiverSum));
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

}
