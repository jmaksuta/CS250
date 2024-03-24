-----------------------------------------------------------------------------------------------
Author:     John Maksuta
Course:     CS250-801 Spring 2024
Instructor: Professor Pallickara
Date:       2024-03-27
Assignment: Homework 3
-----------------------------------------------------------------------------------------------
Description:
This is my submission for Homework 3.
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
There are 6 class files in total in this assignment.

Class Name: TCPServer
Description:
Methods:

Class Name: TCPServer.ClientConnection
Description:
Methods:

Class Name: TCPClient
Description:
Methods:

Class Name: Main
Description:
Methods:

Interface Name: IMain
Description:
Methods:

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

    public static byte[] append(byte[] destination, byte[] source)
    returns a byte array consisting of the destination byte array concatenated with the source byte
    array.
    Parameters:
    destination - The byte array to append to.
    source - The byte array to append onto destination.

Class Name: Configuration
Description:
Methods:

Class Name: 
Description:
Methods:

-----------------------------------------------------------------------------------------------