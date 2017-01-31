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

import java.util.StringTokenizer;

import javanpst.data.structures.dataTable.DataTable;
import javanpst.utils.Files;

/**
 * A TXT reader for numeric tabular data.
 * 
 * Store data inside a inner table. Support null values treatment.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class TabularDataTXTReader{

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
	private static void generateTable(int rows, int cols){
		
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
	 * Adds a new value to the table
	 * 
	 * @param value string representation of value to add
	 * @param row row selected
	 * @param col column selected
	 * @return true if the value was added correctly. False, otherwise
	 */
	private static boolean addValue(String value,int row,int col){
		
		double val;
		
		if(value=="NULL"){
			table.setNull(row, col);
		}
		
		try{
			val=Double.parseDouble(value);
		}catch(Exception e){
			System.out.println("Parsing error on row "+row+" column "+col);
			return false;
		}
		
		table.setValue(row, col, val);
		
		return true;
		
	}//end-method


	/**
	 * Read tabular data from a TXT file
	 * 
	 * @param file path of the file
	 */
	public static void readTXTTabularData(String file){
		
		String content,content2,value;
		StringTokenizer tokens,tokens2;
		int indexRow,indexCol;
		boolean state;
		
		content=Files.readFile(file);
		
		extractDimensions(content);
		
		tokens = new StringTokenizer (content, "\n");
		
		indexRow=0;
		indexCol=0;
		state=true;
		
		while(tokens.hasMoreElements()){
			
			content2=tokens.nextToken();
			
			tokens2 = new StringTokenizer (content2, ";");
			
			if(tokens2.countTokens()!=table.getColumns()){
				table.clear();
				System.out.println("\nError reading "+table.getRows()+" x "+table.getColumns()+" table. " +
						"The number of colums is wrong.\n");
				return;
			}
			
			while(tokens2.hasMoreElements()){
				
				value=tokens2.nextToken();
				
				state= state && addValue(value,indexRow,indexCol);
				
				indexCol++;
			}
			
			if(!state){
				table.clear();
				return;
			}
			
			indexRow++;
			indexCol=0;
		}
		
	}//end-method
	
	/**
	 * Guess dimensions of the table and generates the inner table of the reader
	 * 
	 * @param text contents of the table in TXT format
	 */
	private static void extractDimensions(String text){
		
		int nRows,nColumns;
		String line;
		
		StringTokenizer tokens,tokens2;
		
		tokens = new StringTokenizer (text, "\n");
		
		nRows = tokens.countTokens();
		
		line = tokens.nextToken();
		
		tokens2 = new StringTokenizer (line, ";");
		
		nColumns = tokens2.countTokens();
		
		//Generate table
		generateTable(nRows,nColumns);
		
	}//end-method
	
}//end-class
