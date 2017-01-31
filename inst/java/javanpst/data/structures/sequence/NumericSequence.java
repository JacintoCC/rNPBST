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
import java.util.Collections;

import javanpst.data.readers.sequenceReaders.NumericSequenceCSVReader;
import javanpst.data.readers.sequenceReaders.NumericSequenceTXTReader;
import javanpst.data.readers.sequenceReaders.NumericSequenceXMLReader;
import javanpst.data.writers.sequenceWriters.SequenceCSVWriter;
import javanpst.data.writers.sequenceWriters.SequenceTXTWriter;
import javanpst.data.writers.sequenceWriters.SequenceXMLWriter;

/**
 * A class representing numerical sequences
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class NumericSequence extends Sequence{

	/**
	 * Body of the sequence
	 */
	private ArrayList<Double> body;

	/**
	 * Default builder
	 */
	public NumericSequence (){
		
		body=new ArrayList<Double>();
		
	}//end-method
	
	/**
	 * Builder. 
	 * 
	 * Creates a new numeric sequence from an ArrayList of elements.
	 * 
	 * @param contents array of elements
	 */
	public NumericSequence (ArrayList<Double> contents){
		
		body= new ArrayList<Double>(contents);
		
	}//end-method
	
	/**
	 * Builder. 
	 * 
	 * Creates a new numeric sequence from an array of elements.
	 * 
	 * @param contents array of elements
	 */
	public NumericSequence (double [] contents){
		
		body = new ArrayList<Double>();
		
		for(int i=0; i < contents.length;i++){
			body.add(contents[i]);
		}

	}//end-method
	
	/**
	 * Copy constructor
	 * 
	 * @param copy sequence to copy
	 */
	public NumericSequence(NumericSequence copy) {
		
	    this(copy.getSequence());
	    
	}//end-method
	
	/**
	 * Returns the contents of the sequence as an array
	 * 
	 * @return contents of the sequence
	 */
	public ArrayList<Double> getSequence(){
		
		return body;
		
	}//end-method	
	
	/**
	 *  Adds an element to the end of the sequence
	 */
	public void append(Object value){	
		
		body.add(Double.valueOf(value.toString()).doubleValue());
		
	}//end-method
	
	/**
	 *  Adds an element to the start of the sequence
	 */
	public void prepend(Object value){	
		
		body.add(0,Double.valueOf(value.toString()).doubleValue());
		
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
	 * Get an element of the sequence
	 * 
	 * @param index index of the element
	 * @return the element. Double.NaN if index out of range
	 */
	public double get(int index){
		
		if((index>-1)&&(index<body.size())){
			return body.get(index);
		}
		
		return Double.NaN;
		
	}//end-method
	
	/**
	 * Clear the sequence
	 */
	public void clear(){
		
		body.clear();
		
	}//end-method
	
	/**
	 * Sorts the sequence
	 */
	public void sort(){
		
		Collections.sort(body);
		
	}//end-method
	
	/**
	 * To String method
	 */
	public String toString() {
	
		String text="";
		
		for (Double s : body){
			text+=s+"*";          
        }
		
		text=text.substring(0,text.length()-1);
	
		return text;
		
	}//end-method

	/**
	 * Loads a string sequence from a XML file
	 * 
	 * @param file path of the file
	 */
	public void readXML(String file){
		
		NumericSequenceXMLReader.readXMLSequence(file);
		body=NumericSequenceXMLReader.getSequence();
		
	}//end-method
	
	/**
	 * Writes a string sequence to a XML file
	 * 
	 * @param file path of the file
	 */
	public void writeXML(String file){
		
		SequenceXMLWriter.writeNumericSequence(body,file);
		
	}//end-method
	
	/**
	 * Loads a string sequence from a CSV file
	 * 
	 * @param file path of the file
	 */
	public void readCSV(String file){
		
		NumericSequenceCSVReader.readCSVSequence(file);
		body=NumericSequenceCSVReader.getSequence();
		
	}//end-method
	
	/**
	 * Writes a string sequence to a CSV file
	 * 
	 * @param file path of the file
	 * @param useComma true true if "," is used as separator, false if ";" is used instead
	 */
	public void writeCSV(String file,boolean useComma){
		
		SequenceCSVWriter.writeNumericSequence(body,file,useComma);
		
	}//end-method
	
	/**
	 * Loads a string sequence from a TXT file
	 * 
	 * @param file path of the file
	 */
	public void readTXT(String file){
		
		NumericSequenceTXTReader.readTXTSequence(file);
		body=NumericSequenceTXTReader.getSequence();
		
	}//end-method
	
	/**
	 * Writes a string sequence to a TXT file
	 * 
	 * @param file path of the file
	 */
	public void writeTXT(String file){
		
		SequenceTXTWriter.writeNumericSequence(body,file);
		
	}//end-method
	
}//end-class

