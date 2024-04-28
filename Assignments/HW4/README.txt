-----------------------------------------------------------------------------------------------
Author:     John Maksuta
Course:     CS250-801 Spring 2024
Instructor: Professor Pallickara
Date:       2024-05-03
Assignment: Homework 4
-----------------------------------------------------------------------------------------------
Description:
This is my submission for Homework 4.
Tasks 1 and 2 are completed as described.

Both classes BinaryTree and BTree include the provided main driver code.
There are two applications in this project.

BinaryTree and BTree
-----------------------------------------------------------------------------------------------
BinaryTree
Usage:
java cs250.hw4.BinaryTree <filename>

Description:
this application runs the driver code for the BinaryTree class that implements the TreeStructure
interface.
-----------------------------------------------------------------------------------------------
BTree
Usage:
java cs250.hw4.BTree <filename>

Description:
this application runs the driver code for the BTree class that implements the TreeStructure
interface.
-----------------------------------------------------------------------------------------------
Assignment details:
There are 2 Tasks

Task 1: Build a Binary Search Tree (5 pts)
You should create a class called: BinaryTree that implements the TreeStructure interface that is
provided below. Each of the methods part of the TreeStructure interface should be implemented using
the same logic discussed in the lecture slides.
Command line execution: java cs250.hw4.BinaryTree <filename>
Points Breakdown
1 point for correctly implementing the “insert” operation.
1 point for correctly implementing the “remove” operation.
1 point for correctly implementing the “get” operation.
1 point for correctly implementing the “findMaxDepth” operation.
1 point for correctly implementing the “findMinDepth” operation.

Task 2: Build a B Tree (2.5 pts)
You should create a class called: BTree that implements the TreeStructure interface that is provided
below. Each of the methods part of the TreeStructure interface should be implemented using the same
logic discussed in the lecture slides.
Command line execution: java cs250.hw4.BTree <filename>
Points Breakdown
0.5 points for correctly implementing the “insert” operation.
0.5 points for correctly implementing the “remove” operation.
0.5 points for correctly implementing the “get” operation.
0.5 points for correctly implementing the “findMaxDepth” operation.
0.5 points for correctly implementing the “findMinDepth” operation.
-----------------------------------------------------------------------------------------------
SUBMISSIONS:
There is only 1 submission for this assignment.
-----------------------------------------------------------------------------------------------
CLASSES
There are 3 class files in total in this assignment, 2 classes, and 1 interface.
-------------------------------------------
File Name:  BinaryTree.java
Class Name: BinaryTree
Description: This class contains a Binary Search Tree data structure..
Methods:
    public BinaryTree()
    The default constructor of the BinaryTree class.

    public insert(Integer num)
    Inserts a node into the binary search tree structure.
    Parameters:
    num          - The node's Key value.

    public Boolean remove(Integer num)
    Searches for and removes a node with the value of parameter num.
    Parameters:
    num          - The node's Key value.

    public static void main(String[] args)
    The main entry point of the application. It reads a text file specified in the Parameters
    was supplied for this assignment.
    Parameters:
    args    - the command line arguments supplied to the application.
    
    public Long get(Integer num)
    Searches for a node in the tree with the value num, and returns the node's timestamp.
    Parameters:
    num          - The node's Key value.

    public Integer findMaxDepth()
    Traverses the tree and returns that maximum depth reported by all nodes.

    public Integer findMinDepth()
    Traverses the tree and returns that minimum depth reported by all nodes.
    
-------------------------------------------
File Name:  BTree.java
Class Name: BTree
Description: This class contains a Binary Search Tree data structure..
Methods:
    public BTree()
    The default constructor of the BTree class.

    public insert(Integer num)
    Inserts a node into the BTree structure.
    Parameters:
    num          - The node's Key value.

    public Boolean remove(Integer num)
    Searches for and removes a node with the value of parameter num.
    Parameters:
    num          - The node's Key value.

    public static void main(String[] args)
    The main entry point of the application. It reads a text file specified in the Parameters
    was supplied for this assignment.
    Parameters:
    args    - the command line arguments supplied to the application.
    
    public Long get(Integer num)
    Searches for a node in the tree with the value num, and returns the node's timestamp.
    Parameters:
    num          - The node's Key value.

    public Integer findMaxDepth()
    Traverses the tree and returns that maximum depth reported by all nodes.

    public Integer findMinDepth()
    Traverses the tree and returns that minimum depth reported by all nodes.
    
-------------------------------------------
-----------------------------------------------------------------------------------------------
SAMPLE OUTPUT: BinaryTree
-------------------------------------------
denver:~$ java cs250.hw4.BinaryTree data.txt

-------------------------------------------
SAMPLE OUTPUT: BTree
-------------------------------------------
denver:~$ java cs250.hw4.BTree data.txt
-------------------------------------------