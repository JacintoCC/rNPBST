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
 * A CSV reader for numerical sequences.
 * 
 * Store data inside a string array.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class NumericSequenceTXTReader{

	/**
	 * Inner array
	 */
	private static ArrayList<Double> sequence;

	/**
	 * Get a copy of the inner sequence as output
	 * 
	 * @return a copy of the inner sequence
	 */
	public static ArrayList<Double> getSequence(){
		
		ArrayList<Double> out= new ArrayList<Double>(sequence);
		
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
	 * The element must be a numerical value
	 * 
	 * @param text element to add
	 */
	public static void append(String text){
	
		double value;
		
		try{
			
			value=Double.parseDouble(text);					
			sequence.add(value);
			
		}catch(Exception e){
			
			System.out.println("Parsing error with value \""+text+"\"");
		}
		
	}//end-method
	
	/**
	 * Read a numeric sequence from a TXT file
	 * 
	 * @param file path of the file
	 */
	public static void readTXTSequence(String file){
	
		String content,separator;
		StringTokenizer tokens;
		
		content=Files.readFile(file);
		
		content=content.replaceAll("\n", "");
		content=content.replaceAll("\t", "");
		content=content.replaceAll("\r", "");
		content=content.replaceAll(" ", "");
		
		separator = ";";
		
		tokens = new StringTokenizer (content, separator);
		
		sequence=new ArrayList<Double>();
		
		while(tokens.hasMoreElements()){
			append(tokens.nextToken());
		}

	}//end-method
	
}//end-class

