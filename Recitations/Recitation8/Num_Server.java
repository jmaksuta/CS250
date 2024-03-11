import java.io.*;    
import java.net.*;
import java.io.IOException;
import java.util.Scanner;

public class Num_Server {
    static DataInputStream din;
    static DataOutputStream dout;
    static Scanner userInput = new Scanner(System.in);
    static Socket clientSocket;
    static ServerSocket serverSocket;

    public static int receiveNum(){
        try {
            int response = din.readInt();
            return response;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return -1;
    }

    public static void sendNumber(int numToSend){
        try {
            dout.writeInt(numToSend);
            dout.flush(); // By flushing the stream, it means to clear the stream of any element that may be or maybe not inside the stream
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    public static void cleanUp(){
        try {
            serverSocket.close();
            clientSocket.close();
            dout.close();
            din.close();

            System.out.println("Connections Closed");
            System.exit(0);

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }


    public static void main(String[] args){
        final int port =  5050; 
        final int EXIT_NUM = -1;

        try{
            System.out.println("IP Address: " + InetAddress.getLocalHost() + "\nPort Number " + port);  

            // Initialize Necessary Objects
            serverSocket = new ServerSocket(port);
            System.out.println("waiting for client...");
            clientSocket = serverSocket.accept(); // Blocking call --> waits here until a request comes in from a client
            System.out.println("Client Connected!");
            dout = new DataOutputStream(clientSocket.getOutputStream()); // Instantiates dout so we can then use it to send data to the client
            din = new DataInputStream(clientSocket.getInputStream()); // Instantiates din so we can then use it to receive data from the client
            

            while(true){
                System.out.print("Server Message: ");
                int message = Integer.parseInt(userInput.nextLine());
                 
                if(message == EXIT_NUM){
                    System.out.println("Ending Communitcation");
                    cleanUp();
                }

                sendNumber(message);
                int response = receiveNum();
                System.out.println("Client Response: " + response);
            }
        }
        catch(IOException e){
            System.err.println(e.getMessage());
        }
    }



}
