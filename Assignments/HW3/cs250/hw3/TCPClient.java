package cs250.hw3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.security.InvalidParameterException;
import java.util.ArrayList;

public class TCPClient {

    private String hostName;
    private int hostPort;

    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    private int numberOfMessages;
    private int generatorSeed;
    @SuppressWarnings("unused")
    private long senderSum;

    public TCPClient() {
        super();
        this.hostName = "";
        this.hostPort = -1;
        this.numberOfMessages = 0;
        this.generatorSeed = 0;
        this.senderSum = 0;
        this.socket = null;
        this.dataOutputStream = null;
        this.dataInputStream = null;
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

        String registration = receiveMessage();

        parseRegistration(registration);

        writeRegistrationConfirmationToConsole();
    }

    private void parseRegistration(String registration) {
        String[] registrationParts = registration.split(" ");
        this.numberOfMessages = Integer.parseInt(registrationParts[0]);
        this.generatorSeed = Integer.parseInt(registrationParts[1]);
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

    public String receiveMessage() throws Exception {
        ArrayList<Byte> bytesReceived = new ArrayList<>();
        do {
            bytesReceived.add((byte)this.dataInputStream.read());
        } while (this.dataInputStream.available() > 0);

        String message = new String(Common.listToArray(bytesReceived));
        return message;
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
