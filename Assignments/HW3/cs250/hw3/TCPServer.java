package cs250.hw3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
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

    private Object lock;

    public TCPServer() {
        super();
        this.portNumber = -1;
        this.seed = 0;
        this.numberOfMessages = 0;
        this.serverSocket = null;
        this.clientConnections = new ArrayList<>();
        this.random = new Random();
        lock = new Object();
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
        Common.writeLineToConsole(String.format("IP Address: %s\nPort Number %d",
                InetAddress.getLocalHost().getHostName() + "/" +
                        InetAddress.getLocalHost().getHostAddress(),
                this.portNumber));

        serverSocket = new ServerSocket(this.portNumber);

        random = new Random(this.seed);
    }

    public void run() throws Exception {
        // todo: this may be unnecessary to run this in a thread, also it may be
        // unnecessary to expect more than 2 clients.
        Common.writeLineToConsole("waiting for client...");
        while (clientConnections.size() < 2) {
            connectAndRegisterClient();
        }
        Common.writeLineToConsole("Clients Connected!");

        sendConfigurationToClients();
        Thread.sleep(10000);
        runClients();

        checkIfEnded();
        Common.writeLineToConsole("Finished listening for client messages.");

        printSummary();
    }

    private void checkIfEnded() {
        boolean isRunning = true;
        while (isRunning) {
            try {
                isRunning = areClientConnectionsRunning();

                Thread.sleep(2000);
            } catch (Exception e) {
                // do nothing.
            }
        }
    }

    private boolean areClientConnectionsRunning() {
        boolean isRunning = false;
        for (ClientConnection clientConnection : this.clientConnections) {
            if (clientConnection.isRunning) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    private void printSummary() {
        for (ClientConnection clientConnection : clientConnections) {
            Common.writeLineToConsole(
                    String.format("%s\n      Messages received: %d\n      Sum received: %d",
                            clientConnection.clientSocket.getInetAddress().getHostName(),
                            clientConnection.getNumOfReceivedMessages(), 
                            clientConnection.getReceiverSum()));
        }
    }

    private void connectAndRegisterClient() throws Exception {
        Socket clientSocket = serverSocket.accept();

        Configuration configuration = new Configuration(this.numberOfMessages, this.random.nextInt());

        ClientConnection connection = new ClientConnection(clientSocket, configuration, new ClientConnectionListener() {

            @Override
            public void onBytesReceived(ClientConnection clientConnection, byte[] bytesReceived) {
                try {
                    int clientIndex = clientConnections.indexOf(clientConnection);
                    for (int n = 0; n < clientConnections.size(); n++) {
                        if (n != clientIndex) {
                            // send to this client
                            ClientConnection connection = clientConnections.get(n);
                            connection.sendMessage(bytesReceived);
                        }
                    }

                } catch (Exception e) {
                    System.out.println("DEBUG: " + e.getMessage());
                }
            }

            @Override
            public void onMessageReceived(ClientConnection clientConnection, int message) {
                try {
                    int clientIndex = clientConnections.indexOf(clientConnection);
                    for (int n = 0; n < clientConnections.size(); n++) {
                        if (n != clientIndex) {
                            // send to this client
                            ClientConnection connection = clientConnections.get(n);
                            connection.sendMessage(message);
                        }
                    }
                } catch (SocketException e) {
                    clientConnection.isRunning = false;
                } catch (Exception e) {
                    System.out.println("DEBUG: " + e.getMessage());
                }
            }

        });
        this.clientConnections.add(connection);

        // connection.run();
    }

    private void sendConfigurationToClients() throws Exception {
        Common.writeLineToConsole("Sending config to clients...");
        for (ClientConnection connection : this.clientConnections) {
            connection.sendMessage(connection.configuration);

            Common.writeLineToConsole(String.format("%s %d", connection.clientSocket.getInetAddress().getHostName(),
                    connection.configuration.getSeed()));
        }
        Common.writeLineToConsole("Finished sending config to clients.");
    }

    private void runClients() {
        Common.writeLineToConsole("Starting to listen for client messages...");
        for (ClientConnection clientConnection : this.clientConnections) {
            clientConnection.start();
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

    private interface ClientConnectionListener {
        void onBytesReceived(ClientConnection clientConnection, byte[] bytesReceived);

        void onMessageReceived(ClientConnection clientConnection, int message);
    }

    private class ClientConnection extends Thread {
        boolean isRunning = false;

        private Configuration configuration;
        private Socket clientSocket;
        private DataOutputStream dataOutputStream;
        private DataInputStream dataInputStream;
        private ClientConnectionListener clientConnectionListener;
        private long receiverSum;
        private int numOfReceivedMessages;

        public long getReceiverSum() {
            return receiverSum;
        }

        public int getNumOfReceivedMessages() {
            return numOfReceivedMessages;
        }

        private ClientConnection() {
            super();
            this.configuration = new Configuration();
            this.receiverSum = 0L;
            this.numOfReceivedMessages = 0;
        }

        public ClientConnection(Socket clientSocket, Configuration configuration,
                ClientConnectionListener clientConnectionListener) throws Exception {
            this();
            this.clientSocket = clientSocket;
            if (this.clientSocket != null) {
                this.dataOutputStream = new DataOutputStream(this.clientSocket.getOutputStream());
                this.dataInputStream = new DataInputStream(this.clientSocket.getInputStream());
            }
            this.configuration = configuration;
            this.clientConnectionListener = clientConnectionListener;
        }

        @Override
        public void run() {
            // super.run();

            isRunning = true;
            while (isRunning) {
                try {
                    // byte[] bytesReceived = receive();
                    // raiseOnBytesReceived(bytesReceived);
                    int message = receiveIntMessage();
                    receiverSum += message;
                    numOfReceivedMessages++;
                    raiseOnMessageReceived(message);

                } catch (IOException e) {
                    // the connection is closed or interrupted.
                    isRunning = false;
                } catch (Exception e) {
                    // ignore error.
                }
            }
            // lock.notifyAll();
        }

        private byte[] receive() throws Exception {
            byte[] bytesReceived = new byte[] {};
            do {
                bytesReceived = Common.append(bytesReceived, (byte) this.dataInputStream.read());

            } while (this.dataInputStream.available() > 0);

            return bytesReceived;
        }

        private int receiveIntMessage() throws Exception {
            int message = this.dataInputStream.readInt();

            return message;
        }

        private void raiseOnBytesReceived(byte[] bytesReceived) {
            if (this.clientConnectionListener != null) {
                this.clientConnectionListener.onBytesReceived(this, bytesReceived);
            }
        }

        private void raiseOnMessageReceived(int message) {
            if (this.clientConnectionListener != null) {
                this.clientConnectionListener.onMessageReceived(this, message);
            }
        }

        @SuppressWarnings("unused")
        public void sendMessage(String message) throws Exception {
            this.dataOutputStream.writeBytes(message);
            this.dataOutputStream.flush();
        }

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

        public void sendMessage(byte[] message) throws Exception {
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
                this.isRunning = false;
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
