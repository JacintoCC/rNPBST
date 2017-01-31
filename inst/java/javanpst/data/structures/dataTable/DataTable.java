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

package javanpst.data.structures.dataTable;

import java.util.Arrays;
import javanpst.data.DataDefinitions;
import javanpst.data.readers.tabularDataReaders.TabularDataCSVReader;
import javanpst.data.readers.tabularDataReaders.TabularDataTXTReader;
import javanpst.data.readers.tabularDataReaders.TabularDataXMLReader;
import javanpst.data.writers.tabularDataWriters.TabularDataCSVWriter;
import javanpst.data.writers.tabularDataWriters.TabularDataTXTWriter;
import javanpst.data.writers.tabularDataWriters.TabularDataXMLWriter;

/**
 * A general purpose data table.
 * 
 * Support null values treatment.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class DataTable{

	/**
	 * Contents of the table
	 */
	private double body[][];
	
	/**
	 * Null values representation (true = null, false = not null)
	 */
	private boolean nulls [][];


	/**
	 * Default builder
	 */
	public DataTable(){
		
		body=new double [0][0];
		nulls=new boolean [0][0];
		
	}//end-method
	
	/**
	 * Builder. Built a new table from a data matrix
	 * 
	 * Both matrixes must have the same dimensions, else
	 * a void table is created
	 * 
	 * @param data contents of the table
	 */
	public DataTable(double data[][]){
	
		body=new double [data.length][data[0].length];
		nulls=new boolean [data.length][data[0].length];
		
		for(int i=0;i<data.length;i++){
			System.arraycopy(data[i], 0, body[i], 0, data[0].length);
			Arrays.fill(nulls[i], false);
		}
		
	}//end-method
	
	/**
	 * Builder. Allows to specify which values are null.
	 * 
	 * Both matrixes must have the same dimensions, else
	 * a void table is created
	 * 
	 * @param data contents of the table
	 * @param nullV null values matrix
	 */
	public DataTable(double data[][], boolean nullV [][]){

		if((data.length==nullV.length)&&(data[0].length==nullV[0].length)){
			
			body=new double [data.length][data[0].length];
			nulls=new boolean [data.length][data[0].length];
			
			for(int i=0;i<data.length;i++){
				System.arraycopy(data[i], 0, body[i], 0, data[0].length);
				System.arraycopy(nullV[i], 0, nulls[i], 0, data[0].length);
			}
		}
		else{
			body=new double [0][0];
			nulls=new boolean [0][0];
		}
		
	}//end-method
	
	/**
	 * Copy constructor
	 * 
	 * @param aTable old table
	 * @return a copy of aTable
	 */
	public static DataTable newInstance(DataTable aTable){
		
		DataTable copy= new DataTable(aTable.body,aTable.nulls);
		
		return copy;
		
	}//end-method
	
	/**
	 * Get the data matrix
	 * 
	 * @return values of the table
	 */
	public double [][] getBody(){
		
		return body;
		
	}//end-method
	
	/**
	 * Get the matrix of nulls
	 * 
	 * @return null values of the table
	 */
	public boolean [][] getNulls(){
		
		return nulls;
		
	}//end-method 
	
	/**
	 * Returns if the selected value is null of not
	 * 
	 * @return true if it is null, false otherwise
	 */
	public boolean isNull(int row, int col){
		
		return nulls[row][col];
		
	}//end-method 
	
	/**
	 * Get the number of rows
	 * 
	 * @return number of rows of the table
	 */
	public int getRows(){
		
		return body.length;
		
	}//end-method
	
	/**
	 * Get the number of columns
	 * 
	 * @return number of columns of the table
	 */
	public int getColumns(){
		
		return body[0].length;
		
	}//end-method
	
	/**
	 * Clear method.
	 * 
	 * Set all values of the table to null
	 */
	public void clear(){
			
		for(int i=0;i<body.length;i++){
			Arrays.fill(nulls[i],true);
		}
		
	}//end-method
	
	/**
	 * Set new dimensions.
	 * 
	 * Table is cleared after resizing
	 * 
	 * @param row new number of rows
	 * @param col new number of columns
	 */
	public void setDimensions(int row,int col){
		
		body = new double [row][col];
		nulls = new boolean [row][col];
		
		clear();
		
	}//end-method
	
	/**
	 * Set new number of rows
	 * 
	 * @param row new number of rows
	 */
	public void setRows(int row){
		
		body = new double [row][body[0].length];
		nulls = new boolean [row][body[0].length];
		
		clear();
		
	}//end-method
	
	/**
	 * Set new number of columns
	 * 
	 * @param col new number of columns
	 */
	public void setColumns(int col){
		
		body = new double [body.length][col];
		nulls = new boolean [body.length][col];
		
		clear();
		
	}//end-method
	
	/**
	 * Sets a new value
	 * 
	 * @param row row index
	 * @param col col index
	 * @param value value to set
	 */
	public void setValue(int row,int col, double value){
		
		body[row][col]=value;
		nulls[row][col]=false;
		
	}//end-method
	
	/**
	 * Sets a null value
	 * 
	 * @param row row index
	 * @param col col index
	 */
	public void setNull(int row,int col){
		
		nulls[row][col]=true;
		
	}//end-method
	
	/**
	 * Sets a entire row
	 * @param row row to set
	 * @param array array of values
	 */
	public void setRow(int row,double [] array){
		
		if(array.length<=body[row].length){
			System.arraycopy(array, 0, body[row], 0, array.length);
			Arrays.fill(nulls[row], false);
		}
		
	}//end-method
	
	/**
	 * Sets a entire column
	 * @param col column to set
	 * @param array array of values
	 */
	public void setColumn(int col,double [] array){
		
		if(array.length<=body.length){
			for(int i=0;i<array.length;i++){
				body[i][col]=array[i];
				nulls[i][col]=false;
			}
		}
		
	}//end-method
	
	/**
	 * Get the number of null values in the selected row
	 * 
	 * @param row row selected
	 * @return number of null values
	 */
	public int getRowNulls(int row){
		
		int counter=0;
		
		for(int i=0;i<body[0].length;i++){
			
			if(nulls[row][i]){
				counter++;
			}
		}
		
		return counter;
		
	}//end-method
	
	/**
	 * Get the number of null values in the selected column
	 * 
	 * @param col column selected
	 * @return number of null values
	 */
	public int getColumnNulls(int col){
		
		int counter=0;
		
		for(int i=0;i<body.length;i++){
			
			if(nulls[i][col]){
				counter++;
			}
		}
		
		return counter;
		
	}//end-method
	
	/**
	 * Get a value.
	 * 
	 * Returns NULL_VALUE if null.
	 * 
	 * @param row row index
	 * @param col col index
	 * @return value stored
	 */
	public double get(int row,int col){
		
		if(nulls[row][col]){
			return DataDefinitions.NULL_VALUE;
		}
		
		return body[row][col];
		
	}//end-method
	
	/**
	 * Get a row
	 * 
	 * (no null values are considered)
	 * 
	 * @param row row index
	 * @return array with values stored
	 */
	public double [] getRow(int row){
		
		double copy [];
		
		copy=new double[body[row].length];
		
		System.arraycopy(body[row], 0,copy, 0, body[row].length);
		
		return copy;
		
	}//end-method
	
	/**
	 * Get a column
	 * 
	 * (no null values are considered)
	 * 
	 * @param col column index
	 * @return array with values stored
	 */
	public double [] getColumn(int col){
		
		double copy [];
		
		copy=new double[body.length];
		
		for(int i=0;i<copy.length;i++){
			copy[i]=body[i][col];
		}
		
		return copy;
		
	}//end-method

	/**
	 * To String method
	 */
	public String toString() {
	
		String text="";
		String text2;
		
		text+="*** Rows: "+body.length+" Columns: "+body[0].length+" **********\n";
		
		for(int i=0;i<body.length;i++){
			text2="";
			for(int j=0;j<body[0].length;j++){
				text2+=body[i][j]+"\t";
			}
			text+=text2+"\n";
		}
		
		return text;
		
	}//end-method	
	
	/**
	 * Loads a table from a XML file
	 * 
	 * @param file path of the file
	 */
	public void readXML(String file){
		
		TabularDataXMLReader.readXMLTabularData(file);
		
		body=TabularDataXMLReader.getTable().getBody();
		nulls=TabularDataXMLReader.getTable().getNulls();
		
	}//end-method
	
	/**
	 * Writes a table to a XML file
	 * 
	 * @param file path of the file
	 */
	public void writeXML(String file){
		
		TabularDataXMLWriter.writeTabularData(this, file);
		
	}//end-method
	
	/**
	 * Loads a table from a CSV file
	 * 
	 * @param file path of the file
	 */
	public void readCSV(String file){
		
		TabularDataCSVReader.readCSVTabularData(file);
		
		body=TabularDataCSVReader.getTable().getBody();
		nulls=TabularDataCSVReader.getTable().getNulls();
		
	}//end-method
	
	/**
	 * Writes a table to a CSV file
	 * 
	 * @param file path of the file
	 * @param useComma true true if "," is used as separator, false if ";" is used instead
	 */
	public void writeCSV(String file,boolean useComma){
		
		TabularDataCSVWriter.writeTabularData(this, file, useComma);
		
	}//end-method
	
	/**
	 * Loads a table from a TXT file
	 * 
	 * @param file path of the file
	 */
	public void readTXT(String file){
		
		TabularDataTXTReader.readTXTTabularData(file);
		
		body=TabularDataTXTReader.getTable().getBody();
		nulls=TabularDataTXTReader.getTable().getNulls();
		
	}//end-method
	
	/**
	 * Writes a table to a TXT file
	 * 
	 * @param file path of the file
	 */
	public void writeTXT(String file){
		
		TabularDataTXTWriter.writeTabularData(this, file);
		
	}//end-method

}//end-class

