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

package javanpst.data.writers.sequenceWriters;

import javanpst.utils.Files;
import java.util.ArrayList;

/**
 * A class for writing numeric and string sequences into a XML file
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class SequenceXMLWriter{

	/**
	 * Writes a string sequence in a file, in XML format
	 * 
	 * @param seq sequence to write
	 * @param file path of the file
	 */
	public static void writeStringSequence(ArrayList<String> seq, String file){
	
		String text;
		
		//write header
		text="<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";
		
		Files.writeFile(file,text);
		
		//write sequence
		text="\n<sequence>\n";
		
		for (String s : seq){
			text+="\t<element>"+s+"</element>\n";          
        }
		text+="</sequence>";
		
		Files.addToFile(file,text);
		
	}//end-method
	
	/**
	 * Writes a numeric sequence in a file, in XML format
	 * 
	 * @param seq sequence to write
	 * @param file path of the file
	 */
	public static void writeNumericSequence(ArrayList<Double> seq,String file){
	
		String text;
		
		//write header
		text="<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";
		
		Files.writeFile(file,text);
		
		//write sequence
		text="\n<sequence>\n";
		
		for (Double s : seq){
			text+="<element>"+s+"</element>\n";          
        }
		text+="\n</sequence>";
		
		Files.addToFile(file,text);
		
	}//end-method
	
}//end-class
