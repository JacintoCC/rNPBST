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

import java.util.Arrays;

import javanpst.distributions.DistributionDefinitions;
import javanpst.distributions.tests.KendallDistribution;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * XML format for reading the Kendall Exact distribution table
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class KendallExactReaderFormat extends DefaultHandler {

	/**
	 * n parameter
	 */
	int n;
	
	/**
	 * New n parameter
	 */
	int newN;
	
	/**
	 * index
	 */
	int counter;
	
	/**
	 * Array of probabilities
	 */
	double [] probs;
	
	/**
	 * Array of keys
	 */
	double [] keys;
	
	/**
	 * Temporal string buffer
	 */
	String st;
	
	/**
	 * Default builder
	 */
	public KendallExactReaderFormat(){
		
		super();
		
		counter=0;
		probs=new double [25];
		keys=new double [25];
		clearTable();	
		
	}//end-method
	
	/**
	 * Operations performed at the start of document
	 */
	public void startDocument() throws SAXException{
   
		Arrays.fill(probs,DistributionDefinitions.UNDEFINED);
		Arrays.fill(keys,DistributionDefinitions.UNDEFINED);
		n=3;
		
	}//end-method
	
	/**
	 * Operations performed at the end of document
	 */
	public void endDocument()throws SAXException{
		
		addRow();
		
	}//end-method

	/**
	 * Operations performed at the start of an element
	 */
    public void startElement(String uri, String localName, String qName, Attributes attributes) {

    	if(qName.equals("element")){
    		newN=Integer.parseInt(attributes.getValue("n"));
    		
    		if(newN!=n){
    			addRow();
    			counter=0;
    			Arrays.fill(probs,DistributionDefinitions.UNDEFINED);
    			Arrays.fill(keys,DistributionDefinitions.UNDEFINED);
    			n=newN;
    		}
    		keys[counter]=Double.parseDouble(attributes.getValue("T"));
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
		
    }//end-method
	
    /**
     * Clears the inner table of the reader
     */
	private void clearTable(){
		
		KendallDistribution.getExactTable().clear();
				
	}//end-method
	
	/**
	 * Adds a new row to the table
	 */
	private void addRow(){
	
		KendallDistribution.getExactTable().addRow(n, keys, probs);
		
	}//end-method
	
}//end-class

