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

import javanpst.data.structures.dataTable.DataTable;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * A XML reader for numeric tabular data.
 * 
 * Store data inside a inner table. Support null values treatment.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class TabularDataXMLReader{

	/**
	 * Inner table
	 */
	private static DataTable table;

	/**
	 * Initializes the inner table with the specified dimensions
	 * 
	 * @param rows
	 * @param cols
	 */
	static void generateTable(int rows, int cols){
		
		table= new DataTable();
		table.setDimensions(rows,cols);
		
	}//end-method
	
	/**
	 * Get a copy of the inner table as output
	 * 
	 * @return a copy of the inner table
	 */
	public static DataTable getTable(){
		
		DataTable tab = DataTable.newInstance(table);
		
		return tab;
		
	}//end-method
	
	/**
	 * Clears the table
	 */
	static void clear(){
		
		table.clear();
		
	}//end-method
	
	/**
	 * Add a new row to the table in the specified position
	 * 
	 * @param row index of the row
	 * @param array data to add
	 */
	static void addRow(int row,double array []){
		
		table.setRow(row, array);
		
	}//end-method
	
	/**
	 * Set a value of the table as NULL
	 * 
	 * @param row row index
	 * @param col column index
	 */
	static void setNull(int row,int col){
		
		table.setNull(row, col);

	}//end-method

	/**
	 * Read tabular data from a file, through SAX API
	 * 
	 * @param file path of the file
	 */
	public static void readXMLTabularData(String file){
	
		table=new DataTable();
		
		try{
            SAXParserFactory spf=SAXParserFactory.newInstance(); 
            SAXParser sp = spf.newSAXParser();                   
           	sp.parse(file, new TabularDataXMLReaderFormat());	            
        }catch(ParserConfigurationException e){                  
        	System.err.println("Parser error");             
        }catch(SAXException e2){                                 
        	System.err.println("SAX error: " + e2.getStackTrace());
        } catch (IOException e3) {
        	System.err.println("Input/Output error: " + e3.getMessage() );
        }

	}//end-method
	
}//end-class
