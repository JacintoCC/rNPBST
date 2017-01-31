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

package javanpst.data.readers.tabularDataReaders;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javanpst.data.DataDefinitions;

/**
 * XML format for reading tables.
 * 
 * Support null values treatment.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class TabularDataXMLReaderFormat extends DefaultHandler {

	/**
	 * Temporal string buffer
	 */
	String st;
	
	/**
	 * Number of elements in the current row
	 */
	int counter;
	
	/**
	 * Current row in the table
	 */
	int rowCount;
	
	/**
	 * Temporal buffer for rows
	 */
	double array [];
	
	/**
	 * Number of rows of the table
	 */
	int rows;
	
	/**
	 * Number of columns of the table
	 */
	int cols;

	/**
	 * Builder
	 */
	public TabularDataXMLReaderFormat(){
		
		super();
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

    	if(qName.equals("tabular")){
    		cols=Integer.parseInt(attributes.getValue("columns"));
    		rows=Integer.parseInt(attributes.getValue("rows"));
    		TabularDataXMLReader.generateTable(rows,cols);
    		rowCount=0;
    		
    	}
    	
    	if(qName.equals("row")){
    		array=new double [cols];
    		counter=0;
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
			addElement();
		}
		if(qName.equals("row")){
			addRow();
		}
		
    }//end-method
	
    /**
     * Clears the inner table of the reader
     */
	private void clearBuffer(){
		
		TabularDataXMLReader.clear();
		
	}//end-method
	
	
	/**
	 * Adds a new element to the current row
	 */
	private void addElement(){

		if(st.equals("NULL")){
			TabularDataXMLReader.setNull(rowCount, counter);
			array[counter]=DataDefinitions.NULL_VALUE;
		}
		else{
			array[counter]=Double.parseDouble(st);
		}
		
		counter++;
		
	}//end-method
	
	/**
	 * Adds a new row to the table
	 */
	private void addRow(){
		
		TabularDataXMLReader.addRow(rowCount,array);
		rowCount++;
		
	}//end-method
	
}//end-class
