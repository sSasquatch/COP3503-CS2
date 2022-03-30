/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author 
 */
public class Hw03 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // obtain the filename
        String fileName = args[0];
        
        // create the variables for processing the file
        FileReader fileReader = null;
        BufferedReader br = null;
        
        // process the file one character at a time
        try {
            // call the file
            fileReader = new FileReader(fileName);
            br = new BufferedReader(fileReader);

            // create variable for the string that are
            // the lines of the file
            String str = new String();
            str = br.readLine();

            int hashVal = 0;
            
            // traverse the input file
            // and create the hashes for each line
            while (str != null) {
                hashVal = UCFxram(str, str.length());
                
                System.out.format("%10x:%s\n", hashVal, str);
                
                str = br.readLine();
            }
            
            System.out.println("Input file processed");
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
        System.err.println("an089926;1.5;4");
    }
    
    // hashing algorithm suitable for use in a bloom filter
    public static int UCFxram(String input, int len) {

        int randVal1 = 0xbcde98ef;          // aribtrary value
        int randVal2 = 0x7890face;
        int hashVal = 0xfa01bc96;           // start seed value
        int roundedEnd = len & 0xfffffffc;  // Get 4 byte blocks
        
        int tempData = 0;
        
        for (int i = 0; i < roundedEnd; i = i + 4) {
            tempData = (input.charAt(i) & 0xff) | ((input.charAt(i + 1) & 0xff) << 8) | ((input.charAt(i + 2) & 0xff) << 16) | (input.charAt(i + 3) << 24);
            tempData = tempData * randVal1;                 // Mulitiply
            tempData = Integer.rotateLeft(tempData, 12);    // Rotate left 12 bits
            tempData = tempData * randVal2;                 // Another Multiply
            hashVal = hashVal ^ tempData;
            hashVal = Integer.rotateLeft(hashVal, 13);      // Rotate left 13 bits
            hashVal = hashVal * 5 + 0x46b6456e;
        }

        // Now collect orphan input characters
        tempData = 0;
            
        if ((len & 0x03) == 3) {
            tempData = (input.charAt(roundedEnd + 2) & 0xff) << 16;
            len = len - 1;
        }
        if ((len & 0x03) == 2) {
            tempData |= (input.charAt(roundedEnd + 1) & 0xff) << 8;
            len = len - 1;
        }
        if ((len & 0x03) == 1) {
            tempData |= (input.charAt(roundedEnd) & 0xff);
            tempData = tempData * randVal1;                // Mulitiply
            tempData = Integer.rotateLeft(tempData, 14);   // Rotate left 14 bits
            tempData = tempData * randVal2;                // Another Multiply
            hashVal = hashVal ^ tempData;
        }
        
        hashVal = hashVal ^ len;            // XOR
        hashVal = hashVal & 0xb6acbe58;     // AND
        hashVal = hashVal ^ hashVal >>> 13;
        hashVal = hashVal * 0x53ea2b2c;     // Another arbitrary value
        hashVal = hashVal ^ hashVal >>> 16;
        
        return hashVal;     // Return the 32 bit int hash
    }
}
