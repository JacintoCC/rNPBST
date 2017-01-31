/***********************************************************************

	This file is part of JavaNPST, a Java library of NonParametric
	Statistical Tests.

	Copyright (C) 2011
	
	J. Derrac (jderrac@decsai.ugr.es)
	S. García (sglopez@ujaen.es)
	F. Herrera (herrera@decsai.ugr.es)

	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program.  If not, see http://www.gnu.org/licenses/
  
**********************************************************************/

package javanpst.utils;

import java.io.*;

/**
 * A simple class for managing files.
 * 
 * Basic functionality for reading, writing and adding  
 * contents to text files is provided.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class Files{

    /**
     * 
     * Reads a file and returns the contents on a single string.
     * 
     * @param fileName name of the file 
     * @return A string with the contents of the file
     */
    public static String readFile(String fileName) {
    	
        String content = "";
        
        try {
                FileInputStream fis = new FileInputStream(fileName);
                byte[] piece = new byte[4096];
                int readBytes = 0;
                while (readBytes != -1) {
				readBytes = fis.read(piece);
				if (readBytes != -1) {
					content += new String(piece, 0, readBytes);
				}
		}
                
		fis.close();
		
	    }
	catch (IOException e) {
	        e.printStackTrace();
	        System.exit(-1);
	    }

        return content;
        
    }//end-method


    /**
     *
     * Writes a String in a new file.
     * Note that this method will overwrite contents of the file
     * if it was created previously.
     * 
     * @param fileName name of the file 
     * @param content text to be written
     */
    public static void writeFile (String fileName, String content) {
    	
        try {
                FileOutputStream f = new FileOutputStream(fileName);
                DataOutputStream fis = new DataOutputStream((OutputStream) f);
                fis.writeBytes(content);
                fis.close();
	    }
        catch (IOException e) {
	        e.printStackTrace();
	        System.exit(-1);
	    }
        
    }//end-method

    /**
     * 
     * Adds data to a file.
     * New contents will be added to the end of the file,
     * without overwriting previous contents
     * 
     * @param fileName name of the file
     * @param content content to be added
     */
    public static void addToFile (String fileName, String content) {
        try {
                RandomAccessFile fis = new RandomAccessFile(fileName, "rw");
                fis.seek(fis.length());
                fis.writeBytes(content);
                fis.close();
            }
        catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        
    }//end-method

    /**
     * In CSV-based contents, checks whether "," or ";" was used as separator
     * 
     * @param text text to scan
     * @return a string containing the separator used
     */
    public static String findSeparator(String text){
    	
    	int comma = 0;
    	int dotComma = 0;
    	
    	for(int i=0;i<text.length();i++){
    		if(text.charAt(i)==','){
    			comma++;
    		}
    		if(text.charAt(i)==';'){
    			dotComma++;
    		}
    	}
    	
    	if(comma>dotComma){
    		return ",";
    	}
    	else{
    		return ";";
    	}
    	
    }//end-method
    
}//end-class

