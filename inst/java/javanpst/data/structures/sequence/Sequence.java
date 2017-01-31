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

package javanpst.data.structures.sequence;

/**
 * An abstract class representing sequences of data
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public abstract class Sequence{

	/**
	 * Constants identifying kind of data
	 */
	public static final int NUMERIC = 0;
	public static final int STRING = 1;
	
	/**
	 * Type of trends in sequences
	 */
	public static final int UP = 1;
	public static final int DOWN = 0;
	
	/**
	 * Get size of the sequence
	 * 
	 * @return size of the sequence
	 */
	public abstract int size();
	
	/**
	 * Clears the sequence
	 */
	public abstract void clear();
	
	/**
	 * Adds a new element to the end of the sequence
	 * @param value value to add
	 */
	public abstract void append(Object value);
	
	/**
	 * Adds a new element to the start of the sequence
	 * @param value value to add
	 */
	public abstract void prepend(Object value);
	
	/**
	 * To String method
	 */
	public abstract String toString();
	
	/**
	 * Loads a sequence from a XML file
	 * 
	 * @param file path of the file
	 */
	public abstract void readXML(String file);
	
	/**
	 * Writes a sequence to a XML file
	 * 
	 * @param file path of the file
	 */
	public abstract void writeXML(String file);
	
	/**
	 * Loads a sequence from a CSV file
	 * 
	 * @param file path of the file
	 */
	public abstract void readCSV(String file);

	/**
	 * Writes a sequence to a CSV file
	 * 
	 * @param file path of the file
	 * @param useComma true true if "," is used as separator, false if ";" is used instead
	 */
	public abstract void writeCSV(String file, boolean useComma);
	
	/**
	 * Loads a sequence from a TXT file
	 * 
	 * @param file path of the file
	 */
	public abstract void readTXT(String file);
	
	/**
	 * Writes a sequence to a TXT file
	 * 
	 * @param file path of the file
	 */
	public abstract void writeTXT(String file);
	
}//end-class