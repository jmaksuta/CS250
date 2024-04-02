-----------------------------------------------------------------------------------------------
Author:     John Maksuta
Course:     CS250-801 Spring 2024
Instructor: Professor Pallickara
Date:       2024-04-03
Assignment: Homework 3
-----------------------------------------------------------------------------------------------
Description:
This is my submission number 2 for Homework 3.
Tasks 1, 2, and 3 are completed.
There are two applications in this project.

TCPServer and TCPClient
-----------------------------------------------------------------------------------------------
TCPServer
Usage:
java cs250.hw3.TCPServer <port-number> <seed> <number-of-messages>

Description:
This application starts a tcp server at the port number specified by the command line argument <port-number>.
It waits for 2 clients to establish connection.
Once the connections are established, it sends configuration data to both clients. The configuration data contains
two integers in one message as a space delimited string. This contains the number of messages as integer and a
generated seed value as integer i.e. "15 123456".
-----------------------------------------------------------------------------------------------
TCPClient
Usage:
java cs250.hw3.TCPClient <server-host> <server-port>

Description:
This application starts a tcp client which establishes a TCP connection to the ip address/host name and
port number specified in the command line arguments <server-host> and <server-port> respectively.
-----------------------------------------------------------------------------------------------
Assignment details:
There are 5 tasks associated with this assignment (1.1 to 1.5).

1.1 Task 1: Server start-up:
The server should first check to see if it is able to successfully create a server socket on the specified
port. The value should be > 1024 <=65,535. You may encounter an exception if a server socket
currently is bound to that port number, print that exceptions message using “exception.getMessage()”.
Upon start-up the server should initialize its random number generator using the specified seed.

1.2 Task 2: Registering two clients to the Server:
Upon start-up the clients should first connect to the Server. Provided you have specified the correct
server host/port information and the server is currently running this will result in a successful connection
to the server.
In one message the server side sends two integers to the client. The first integer specifies the number
of messages that the server expects the client to send. This number is the argument that was specified
to the server at its command line: you should not hard-code this number in your client-side code.
The second integer is a random number from the Server that is used to initialize the client-side random
number generator.

1.3 Task 3: Sending messages from the client to the server:
Once the registration process is complete. The client must do two things.
First, it must wait for 10 seconds. This wait is to allow you to start-up another client and connect to the
server.
Second, the client is required to use its client-side random number generator to produce the specified
number of messages. Each message is an integer that was produced using the random number
generator.
The client should also maintain a variable (long senderSum) which is used to maintain a running sum
of all integer numbers that are being sent. Setting this variable as a long allows you to circumvent any
overflow situations that would arise if you used an int instead.
The client should maintain a second variable (int numOfSentMessages) that tracks the number of
messages that were sent by the client.

1.4 Task 4: Processing a message that is relayed from the server to the client:
Any message received by a server from a client (say A) will be relayed (or forwarded) to the other client
(say B) that is connected to the server.
A client should maintain a running count of the messages (each of which is just a number) forwarded
by the server. This information is maintained in another variable (long receiverSum).

1.5 Task 5: Printing metadata information:
This task entails printing metadata information at the server and the two clients.
In particular, the information that should be printed includes the number of messages and the running
counts of the messages that are sent and received by each client.
This information is printed at the server and each of the clients. Printing this metadata information is
what allows you to confirm the correctness of your implementation. The running counts should match
up. For example:
1. Let senderSum at client A be X
2. Let the receiverSum at client B be Y. This receiverSum corresponds to messages from client A
that were relayed by the server.
3. If your implementation is correct then X=Y. That is, what you sent is what was received!
-----------------------------------------------------------------------------------------------
SUBMISSIONS:
Submission 1 contains Tasks 1 and 2 completed.

Submission 2 contains previous tasks and Task 3 completed.

Submission 3 contains previous tasks and Tasks 4 and 5.
-----------------------------------------------------------------------------------------------
CLASSES
There are 6 class files in total in this assignment, and 7 classes.
-------------------------------------------
File Name:  TCPServer.java
Class Name: TCPServer
Description: This class is used to run the TCP Server application that interacts with TCP Client applications
    using TCP sockets.
Methods:
    public TCPServer()
    The default constructor of the TCPServer class.

    public TCPServer(int portNumber, int seed, int numberOfMessages)
    Creates an instance of the TCPServer class with the values supplied as parameters.
    Parameters:
    portNumber          - The port number the TCPServer is running on.
    seed                - The seed value of the TCPServer's random number generator.
    numberOfMessages    - The number of messages to exchange with TCPClients.

    public static void main(String[] args)
    The main entry point of the application. It initializes, starts, runs, and cleans up the TCP Server.
    Parameters:
    args    - the command line arguments supplied to the application.
    
    public void startup() throws Exception
    Initializes the TCP Server.

    public void run() throws Exception
    Waits for and establishes two connections to TCPClient applications on a given port. Once both clients
    are connected it will exchange configuration information with the TCP clients providing each with
    the number of messages it will exchange and a randomly generated number used for the TCP Clients'say
    random number generator's seed value.

    public void cleanup()
    Cleans up resources used by TCPServer for garbage collection.
-------------------------------------------
File Name:  TCPServer.java
Class Name: TCPServer.ClientConnection
Super Class: java.lang.Thread
Description: This class maintains the instance of the TCPClient's connection to the TCPServer.
Fields:
    boolean isRunning
    This is the state of the thread.
Methods:
    public ClientConnection(Socket clientSocket, Configuration configuration) throws Exception
    The public constructor of the ClientConnection class.
    Parameters:
    clientSocket    - An instance of java.net.Socket used for the TCPClient's connection to the TCPServer.
    configuration   - An instance of Configuration used for representing the TCPClient on the TCPServer.
    
    public void run()
    Starts the ClientConnection and runs whilre isRunning is true.
        
    public void sendMessage(String message) throws Exception
    Sends a message to the TCPClient.
    Parameters:
    message - The message, as string, to send to the TCPClient.

    public void sendMessage(int message) throws Exception
    Sends a message to the TCPClient.
    Parameters:
    message - The message, as int, to send to the TCPClient.

    public void sendMessage(Configuration configuration) throws Exception
    Sends a message to the TCPClient containing the Configuration values.
    Parameters:
    configuration - The Configuration whose values are sent to the TCPClient.

    public String receiveMessage() throws Exception
    Returns a string received from the TCPClient.

    public void cleanup()
    Cleans up resources used by TCPServer.ClientConnection for garbage collection.
-------------------------------------------
File Name:  TCPClient.java
Class Name: TCPClient
Description: The TCP Client application that connects to a TCP Server, retrieves a client seed
    and number of messages.
Methods:
    public TCPClient()
    The default constructor of TCPClient class.

    public TCPClient(String hostName, int hostPort)
    A constructor of TCPClient class that sets the hostName and hostPort to the parameter values supplied.

    public static void main(String[] args)
    The main entry point of the application. It initializes, starts, runs, and cleans up the TCP Client.
    Parameters:
    args    - the command line arguments supplied to the application.

    public void startup() throws Exception
    Starts the TCPClient instance.

    public void sendMessage(String message) throws Exception
    Sends a message to the TCPServer.
    Parameters:
    message - the message to send to the TCPServer.

    public void sendMessage(int toSend) throws Exception
    Converts and int to a big endian encoded byte array and sends it to the server.
    After sending it flushs the DataOutPutStream used.

    public byte[] receiveRegistration() throws Exception
    reads the bytes from the server that were sent during registration process.
    It anticipates receiving two integers encoded as big endian with 4 bytes each for a total of
    8 bytes. It will read the bytes until there are no more available in the DataInputStream.

    public void run() throws Exception
    This performs the main actions of the application after registration. First, it waits 10 seconds,
    and then begins its process. It writes the "starting to send messages to the server message to the console.
    It then clears the counter variables, senderSum, and numOfSentMessages. Then enters a loop where it generates
    a random number, adds it to the sender sum, sends it to the server, and then increments the numOfSentMessages
    counter. The loop continues until the number of messages defined by the server has been reached.
    After this loop completes it writes the summary to the console.

    public void cleanup()
    Cleans up resources used by TCPClient for garbage collection.
-------------------------------------------
File Name:  Main.java
Class Name: Main
Description: This class is used to handle many of the common operations in starting applications.
Constants:
    public static final int MIN_PORT_NUMBER = 1025
    The minimum port number allowed for the TCPServer.

    public static final int MAX_PORT_NUMBER = 65535
    The maximum port number allowed for the TCPServer.

    public static final int MIN_UNSIGNED_INTEGER = 0
    The minimum value of unsigned integer.

    public static final int MAX_UNSIGNED_INTEGER = 2147483647
    The maximum value of unsigned integer.
Methods:
    public static void validateCommandLineArgs(String[] args, IMain mainInterface) throws Exception
    Validates the command line args implementing the interface mainInterface.
    Parameters:
    args            - the command line args to validate.
    mainInterface   - the IMain interface used.
-------------------------------------------
File Name:  IMain.java
Interface Name: IMain
Description:    This interface is used by both TCPServer and TCPClient to add behaviors.
Methods:
    void validateCommandLineArgs() throws Exception
    Allows the implementating class to validate validateCommandLineArgs.
-------------------------------------------
File Name:  Common.java
Class Name: Common
Description: This class contains many static methods used by all classes as a global resource.
Methods:
    public static void writeToConsole(String message)
    Writes a string to the console without a new line character.
    Parameters:
    message - the message to write.

    public static void writeLineToConsole(String message)
    Writes a string to the console with a new line character.
    Parameters:
    message - the message to write.

    public static boolean isInteger(String value)
    returns a boolean value if the string parameter value is an integer.
    Parameters:
    value - the string used to check whether its value is an integer.

    public static int toInteger(String value)
    Returns an int value from a string, or 0 if the string parameter is not an integer.
    Parameters:
    value - the string used to convert to an integer, if it is a valid integer.

    public static boolean isValidRange(Integer value, Integer lowerLimit, Integer upperLimit)
    Returns true if the parameter integer value is within the lowerLimit and upperLimit inclusive.
    Parameters:
    value       - the value used to check whether it is within the bounds of lowerLimit and upperLimit.
    lowerLimit  - the lower limit of the range.
    upperLimit  - the upper limit of the range.

    public static boolean isValidHost(String value)
    Returns true if the string parameter value is a valid host name, either an IPv4 address,
    an IPv6 address, or a domain name.
    Parameters:
    value   - The string value used to check whether its value is a valid host.

    public static byte[] listToArray(ArrayList<Byte> list)
    Returns a byte array from the elements in an ArrayList of Byte.
    Parameters:
    list - The array list used to convert to a byte array.

    public static byte[] intToByteArray(int value)
    Converts an int to a big endian encoded byte array.
    Parameters:
    value   - the int value used to converte to a byte array.

    public static byte[] append(byte[] destination, byte source)
    returns a byte array consisting of the destination byte array concatenated with the source byte.
    Parameters:
    destination - The byte array to append to.
    source - The byte to append onto destination.

    public static byte[] append(byte[] destination, byte[] source)
    returns a byte array consisting of the destination byte array concatenated with the source byte
    array.
    Parameters:
    destination - The byte array to append to.
    source - The byte array to append onto destination.

    public static byte[] subbyte(byte[] bytes, int startIndex, int length)
    This method is similar to the substring function for strings, but it works on a byte array instead.
    It returns a byte array from the bytes byte array parameter from the startIndex for a specified length.
    It will return an empty array if the source bytes has a length less than the length parameter.
    Parameters:
    bytes - The source byte array.
    startIndex - the index to start from.
    length - the length of bytes to return.

    public static int toInteger(byte[] bytes)
    This method converts an array of big endian encoded bytes to an integer.
    Parameters:
    bytes - the byte array to convert to an integer.
-------------------------------------------
File Name:  Configuration.java
Class Name: Configuration
Description: This class holds the values used for client configuration
Methods:

    public int getNumberOfMessages()
    Returns the int value of the number of messages used by TCPClient.

    public int getSeed()
    Returns the int value of seed used by TCPClient.

    public Configuration()
    The public default constructor of the Configuration class.

    public Configuration(int numberOfMessages, int seed)
    A public constructor for the Configuration class. Sets the instance fields numberOfMessages and seed.

    public String toString()
    Returns a string in the format <numberOfMessages> <seed>
-------------------------------------------
-----------------------------------------------------------------------------------------------