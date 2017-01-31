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

package javanpst.data.distributions.aproximateTable;

import java.util.Arrays;
import javanpst.data.DataDefinitions;

/**
 * A table representation with 1 aproximated key.
 *
 * This table features two different indexes: a primary, integer one,
 * and a secondary which is real valued. A maximum difference (EPSILON)
 * is allowed in this second value
 *
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class Aproximate1KeyTable{

	/**
	 * Matrix of keys (indexes)
	 */
	private double keys [][];

	/**
	 * Matrix of values
	 */
	private double values [][];

	/**
	 * Maximum error allowed
	 */
	private static double EPSILON = 0.002;

	/**
	 * Builder
	 *
	 * @param integerLenght Dimension of the integer index
	 * @param realLenght Dimension of the real valued index
	 */
	public Aproximate1KeyTable(int integerLenght,int realLenght){

		keys=new double[integerLenght][realLenght];
		values=new double[integerLenght][realLenght];

		clear();

	}//end-method

	/**
	 * Clears the table
	 */
	public void clear(){

		for(int i=0;i<keys.length;i++){
			Arrays.fill(keys[i],DataDefinitions.UNDEFINED);
			Arrays.fill(values[i],DataDefinitions.UNDEFINED);
		}

	}//end-method

	/**
	 * Clears a row given an integer index
	 *
	 * @param index row to clear
	 */
	public void erase(int index){

		Arrays.fill(keys[index],DataDefinitions.UNDEFINED);
		Arrays.fill(values[index],DataDefinitions.UNDEFINED);

	}//end-method

	/**
	 * Copy the values of a new row to the table
	 *
	 * @param index row to modify
	 * @param keys array of keys of the new row
	 * @param values array of values of the new row
	 */
	public void addRow(int index, double [] keys, double [] values){

		System.arraycopy(keys, 0, this.keys[index], 0, values.length);
		System.arraycopy(values, 0, this.values[index], 0, values.length);

	}//end-method

	/**
	 * Get a value, given an integer index and an approximate key
	 *
	 * @param index integer index
	 * @param aproximate
	 * @return value recovered, UNDEFINED if not found any
	 */
	public double get(int index, double aproximate){

		double value=DataDefinitions.UNDEFINED;
		boolean found=false;
		int pointer=0;

		while((!found)&&(keys[index][pointer]!=DataDefinitions.UNDEFINED)&&(pointer<keys[index].length)){

			if(Math.abs(keys[index][pointer]-aproximate)<=EPSILON){
				found=true;
			}
			else{
				pointer++;
			}
		}

		if(found){
			value=values[index][pointer];
		}

		return value;

	}//end-method

}//end-class
