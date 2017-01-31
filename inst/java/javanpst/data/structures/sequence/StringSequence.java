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

package javanpst.data.structures.sequence;

import java.util.ArrayList;

import javanpst.data.readers.sequenceReaders.StringSequenceCSVReader;
import javanpst.data.readers.sequenceReaders.StringSequenceTXTReader;
import javanpst.data.readers.sequenceReaders.StringSequenceXMLReader;
import javanpst.data.writers.sequenceWriters.SequenceCSVWriter;
import javanpst.data.writers.sequenceWriters.SequenceTXTWriter;
import javanpst.data.writers.sequenceWriters.SequenceXMLWriter;

/**
 * A class representing string sequences
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class StringSequence extends Sequence{

	/**
	 * Body of the sequence
	 */
	private ArrayList<String> body;
	
	/**
	 * Default builder
	 */
	public StringSequence (){
		
		body=new ArrayList<String>();
		
	}//end-method
	
	/**
	 * Builder. 
	 * 
	 * Creates a new string sequence from an ArrayList of elements.
	 * 
	 * @param contents array of elements
	 */
	public StringSequence (ArrayList<String> contents){
		
		body= new ArrayList<String>(contents);
		
	}//end-method
	
	/**
	 * Builder. 
	 * 
	 * Creates a new numeric sequence from an array of elements.
	 * 
	 * @param contents array of elements
	 */
	public StringSequence (String [] contents){
		
		body = new ArrayList<String>();
		
		for(int i=0; i < contents.length;i++){
			body.add(contents[i]);
		}

	}//end-method
	
	/**
	 * Copy constructor
	 * 
	 * @param copy sequence to copy
	 */
	public StringSequence(StringSequence copy) {
		
	    this(copy.getSequence());
	    
	}//end-method
	
	/**
	 * Returns the contents of the sequence as an array
	 * 
	 * @return contents of the sequence
	 */
	public ArrayList<String> getSequence(){
		
		return body;
		
	}//end-method
	
	/**
	 *  Adds an element to the end of the sequence
	 */
	public void append(Object text){	
		
		body.add(text.toString());
		
	}//end-method
	
	/**
	 *  Adds an element to the start of the sequence
	 */
	public void prepend(Object text){	
		
		body.add(0,text.toString());
		
	}//end-method
	
	/**
	 * Get an element of the sequence
	 * 
	 * @param index index of the element
	 * @return the element. "" if index out of range
	 */
	public String get(int index){
		
		if((index>-1)&&(index<body.size())){
			return body.get(index);
		}
		
		return "";
		
	}//end-method
	
	/**
	 * Get the size of the sequence
	 * 
	 * @return size
	 */
	public int size(){
		
		return body.size();
		
	}//end-method
	
	/**
	 * Clear the sequence
	 */
	public void clear(){
		
		body.clear();
		
	}//end-method
	
	/**
	 * To String method
	 */
	public String toString() {
	
		String text="";
		
		for (String s : body){
			text+=s+"*";          
        }
		
		text=text.substring(0,text.length()-1);
	
		return text;
		
	}//end-method
	
	/**
	 * Checks is the sequence is binary
	 * 
	 * @return True if the sequence is binary. 
	 */
	public boolean isBinary(){
		
		int i;
		String s;
		
		String firstElement, secondElement;
		
		if(body.size()<2){
			return true;
		}

		firstElement=body.get(0);
		s=body.get(1);
		
		i=2;
		while(firstElement.equals(s)&& i<body.size()){
			s=body.get(i);
			i++;
		}
		
		if(i==body.size()){
			return true;
		}
		
		i--;
		secondElement=body.get(i);
		i++;
		
		while(i<body.size()){
			if(!body.get(i).equals(firstElement)){
				if(!body.get(i).equals(secondElement)){
					return false;
				}
			}
			i++;
		}
		
		return true;
		
	}//end-method
	
	/**
	 * Loads a string sequence from a XML file
	 * 
	 * @param file path of the file
	 */
	public void readXML(String file){
		
		StringSequenceXMLReader.readXMLSequence(file);
		body=StringSequenceXMLReader.getSequence();
		
	}//end-method
	
	/**
	 * Writes a string sequence to a XML file
	 * 
	 * @param file path of the file
	 */
	public void writeXML(String file){
		
		SequenceXMLWriter.writeStringSequence(body,file);
		
	}//end-method
	
	/**
	 * Loads a string sequence from a CSV file
	 * 
	 * @param file path of the file
	 */
	public void readCSV(String file){
		
		StringSequenceCSVReader.readCSVSequence(file);
		body=StringSequenceCSVReader.getSequence();
		
	}//end-method
	
	/**
	 * Writes a string sequence to a CSV file
	 * 
	 * @param file path of the file
	 * @param useComma true true if "," is used as separator, false if ";" is used instead
	 */
	public void writeCSV(String file, boolean useComma){
		
		SequenceCSVWriter.writeStringSequence(body,file,useComma);
		
	}//end-method
	
	/**
	 * Loads a string sequence from a TXT file
	 * 
	 * @param file path of the file
	 */
	public void readTXT(String file){
		
		StringSequenceTXTReader.readTXTSequence(file);
		body=StringSequenceTXTReader.getSequence();
		
	}//end-method
	
	/**
	 * Writes a string sequence to a TXT file
	 * 
	 * @param file path of the file
	 */
	public void writeTXT(String file){
		
		SequenceTXTWriter.writeStringSequence(body,file);
		
	}//end-method

}//end-class
