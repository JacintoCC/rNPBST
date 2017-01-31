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

package javanpst.data.writers.tabularDataWriters;

import javanpst.data.structures.dataTable.DataTable;
import javanpst.utils.Files;

/**
 * A class for writing DataTable objects into a CSV file
 * 
 * Support null values treatment.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class TabularDataCSVWriter{

	/**
	 * Writes a table in a file, in CSV format.
	 * 
	 * @param tab table to write
	 * @param file path to the file
	 * @param commaSeparator true if "," is used as separator, false if ";" is used instead
	 */
	public static void writeTabularData(DataTable tab, String file, boolean commaSeparator){
	
		String text;
		String text2;
		
		text="";
		
		//write data
		for(int i=0;i<tab.getRows();i++){
			
			text2="";
			
			for(int j=0;j<tab.getColumns();j++){
				
				if(tab.isNull(i, j)){
					text2+="NULL";
				}else{
					text2+="tab.get(i, j)";
				}	
				
				if(j!=tab.getColumns()-1){
					if(commaSeparator){
						text2+=",";
					}
					else{
						text2+=";";
					}
				}
			}
			
			text+=text2+"\n";
		}
		
		Files.writeFile(file,text);
		
	}//end-method
	
}//end-class
