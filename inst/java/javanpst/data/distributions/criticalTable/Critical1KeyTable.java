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

package javanpst.data.distributions.criticalTable;

import java.util.Arrays;
import javanpst.data.DataDefinitions;

/**
 * A table representation with 1 key and critical levels.
 *
 * In each row of this table, several critical values are stored,
 * being each one assigned to a previously especified p-value
 *
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class Critical1KeyTable{

	/**
	 * Matrix of values
	 */
	private double body [][];

	/**
	 * Array of p-values
	 */
	private double pValues [];

	/**
	 * Buider.
	 *
	 * @param lenght number of rows of the table
	 * @param criticals array of p-values
	 */
	public Critical1KeyTable(int lenght,double [] criticals){

		body=new double[lenght][criticals.length];
		pValues=new double [criticals.length];

		System.arraycopy(criticals, 0, pValues, 0, criticals.length);

		clear();

	}//end-method

	/**
	 * Returns the p-value assigned to a given position
	 *
	 * @param index position selected
	 * @return p-value associated
	 */
	public double getHeader(int index){

		return pValues[index];

	}//end-method

	/**
	 * Clears the table
	 */
	public void clear(){

		for(int i=0;i<body.length;i++){
			Arrays.fill(body[i],DataDefinitions.UNDEFINED);
		}

	}//end-method

	/**
	 * Clears a row given an integer index
	 *
	 * @param index row to clear
	 */
	public void erase(int index){

		Arrays.fill(body[index],DataDefinitions.UNDEFINED);

	}//end-method

	/**
	 * Copy the values of a new row to the table
	 *
	 * @param index row to modify
	 * @param values array of values of the new row
	 */
	public void addRow(int index, double [] values){

		System.arraycopy(values, 0, body[index], 0, values.length);

	}//end-method

	/**
	 * Get a value from the table
	 *
	 * @param dim1 row selected
	 * @param dim2 critical level position selected
	 * @return value stored
	 */
	public double get(int dim1, int dim2){

		double value;

		value=body[dim1][dim2];

		return value;

	}//end-method

}//end-class
