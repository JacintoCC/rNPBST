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

package javanpst.data.distributions.incompleteTable;

import java.util.Arrays;
import javanpst.data.DataDefinitions;

/**
 * A table representation with 2 keys where some elements are missing.
 *
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class Incomplete2KeyTable{

	/**
	 * Length of each dimension
	 */
	private int dimensions [];

	/**
	 * Matrix of values
	 */
	private double body [][];

	/**
	 * Builder
	 *
	 * @param dim1 length of first dimension
	 * @param dim2 length of second dimension
	 */
	public Incomplete2KeyTable(int dim1, int dim2){

		dimensions= new int[2];

		dimensions[0]=dim1;
		dimensions[1]=dim2;

		body=new double[dim1][dim2];

		clear();

	}//end-method

	/**
	 * Clears the table
	 */
	public void clear(){

		for(int i=0;i<dimensions[0];i++){
			Arrays.fill(body[i],DataDefinitions.UNDEFINED);
		}

	}//end-method

	/**
	 * Erase a value
	 *
	 * @param dim1 first index
	 * @param dim2 second index
	 */
	public void erase(int dim1, int dim2){

		if((dim1>=0) && (dim1<dimensions[0]) &&
				(dim2>=0) && (dim2<dimensions[1])
		){
			body[dim1][dim2]=DataDefinitions.UNDEFINED;
		}

	}//end-method

	/**
	 * Adds a new value
	 *
	 * @param dim1 first index
	 * @param dim2 second index
	 * @param value value to add
	 */
	public void add(int dim1, int dim2,double value){

		if((dim1>=0) && (dim1<dimensions[0]) &&
				(dim2>=0) && (dim2<dimensions[1])
		){
			body[dim1][dim2]=value;
		}

	}//end-method

	/**
	 * Gets a value
	 *
	 * @param dim1 first index
	 * @param dim2 second index
	 * @return value stored
	 */
	public double get(int dim1, int dim2){

		double value;

		if((dim1>=0) && (dim1<dimensions[0]) &&
				(dim2>=0) && (dim2<dimensions[1])
		){
			value=body[dim1][dim2];
		}
		else{
			value=DataDefinitions.UNDEFINED;
		}

		return value;

	}//end-method

}//end-class
