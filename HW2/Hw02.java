/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author Anthony
 */
public class Hw02 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // obtain the filename
        String fileName = args[0];
        
        complexityIndicator();
        System.out.println("For the input file named " + fileName);
        
        // obtain the 'R' or 'r' for seeding with a random number
        // if it is specified after the file name on the command line
        long seed;
        Random rand;
        
        try {
            if (args[0].equals(new String("R"))) {
                seed = System.currentTimeMillis();
                System.out.println("With the RNG seeded,");
            }
            else {
                seed = 42;
                System.out.println("With the RNG unseeded,");
            }
        }
        catch (ArrayIndexOutOfBoundsException aiobe) {
            seed = 42;
            System.out.println("With the RNG unseeded,");
        }
        
        rand = new Random(seed);
        
        // call the file
        FileReader fileReader = null;
        
        // default constructor
        SkipList skipList = new SkipList();
        
        BufferedReader br = null;
      
        // process the file one character at a time to do the desired commands
        try {
            fileReader = new FileReader(fileName);
            br = new BufferedReader(fileReader);

            int val = 0;
            while ((val = br.read()) != -1) {
                char c = (char) val;
                // check if the current character is a space or new line
                // if so, continue on to the next character in the file
                if (c == ' ' || c == '\n') {
                    continue;
                }
                // if the character is an 'i', insert
                if (c == 'i') {
                    val = br.read();
                    val = br.read();
                    
                    // obtain the integer that is going to be inserted
                    String s = new String();
                    while ((char) val >= '0' && (char) val <= '9') {
                        s += Character.toString((char) val);
                        val = br.read();
                    }

                    int number = 0;
                    int multiplyer = 1;
                    for (int i = 0; i < s.length(); i++) {
                        number = number + (Character.getNumericValue(s.charAt(s.length() - i - 1)) * multiplyer);
                        multiplyer *= 10;
                    }
                    
                    skipList.insert(number, rand);
                }
                // if the character is a 'd', delete
                else if (c == 'd') {
                    val = br.read();
                    val = br.read();
                    
                    // obtain the integer that is going to be deleted
                    String s = new String();
                    while ((char) val >= '0' && (char) val <= '9') {
                        s += Character.toString((char) val);
                        val = br.read();
                    }
                    int number = 0;
                    int multiplyer = 1;
                    for (int i = 0; i < s.length(); i++) {
                        number = number + (Character.getNumericValue(s.charAt(s.length() - i - 1)) * multiplyer);
                        multiplyer *= 10;
                    }
                    
                    skipList.delete(number);
                }
                // if the character is a 's', search
                else if (c == 's') {
                    val = br.read();
                    val = br.read();
                    
                    // obtain the integer that is going to be searched for
                    String s = new String();
                    while ((char) val >= '0' && (char) val <= '9') {
                        s += Character.toString((char) val);
                        val = br.read();
                    }
                    int number = 0;
                    int multiplyer = 1;
                    for (int i = 0; i < s.length(); i++) {
                        number = number + (Character.getNumericValue(s.charAt(s.length() - i - 1)) * multiplyer);
                        multiplyer *= 10;
                    }
                    
                    skipList.search(number);
                }
                // if the character is a 'p', print the tree
                else if (c == 'p') {
                    skipList.printAll();
                }
                // if the character is a 'q', stop the program
                else if (c == 'q') {
                    break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (br != null) {
                try {
                    br.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return;
    }
    
    // name of the function says it all
    public static void complexityIndicator() {
        System.err.println("an089926;4;15");
    }
}

class SkipList {
    
    public Node negativeInfinity = new Node(Integer.MIN_VALUE);
    public Node positiveInfinity = new Node(Integer.MAX_VALUE);
    public Node head = negativeInfinity;
    public Node tail = positiveInfinity;
    public int maxHeight = 1;
    
    public SkipList() {
        head.setRight(tail);
        tail.setLeft(head);
    }
    
    // inserts the specific integer into the skip list
    public void insert(int data, Random rand) {
    
        Node nodeCurrent = head;
        Node nodeLast = nodeCurrent;
        Node nodeTemp = new Node(data);
        
        if (data == nodeCurrent.getData()) {
            return;
        }
        
        while (data > nodeCurrent.getData()) {
            nodeLast = nodeCurrent;
            nodeCurrent = nodeCurrent.getRight();
            
            if (data == nodeCurrent.getData()) {
                return;
            }
        }
        
        nodeTemp.setRight(nodeCurrent);
        nodeTemp.setLeft(nodeLast);
        nodeCurrent.setLeft(nodeTemp);
        nodeLast.setRight(nodeTemp);
        
        promote(nodeTemp, rand);
        return;
    }
    
    // The promotion method used to generate the test cases 
    // used a 1 to represent heads and correspondingly a 0 to represent tails.
    public void promote(Node node, Random rand) {
        
        int coin = rand.nextInt() % 2;
        
        if (coin == 1) {
            
            Node nodeHeadLevel = head;
            Node nodeNewHeadLevel = new Node(head.data);
            Node nodeTailLevel = tail;
            Node nodeNewTailLevel = new Node(tail.data);
            Node nodeCurrentLevel = nodeHeadLevel;
            Node nodeLastLevel = nodeCurrentLevel;
            Node nodeCurrent = node;
            Node nodeTemp = new Node(node.data);
            
            while (coin == 1) {
                if (nodeHeadLevel.getTop() == null) {
                    nodeHeadLevel.setTop(nodeNewHeadLevel);
                    nodeNewHeadLevel.setBottom(nodeHeadLevel);
                    nodeNewHeadLevel.level = nodeHeadLevel.level + 1;
                    head.height++;
                    
                    nodeTailLevel.setTop(nodeNewTailLevel);
                    nodeNewTailLevel.setBottom(nodeTailLevel);
                    nodeNewTailLevel.level = nodeTailLevel.level + 1;
                    tail.height++;
                    
                    nodeNewHeadLevel.setRight(nodeNewTailLevel);
                    nodeNewTailLevel.setLeft(nodeNewHeadLevel);
                    
                    nodeHeadLevel = nodeHeadLevel.getTop();
                    nodeCurrentLevel = nodeHeadLevel;
                    nodeLastLevel = nodeCurrentLevel;
                    nodeTailLevel = nodeTailLevel.getTop();
                    
                    if (nodeCurrent.getTop() == null) {
                        nodeCurrent.setTop(nodeTemp);
                        nodeTemp.setBottom(nodeCurrent);
                    }
                    else {
                        nodeCurrent = nodeCurrent.getTop();
                    }
                }
                else {
                    nodeHeadLevel = nodeHeadLevel.getTop();
                    nodeCurrentLevel = nodeHeadLevel;
                }
                
                while (node.data > nodeCurrentLevel.data) {
                    nodeLastLevel = nodeCurrentLevel;
                    nodeCurrentLevel = nodeCurrentLevel.getRight();
                }
                
                nodeTemp.setRight(nodeCurrentLevel);
                nodeCurrentLevel.setLeft(nodeTemp);
                nodeTemp.setLeft(nodeLastLevel);
                nodeLastLevel.setRight(nodeTemp);
                
                node.height++;
                coin = rand.nextInt() % 2;
                nodeTemp = new Node(node.data);
                nodeNewHeadLevel = new Node(head.data);
                nodeNewTailLevel = new Node(tail.data);
            }
        }
        else {
            return;
        }
        
        return;
    }
    
    // deletes the specific integer from the skip list
    public void delete(int data) {
        
        Node nodeCurrent = head;
        Node nodeTempL = nodeCurrent;
        Node nodeTempR = nodeCurrent;
        
        // checks for NPE
        if (nodeCurrent != null) {
            
            // navigate to the top height of the list
            for (int i = 0; i < head.height; i++) {
                if (nodeCurrent.getTop() != null) {
                    nodeCurrent = nodeCurrent.getTop();
                }
                else {
                    break;
                }
            }
            
            // node navigate through each level as necessary
            // until finding the desired integer or finding it is not there
            while (nodeCurrent != null) {
                // if you have found the desired integer in the list
                if (data == nodeCurrent.getData()) {

                    while (nodeCurrent != null) {
                        nodeTempL = nodeCurrent.getLeft();
                        nodeTempR = nodeCurrent.getRight();
                        
                        nodeTempL.setRight(nodeTempR);
                        nodeTempR.setLeft(nodeTempL);
                        
                        nodeCurrent.height--;
                        nodeCurrent = nodeCurrent.getBottom();
                    }
                    
                    System.out.println(data + " deleted");
                    return;
                }
                // if the data in the current node is less than the desired data
                else if (data > nodeCurrent.getData()) {
                    if (nodeCurrent.getRight() != null) {
                        nodeCurrent = nodeCurrent.getRight();
                    }
                    else {
                        System.out.println(data + " integer not found - delete not successful");
                        return;
                    }
                }
                // if the data in the current node is greater than the desired data
                else if (data < nodeCurrent.getData()) {
                    if (nodeCurrent.getLeft() != null) {
                        nodeCurrent = nodeCurrent.getLeft();
                        if (nodeCurrent.getBottom() != null) {
                            nodeCurrent = nodeCurrent.getBottom();
                        }
                        else {
                            System.out.println(data + " integer not found - delete not successful");
                            return;
                        }
                    }
                    else {
                        System.out.println(data + " integer not found - delete not successful");
                        return;
                    }
                }
            }
        }
    }

    // searches the skip list for the specific integer
    public void search(int data) {
        
        Node nodeCurrent = head;
        
        // checks for NPE
        if (nodeCurrent != null) {
            
            // navigate to the top height of the list
            for (int i = 0; i < head.height; i++) {
                if (nodeCurrent.getTop() != null) {
                    nodeCurrent = nodeCurrent.getTop();
                }
                else {
                    break;
                }
            }
            
            // node navigate through each level as necessary
            // until finding the desired integer or finding it is not there
            while (nodeCurrent != null) {
                // if you have found the desired integer in the list
                if (data == nodeCurrent.getData()) {
                    System.out.println(data + " found");
                    return;
                }
                // if the data in the current node is less than the desired data
                else if (data > nodeCurrent.getData()) {
                    if (nodeCurrent.getRight() != null) {
                        nodeCurrent = nodeCurrent.getRight();
                    }
                    else {
                        System.out.println(data + " NOT FOUND");
                        return;
                    }
                }
                // if the data in the current node is greater than the desired data
                else if (data < nodeCurrent.getData()) {
                    if (nodeCurrent.getLeft() != null) {
                        nodeCurrent = nodeCurrent.getLeft();
                        if (nodeCurrent.getBottom() != null) {
                            nodeCurrent = nodeCurrent.getBottom();
                        }
                        else {
                            System.out.println(data + " NOT FOUND");
                            return;
                        }
                    }
                    else {
                        System.out.println(data + " NOT FOUND");
                        return;
                    }
                }
            }
        }
    }
    
    // prints the current skip list
    public void printAll() {
        
        Node nodeCurrent = head;
        
        // checks for NPE
        if (nodeCurrent != null) {
            System.out.println("the current Skip List is shown below: ");
            
            while (nodeCurrent != null) {
                if (nodeCurrent.getData() == Integer.MIN_VALUE) {
                    System.out.println("---infinity");
                    nodeCurrent = nodeCurrent.getRight();
                }
                else if (nodeCurrent.getData() == Integer.MAX_VALUE) {
                    System.out.println("+++infinity");
                    System.out.println("---End of Skip List---");
                    return;
                }
                else {
                    for (int i = 0; i < nodeCurrent.height ; i++) {
                        if (i == nodeCurrent.height - 1) {
                            System.out.print(" " + nodeCurrent.getData() + "; \n");
                        }
                        else {
                            System.out.print(" " + nodeCurrent.getData() + "; ");
                        }
                    }
                    
                    nodeCurrent = nodeCurrent.getRight();
                }    
            }
        }
    }
    
    public class Node {
        int data, level, height;
        Node left, right, bottom, top;
        
        // node constructor
        public Node(int dataValue) {
            left = null;
            right = null;
            bottom = null;
            top = null;
            data = dataValue;
            level = 1;
            height = 1;
        }
        
        // name of the methods speaks for them
        public int getData() {
            return data;
        }
        
        public void setData(int dataValue) {
            data = dataValue;
        }
        
        public int getHeight() {
            return height;
        }
        
        public void setHeight(int dataValue) {
            height = dataValue;
        }
        
        public int getLevel() {
            return level;
        }
        
        public void setLevel(int dataValue) {
            level = dataValue;
        }
        
        public void setLeft(Node leftValue) {
            left = leftValue;
        }
        
        public void setRight(Node rightValue) {
            right = rightValue;
        }
        
        public void setTop(Node topValue) {
            top = topValue;
        }
        
        public void setBottom(Node bottomValue) {
            bottom = bottomValue;
        }
        
        public Node getLeft() {
            return left;
        }
        
        public Node getRight() {
            return right;
        }
        
        public Node getTop() {
            return top;
        }
        
        public Node getBottom() {
            return bottom;
        }
    }
}