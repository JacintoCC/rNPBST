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

package javanpst.data.readers.distributionReaders;

import javanpst.distributions.tests.PartialCorrelationDistribution;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * XML format for reading the Partial correlation distribution table
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class PartialCorrelationReaderFormat extends DefaultHandler {

	/**
	 * m parameter
	 */
	int m;
	
	/**
	 * index
	 */
	int counter;
	
	/**
	 * Array of probabilities
	 */
	double [] probs;
	
	/**
	 * Temporal string buffer
	 */
	String st;
	
	/**
	 * Default builder
	 */
	public PartialCorrelationReaderFormat(){
		
		super();
		
		counter=0;
		probs=new double [4];
		
		clearTable();
		
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

    	if(qName.equals("row")){
    		m=Integer.parseInt(attributes.getValue("m"));
    	}
    	
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
			probs[counter]=Double.parseDouble(st);
			counter++;
		}
		if(qName.equals("row")){
			addRow();
			counter=0;
		}
		
    }//end-method
	
    /**
     * Clears the inner table of the reader
     */
	private void clearTable(){
		
		PartialCorrelationDistribution.getTable().clear();
				
	}//end-method
	
	/**
	 * Adds a new row to the table
	 */
	private void addRow(){
	
		PartialCorrelationDistribution.getTable().addRow(m, probs);
		
	}//end-method
	
}//end-class

