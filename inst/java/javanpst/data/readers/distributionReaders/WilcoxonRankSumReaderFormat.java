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

import javanpst.distributions.tests.WilcoxonRankSumDistribution;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * XML format for reading the Wilcoxon Ranks-Sum distribution table
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class WilcoxonRankSumReaderFormat extends DefaultHandler {

	/**
	 * x parameter
	 */
	int x;
	
	/**
	 * y parameter
	 */
	int y;
	
	/**
	 * z parameter
	 */
	int z;
	
	/**
	 * probability
	 */
	double prob;
	
	/**
	 * Temporal string buffer
	 */
	String st;
	
	/**
	 * Default builder
	 */
	public WilcoxonRankSumReaderFormat(){
		
		super();
		clearTables();	
		
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

    	if(qName.equals("element")){
    		x=Integer.parseInt(attributes.getValue("x"));
    		y=Integer.parseInt(attributes.getValue("y"));
    		z=Integer.parseInt(attributes.getValue("z"));
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
			prob=Double.parseDouble(st);
			addElement();
		}
		
    }//end-method
	
    /**
     * Clears the inner table of the reader
     */
	private void clearTables(){
	
		WilcoxonRankSumDistribution.getTable().clear();
		
	}//end-method
	
	/**
	 * Adds a new row to the table
	 */
	private void addElement(){
	
		WilcoxonRankSumDistribution.getTable().add(x, y, z, prob);

	}//end-method
	
}//end-class
