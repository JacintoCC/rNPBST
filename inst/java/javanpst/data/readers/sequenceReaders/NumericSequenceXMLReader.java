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

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import java.io.IOException;

import java.util.ArrayList;

/**
 * A XML reader for numerical sequences.
 * 
 * Store data inside a string array.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class NumericSequenceXMLReader{

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
	 * Read a string sequence from a file, through SAX API
	 * 
	 * @param file path of the file
	 */
	public static void readXMLSequence(String file){
	
		sequence=new ArrayList<Double>();
		
		try{
            SAXParserFactory spf=SAXParserFactory.newInstance(); 
            SAXParser sp = spf.newSAXParser();                   
           	sp.parse(file, new SequenceXMLReaderFormat(SequenceXMLReaderFormat.NUMERIC) );	            
        }catch(ParserConfigurationException e){                  
        	System.err.println("Parser error");             
        }catch(SAXException e2){                                 
        	System.err.println("SAX error: " + e2.getStackTrace());
        } catch (IOException e3) {
        	System.err.println("Input/Output error: " + e3.getMessage() );
        }

	}//end-method
	
}//end-class

