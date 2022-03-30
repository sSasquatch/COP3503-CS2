/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 
 */
public class Hw01 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // obtain the filename
        String fileName = args[0];
        
        // call the file
        FileReader fileReader = null;
        
        // default constructor
        BST bst = new BST();
        
        // scanning file
        BufferedReader br = null;
        
        // print out the entire file
        try {
            fileReader = new FileReader(fileName);
            br = new BufferedReader(fileReader);
            
            String lineCurrent = null;
            System.out.println(fileName + " contains:");
            while ((lineCurrent = br.readLine()) != null) {
                System.out.println(lineCurrent);
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
                    //val = Character.getNumericValue(br.read());
                    int number = 0;
                    int multiplyer = 1;
                    for (int i = 0; i < s.length(); i++) {
                        number = number + (Character.getNumericValue(s.charAt(s.length() - i - 1)) * multiplyer);
                        multiplyer *= 10;
                    }
                    
                    bst.insert(number);
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
                    
                    bst.delete(number);
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
                    
                    bst.search(number);
                }
                // if the character is a 'p', print the tree inorder
                else if (c == 'p') {
                    bst.printBST(bst.root);
                }
                // if the character is a 'q', stop the loop for reading the file
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
        
        System.out.println("left children:          " + countChildren(bst.root.getChildL()));
        System.out.println("left depth:             " + getDepth(bst.root.getChildL()));
        System.out.println("right children:         " + countChildren(bst.root.getChildR()));
        System.out.println("right depth:            " + getDepth(bst.root.getChildR()));
        
        complexityIndicator();
        
        return;
    }
    
    // name of the function says it all
    public static void complexityIndicator() {
        System.err.println("an089926;3;16");
    }
    
    // counts the total number of children in the tree
    public static int countChildren(BST.Node root) {
        if (root == null) {
            return 0;
        }
            
        return 1 + countChildren(root.getChildL()) + countChildren(root.getChildR());
    }
    
    // gets the depth of the tree
    public static int getDepth(BST.Node root) {
        if (root == null) {
            return 0;
        }
        
        int depthL = getDepth(root.getChildL()), depthR = getDepth(root.getChildR());
        
        if (depthL > depthR) {
            return depthL + 1;
        }
        else {
            return depthR + 1;
        }
    }
}

class BST {
    
    public Node root;
    
    public BST() {
        
    }
    
    // inserts the specific integer into the BST
    // Inserts are based on the algorithm of < goes left and >= goes right.
    public void insert(int data) {
        
        // check if there is a root
        // if not, initialize one
        if (root == null) {
            root = new Node(data);
            return;
        }
        
        Node nodeTemp = new Node(data);
        Node nodeCurrent = root;
        
        // checks for NPE
        if (nodeCurrent != null) {
            
            // make the way through the BST
            // by checking which side to traverse to
            while (nodeCurrent.getChildL() != null || nodeCurrent.getChildR() != null) {
                if  (data < nodeCurrent.getData()) {
                    if (nodeCurrent.getChildL() != null) {
                        nodeCurrent = nodeCurrent.getChildL();
                    }
                    else {
                        break;
                    }
                }
                else if (data >= nodeCurrent.getData()) {
                    if (nodeCurrent.getChildR() != null) {
                        nodeCurrent = nodeCurrent.getChildR();
                    }
                    else {
                        break;
                    }
                }
            }
            
            // set the respective left/right and parent references
            if (data < nodeCurrent.getData()) {
                nodeCurrent.setChildL(nodeTemp);
            }
            else if (data >= nodeCurrent.getData()) {
                nodeCurrent.setChildR(nodeTemp);
            }
            nodeTemp.setParent(nodeCurrent);
        }
    }
    
    // deletes the specific integer from the BST
    public void delete(int data) {
        
        Node nodeCurrent = root;
        Node nodeParent = nodeCurrent;
        Node nodeTemp = nodeCurrent;
        
        // checks for NPE
        if (nodeCurrent != null) {
            
            // make the way through the BST
            // by checking which side to traverse to
            while (nodeCurrent.getChildL() != null || nodeCurrent.getChildR() != null) {
                if  (data < nodeCurrent.getData()) {
                    if (nodeCurrent.getChildL() != null) {
                        nodeParent = nodeCurrent;
                        nodeCurrent = nodeCurrent.getChildL();
                    }
                    else {
                        break;
                    }
                }
                else if (data > nodeCurrent.getData()) {
                    if (nodeCurrent.getChildR() != null) {
                        nodeParent = nodeCurrent;
                        nodeCurrent = nodeCurrent.getChildR();
                    }
                    else {
                        break;
                    }
                }
                else if (data == nodeCurrent.getData()) {
                    // if the node has no children
                    // just get rid of it
                    if (nodeCurrent.getChildL() == null && nodeCurrent.getChildR() == null) {
                        if (nodeParent.getChildL() != null && nodeParent.getChildL().getData() == data) {
                            nodeParent.setChildL(null);
                        }
                        else if (nodeParent.getChildR() != null && nodeParent.getChildR().getData() == data) {
                            nodeParent.setChildR(null);
                        }
                        nodeCurrent.setParent(null);
                    }
                    // if the node has one left child
                    // the child moves up in the place of the deleted node
                    else if (nodeCurrent.getChildR() == null) {
                        nodeTemp = nodeCurrent.getChildL();
                        if (nodeParent.getChildL() != null && nodeParent.getChildL().getData() == data) {
                            nodeParent.setChildL(nodeTemp);
                        }
                        else if (nodeParent.getChildR() != null && nodeParent.getChildR().getData() == data) {
                            nodeParent.setChildR(nodeTemp);
                        }
                        nodeCurrent.setParent(null);
                    }
                    // if the node has one right child
                    // the child moves up in the place of the deleted node
                    else if (nodeCurrent.getChildL() == null) {
                        nodeTemp = nodeCurrent.getChildR();
                        if (nodeParent.getChildL() != null && nodeParent.getChildL().getData() == data) {
                            nodeParent.setChildL(nodeTemp);
                        }
                        else if (nodeParent.getChildR() != null && nodeParent.getChildR().getData() == data) {
                            nodeParent.setChildR(nodeTemp);
                        }
                        nodeCurrent.setParent(null);
                    }
                    // if the node has two children
                    // you find the min value in the right subtree 
                    // then move it up to be at the location of the deleted node
                    // then also delete its original node
                    else {
                        Node nodeMin = findMinNode(nodeCurrent.getChildR());
                        if (nodeParent.getChildL() != null && nodeParent.getChildL().getData() == data) {
                            nodeParent.setChildL(nodeCurrent);
                        }
                        else if (nodeParent.getChildR() != null && nodeParent.getChildR().getData() == data) {
                            nodeParent.setChildR(nodeCurrent);
                        }
                        nodeCurrent.setData(nodeMin.getData());
                        if (nodeMin.getParent() != null && nodeMin.getParent().getChildL() != null && nodeMin.getParent().getChildL() == nodeMin) {
                            nodeMin.getParent().setChildL(null);
                        }
                        else if (nodeMin.getParent() != null && nodeMin.getParent().getChildR() != null && nodeMin.getParent().getChildR() == nodeMin) {
                            nodeMin.getParent().setChildR(null);
                        }
                        nodeMin.setParent(null);
                    }
                    
                    return;
                }   
            }
        }
    }
    
    // searches the tree for a desired integer
    public void search(int data) {
        
        Node nodeCurrent = root;
        
        // checks for NPE
        if (nodeCurrent != null) {

            // make the way through the BST
            // by checking which side to traverse to
            while (nodeCurrent.getChildL() != null || nodeCurrent.getChildR() != null) {
                if  (data < nodeCurrent.getData()) {
                    if (nodeCurrent.getChildL() != null) {
                        nodeCurrent = nodeCurrent.getChildL();
                    }
                    else {
                        break;
                    }
                }
                else if (data > nodeCurrent.getData()) {
                    if (nodeCurrent.getChildR() != null) {
                        nodeCurrent = nodeCurrent.getChildR();
                    }
                    else {
                        break;
                    }
                }
            }
        }
        
        if (data == nodeCurrent.getData()) {
            System.out.println(data + ": found");   
        }
        else {
            System.out.println(data + ": NOT found");
        }
    }
    
    // main print function that traverses the tree
    public void printBSTinorder(Node root) {
        if (root == null) {
            return;
        }
        
        printBSTinorder(root.getChildL());
        System.out.print(" " + root.getData());
        printBSTinorder(root.getChildR());
    }
    
    // print function that you call used to have the line break at the end
    public void printBST(Node root) {
        if (root == null) {
            return;
        }
        
        printBSTinorder(root);
        System.out.print("\n");
    }
    
    // finds and returns the node with the min value of the tree
    public Node findMinNode(Node root) {
        Node nodeCurrent = root;
        
        while (nodeCurrent.getChildL() != null) {
            nodeCurrent = nodeCurrent.getChildL();
        }
        
        return nodeCurrent;
    }
    
    public class Node {
        int data;
        Node left, right, parent;
        
        // node constructor
        public Node(int dataValue) {
            left = null;
            right = null;
            parent = null;
            data = dataValue;
        }
        
        // name of the methods speaks for them
        public int getData() {
            return data;
        }
        
        public void setData(int dataValue) {
            data = dataValue;
        }
        
        public void setChildL(Node leftValue) {
            left = leftValue;
        }
        
        public void setChildR(Node rightValue) {
            right = rightValue;
        }
        
        public void setParent(Node parentValue) {
            parent = parentValue;
        }
        
        public Node getChildL() {
            return left;
        }
        
        public Node getChildR() {
            return right;
        }
        
        public Node getParent() {
            return parent;
        }
    }
}
