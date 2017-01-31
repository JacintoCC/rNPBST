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

import javanpst.distributions.tests.WilcoxonDistribution;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * XML format for reading the Wilcoxon distribution table
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class WilcoxonReaderFormat extends DefaultHandler {

	/**
	 * n parameter
	 */
	int n;
	
	/**
	 * r parameter
	 */
	int r;
	
	/**
	 * Temporal string buffer
	 */
	String st;
	
	/**
	 * Default builder
	 */
	public WilcoxonReaderFormat(){
		
		super();
		
		r=0;
		n=4;
		
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
			n++;
		}
		if(qName.equals("row")){
			n=4;
			r++;
		}
		
    }//end-method
	
    /**
     * Clears the inner table of the reader
     */
	private void clearTable(){
		
		WilcoxonDistribution.getTable().clear();
				
	}//end-method
	
	/**
	 * Adds a new row to the table
	 */
	private void addElement(){
	
		WilcoxonDistribution.getTable().add(r, n, Double.parseDouble(st));
		
	}//end-method
	
}//end-class
