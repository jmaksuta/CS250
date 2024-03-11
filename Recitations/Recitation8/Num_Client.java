import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Num_Client {
    static DataInputStream din;
    static DataOutputStream dout;
    static Scanner userInput = new Scanner(System.in);
    static Socket clientSocket;

    public static int receiveNum() {
        try {
            int response = din.readInt(); // Reads an int from the input stream
            return response;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return -1; // if an incorrect value is read, the EXIT_NUM will be returned
    }

    public static void sendNumber(int numToSend) {
        try {
            dout.writeInt(numToSend); // Writes an int to the output stream
            dout.flush(); // By flushing the stream, it means to clear the stream of any element that may
                          // be or maybe not inside the stream
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    // Below method cleans up all of the connections by closing them and then
    // exiting.
    // This prevents a lot of problems, so its good practice to always make sure the
    // connections close.

    public static void cleanUp() {
        try {
            clientSocket.close();
            dout.close();
            din.close();

            System.out.println("Connections Closed");
            System.exit(0);

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        final int EXIT_NUM = -1;
        Scanner userInput = new Scanner(System.in);

        final int port = Integer.parseInt(args[0]);
        final String server_ip = args[1];

        try {

            // Initialize Necessary Objects
            clientSocket = new Socket(server_ip, port); // Establishes a connection to the server
            dout = new DataOutputStream(clientSocket.getOutputStream()); // Instantiates out so we can then use it to
                                                                         // send data to the client
            din = new DataInputStream(clientSocket.getInputStream()); // Instantiates in so we can then use it to
                                                                      // receive data from the client

            // FIX ME: Create the while loop that sends and receives data

            while (true) {
                int response = receiveNum();
                System.out.println("Server says: " + response);

                System.out.print("Client Message: ");
                int message = Integer.parseInt(userInput.nextLine());
                
                if(message == EXIT_NUM){
                    System.out.println("Ending Communitcation");
                    cleanUp();
                }

                sendNumber(message);
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
