package cs250.hw3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Random;

public class TCPServer {

    private int portNumber;
    private int seed;
    private int numberOfMessages;

    private ServerSocket serverSocket;
    private ArrayList<ClientConnection> clientConnections;
    private Random random;

    public TCPServer() {
        super();
        this.portNumber = -1;
        this.seed = 0;
        this.numberOfMessages = 0;
        this.serverSocket = null;
        this.clientConnections = new ArrayList<>();
        this.random = new Random();
    }

    public TCPServer(int portNumber, int seed, int numberOfMessages) {
        this();
        this.portNumber = portNumber;
        this.seed = seed;
        this.numberOfMessages = numberOfMessages;
    }

    public static void main(String[] args) {
        TCPServer server = null;
        try {
            Main.validateCommandLineArgs(args, new IMain() {

                @Override
                public void validateCommandLineArgs() throws Exception {
                    String parameter1Name = "port-number";
                    String parameter2Name = "seed";
                    String parameter3Name = "number-Of-Messages";

                    String usageMsg = String.format("Usage is: %s <%s> <%s> <%s>", TCPServer.class.getSimpleName(),
                            parameter1Name, parameter2Name, parameter3Name);
                    String validIntegerMessage = "%s is a valid integer (range is between %d and %d).";
                    String parameter1Message = String.format("%s is a valid port number (range is between %d and %d).",
                            parameter1Name, Main.MIN_PORT_NUMBER, Main.MAX_PORT_NUMBER);
                    String parameter2Message = String.format(validIntegerMessage,
                            parameter2Name, Main.MIN_UNSIGNED_INTEGER, Main.MAX_UNSIGNED_INTEGER);
                    String parameter3Message = String.format(validIntegerMessage,
                            parameter3Name, Main.MIN_UNSIGNED_INTEGER, Main.MAX_UNSIGNED_INTEGER);
                    if (args.length != 3) {
                        throw new InvalidParameterException("The number of parameters is incorrect.\n" + usageMsg);
                    }

                    if (!Common.isInteger(args[0])
                            || !Common.isValidRange(Common.toInteger(args[0]), Main.MIN_PORT_NUMBER,
                                    Main.MAX_PORT_NUMBER)) {
                        throw new InvalidParameterException(
                                String.format("the %s provided is invalid.\n", parameter1Name) + parameter1Message);
                    }

                    if (!Common.isInteger(args[1])
                            || !Common.isValidRange(Common.toInteger(args[1]), Main.MIN_UNSIGNED_INTEGER,
                                    Main.MAX_UNSIGNED_INTEGER)) {
                        throw new InvalidParameterException(
                                String.format("the %s provided is invalid.\n", parameter2Name) + parameter2Message);
                    }

                    if (!Common.isInteger(args[2])
                            || !Common.isValidRange(Common.toInteger(args[2]), Main.MIN_UNSIGNED_INTEGER,
                                    Main.MAX_UNSIGNED_INTEGER)) {
                        throw new InvalidParameterException(
                                String.format("the %s provided is invalid.\n", parameter3Name) + parameter3Message);
                    }

                }

            });

            server = new TCPServer(Common.toInteger(args[0]), Common.toInteger(args[1]),
                    Common.toInteger(args[2]));
            server.startup();
            server.run();

        } catch (Exception e) {
            Common.writeLineToConsole(e.getMessage());
        } finally {
            if (server != null) {
                server.cleanup();
            }
        }
    }

    public void startup() throws Exception {
        serverSocket = new ServerSocket(this.portNumber);

        random = new Random(this.seed);

        Common.writeLineToConsole(String.format("IP Address: %s\nPort Number: %d",
                InetAddress.getLocalHost().getHostAddress(), this.portNumber));
    }

    public void run() throws Exception {
        // todo: this may be unnecessary to run this in a thread, also it may be
        // unnecessary to expect more than 2 clients.
        Common.writeLineToConsole("Waiting for clients...");
        while (clientConnections.size() < 2) {
            connectAndRegisterClient();
        }
        Common.writeLineToConsole("Clients Connected!");

        sendConfigurationToClients();
    }

    private void connectAndRegisterClient() throws Exception {
        Socket clientSocket = serverSocket.accept();

        Configuration configuration = new Configuration(this.numberOfMessages, this.random.nextInt());

        ClientConnection connection = new ClientConnection(clientSocket, configuration);
        this.clientConnections.add(connection);

        // connection.run();
    }

    private void sendConfigurationToClients() throws Exception {
        for (ClientConnection connection : this.clientConnections) {
            connection.sendMessage(connection.configuration);

            Common.writeLineToConsole(String.format("%s %d", connection.clientSocket.getLocalAddress(),
                    connection.configuration.getSeed()));
        }
    }

    public void cleanup() {
        try {
            if (this.serverSocket != null) {
                this.serverSocket.close();
            }
            for (ClientConnection connection : this.clientConnections) {
                connection.cleanup();
            }

        } catch (Exception e) {
            // do nothing.
        }
    }

    private class ClientConnection extends Thread {
        boolean isRunning = false;

        private Configuration configuration;
        private Socket clientSocket;
        private DataOutputStream dataOutputStream;
        private DataInputStream dataInputStream;

        private ClientConnection() {
            super();
            this.configuration = new Configuration();
        }

        public ClientConnection(Socket clientSocket, Configuration configuration) throws Exception {
            this();
            this.clientSocket = clientSocket;
            if (this.clientSocket != null) {
                this.dataOutputStream = new DataOutputStream(this.clientSocket.getOutputStream());
                this.dataInputStream = new DataInputStream(this.clientSocket.getInputStream());
            }
            this.configuration = configuration;
        }

        @Override
        public void run() {
            super.run();
            isRunning = true;
            while (isRunning) {

            }
        }

        @SuppressWarnings("unused")
        public void sendMessage(String message) throws Exception {
            this.dataOutputStream.writeBytes(message);
            this.dataOutputStream.flush();
        }

        @SuppressWarnings("unused")
        public void sendMessage(int message) throws Exception {
            this.dataOutputStream.writeInt(message);
            this.dataOutputStream.flush();
        }

        public void sendMessage(Configuration configuration) throws Exception {
            byte[] message = new byte[] {};

            message = Common.append(message, Common.intToByteArray(configuration.getNumberOfMessages()));
            message = Common.append(message, Common.intToByteArray(configuration.getSeed()));

            this.dataOutputStream.write(message);
            this.dataOutputStream.flush();
        }

        @SuppressWarnings("unused")
        public String receiveMessage() throws Exception {
            String message = "";
            byte[] bytesReceived = this.dataInputStream.readAllBytes();
            message = new String(bytesReceived, Charset.forName("utf-8"));
            return message;
        }

        public void cleanup() {
            try {
                if (this.clientSocket != null) {
                    this.clientSocket.close();
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

}
