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
 * A class for writing DataTable objects into a XML file
 * 
 * Support null values treatment.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class TabularDataXMLWriter{

	/**
	 * Writes a table in a file, in XML format
	 * 
	 * @param tab table to write
	 * @param file path to the file
	 */
	public static void writeTabularData(DataTable tab, String file){
	
		String text;
		String text2;
		
		//write header
		text="<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";
		
		Files.writeFile(file,text);
		
		text="\n<tabular rows=\""+tab.getRows()+"\" columns=\""+tab.getColumns()+"\">\n";
		
		//write data
		for(int i=0;i<tab.getRows();i++){
			text2="<row>";
			for(int j=0;j<tab.getColumns();j++){
				if(tab.isNull(i, j)){
					text2+="<element>NULL</element>";
				}else{
					text2+="<element>"+tab.get(i, j)+"</element>";
				}			
			}
			text+=text2+"</row>\n";
		}

		text+="</tabular>";
		
		Files.addToFile(file,text);
		
	}//end-method
	
}//end-class
