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
 * A class for writing numeric and string sequences into a CSV file
 *
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class SequenceCSVWriter{

	/**
	 * Writes a string sequence in a file, in CSV format
	 *
	 * @param seq sequence to write
	 * @param file path of the file
	 * @param commaSeparator true if "," is used as separator, false if ";" is used instead
	 */
	public static void writeStringSequence(ArrayList<String> seq, String file, boolean commaSeparator){

		String text;

		text="";

		for (String s : seq){

			text+=s;

			if(commaSeparator){
				text+=",";
			}
			else{
				text+=";";
			}
        }

		//drop last separator
		text=text.substring(0, text.length()-2);

		Files.writeFile(file,text);

	}//end-method

	/**
	 * Writes a numeric sequence in a file, in CSV format
	 *
	 * @param seq sequence to write
	 * @param file path of the file
	 * @param commaSeparator true if "," is used as separator, false if ";" is used instead
	 */
	public static void writeNumericSequence(ArrayList<Double> seq,String file, boolean commaSeparator){

		String text;

		text="";

		for (Double s : seq){

			text+=s;

			if(commaSeparator){
				text+=",";
			}
			else{
				text+=";";
			}
        }

		//drop last separator
		text=text.substring(0, text.length()-2);

		Files.writeFile(file,text);

	}//end-method

}//end-class
