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
import javanpst.distributions.DistributionDefinitions;

/**
 * A table representation with 2 keys and critical levels.
 *
 * In each row of this table, several critical values are stored,
 * being each one assigned to a previously specified p-value
 *
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class Critical2KeyTable{

	/**
	 * Matrix of values
	 */
	private double body [][][];

	/**
	 * Array of p-values
	 */
	private double pValues [];

	/**
	 * Buider.
	 *
	 * @param dim1 number of rows of the table
	 * @param dim2 number of columns of the table
	 * @param criticals array of p-values
	 */
	public Critical2KeyTable(int dim1,int dim2, double [] criticals){

		pValues=new double [criticals.length];
		body=new double [dim1][dim2][criticals.length];

		System.arraycopy(criticals, 0, pValues, 0, criticals.length);

		clear();

	}//end-method

	/**
	 * Clears the table
	 */
	public void clear(){

		for(int i=0;i<body.length;i++){
			for(int j=0;j<body[0].length;j++){
				Arrays.fill(body[i][j], DataDefinitions.UNDEFINED);
			}
		}
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
	 * Estimates a pValue given a statistic value and two indexes
	 *
	 * @param dim1 first index
	 * @param dim2 second index
	 * @param critical statistic value
	 * @return p-value found
	 */
	public double estimate(int dim1,int dim2,double critical){

		boolean found=false;
		int pointer=0;

		while((!found)&&(pointer<pValues.length)){
			if(critical>=body[dim1][dim2][pointer]){
				found=true;
			}
			else{
				pointer++;
			}
		}

		if(found){
			return getHeader(pointer);
		}
		else{
			return DistributionDefinitions.ALL;
		}

	}//end-method

	/**
	 * Copy a value in the table
	 *
	 * @param dim1 first index
	 * @param dim2 second index
	 * @param keyCritical p-value assigned (index)
	 * @param value value to set
	 */
	public void set(int dim1,int dim2,int keyCritical,double value){

		body[dim1][dim2][keyCritical]=value;

	}//end-method

}//end-class
