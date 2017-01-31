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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * XML format for reading sequences.
 * 
 * Support both numerical and string sequences.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class SequenceXMLReaderFormat extends DefaultHandler {

	/**
	 * Identifier for string sequences
	 */
	public static final int STRING = 0;
	
	/**
	 * Identifier for numerical sequences
	 */
	public static final int NUMERIC = 1;
	
	/**
	 * Type of sequence
	 */
	int type;
	
	/**
	 * Temporal string buffer
	 */
	String st;
	
	/**
	 * Builder
	 * 
	 * @param type type of sequence
	 */
	public SequenceXMLReaderFormat(int type){
		
		super();
		this.type=type;
		clearBuffer();		
		
	}//end-method
	
	/**
	 * Operations performed at the start of document
	 */
	public void startDocument() throws SAXException{
  
	}//end-method
	
	/**
	 * Operations performed at the end of document
	 */
	public void endDocument()throws SAXException{

	}//end-method

	/**
	 * Operations performed at the start of an element
	 */
    public void startElement(String uri, String localName, String qName, Attributes attributes) {

    }//end-method

    /**
     * Read of text strings
     */
    public void characters(char buf[], int offset, int len) throws SAXException{
    	
        st = new String(buf, offset, len); 
        
    }//end-method
       
    /**
	 * Operations performed at the end of an element
	 */
    public void endElement(String uri, String localName, String qName) {
    	
		if(qName.equals("element")){
			addElement();
		}
		
    }//end-method
	
    /**
     * Clears the inner array of the reader
     */
	private void clearBuffer(){
	
		switch(type){
		
			case NUMERIC:
				NumericSequenceXMLReader.clear();
				break;
				
			case STRING:
				StringSequenceXMLReader.clear();
				break;
		}
		
	}//end-method
	
	/**
	 * Adds a new element to the array
	 */
	private void addElement(){
	
		switch(type){
		
			case NUMERIC:
				NumericSequenceXMLReader.append(st);
				break;
				
			case STRING:
				StringSequenceXMLReader.append(st);
				break;
		}
		
	}//end-method
	
}//end-class
