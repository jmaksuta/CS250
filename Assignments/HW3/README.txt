Author:     John Maksuta
Course:     CS250-801 Spring 2024
Instructor: Professor Pallickara
Date:       2024-03-27
Assignment: Homework 3

Description:
This is my submission for Homework 3.
There are two applications in this project.

TCPServer and TCPClient

TCPServer
Usage:
java cs250.hw3.TCPServer <port-number> <seed> <number-of-messages>

Description:
This application starts a tcp server at the port number specified by the command line argument <port-number>.
It waits for 2 clients to establish connection.
Once the connections are established, it sends configuration data to both clients. The configuration data contains
two integers in one message as a space delimited string. This contains the number of messages as integer and a
generated seed value as integer i.e. "15 123456".

TCPClient
Usage:
java cs250.hw3.TCPClient <server-host> <server-port>

Description:
This application starts a tcp client which establishes a TCP connection to the ip address/host name and
port number specified in the command line arguments <server-host> and <server-port> respectively.

Assignment details.
There are 5 tasks associated with this assignment.

Submission 1 contains Tasks 1 and 2 completed.

Submission 2 contains previous tasks and Task 3 completed.

Submission 3 contains previous tasks and Tasks 4 and 5.