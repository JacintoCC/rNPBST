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

package javanpst.data.readers.sequenceReaders;

import javanpst.utils.Files;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * A CSV reader for string sequences.
 * 
 * Store data inside a string array.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class StringSequenceCSVReader{

	/**
	 * Inner array
	 */
	private static ArrayList<String> sequence;

	/**
	 * Get a copy of the inner sequence as output
	 * 
	 * @return a copy of the inner sequence
	 */
	public static ArrayList<String> getSequence(){
		
		ArrayList<String> out= new ArrayList<String>(sequence);
		
		return out;
		
	}//end-method
	
	/**
	 * Clears the array
	 */
	public static void clear(){
		
		sequence.clear();
		
	}//end-method
	
	/**
	 * Adds a new element to the sequence.
	 * 
	 * @param text element to add
	 */
	public static void append(String text){
				
		sequence.add(text);		
		
	}//end-method
	
	/**
	 * Read a string sequence from a CSV file
	 * 
	 * @param file path of the file
	 */
	public static void readCSVSequence(String file){
	
		String content,separator;
		StringTokenizer tokens;
		
		content=Files.readFile(file);
		
		content=content.replaceAll("\n", "");
		content=content.replaceAll("\r", "");
		
		separator = Files.findSeparator(content);
		
		tokens = new StringTokenizer (content, separator);
		
		sequence=new ArrayList<String>();
		
		while(tokens.hasMoreElements()){
			append(tokens.nextToken());
		}

	}//end-method
	
}//end-class

