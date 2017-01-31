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

package javanpst.utils;

/**
 * A class with some procedures to test format of data
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class Formats{
	
	/**
	 * Tests if a given real value is an integer, with a given error bound
	 *  
	 * @param x value to test
	 * @param delta error bound
	 * @return true if the number is an integer, false if not
	 */
	private static boolean isInt(double x, double delta) {
		
	    double ceil = Math.ceil(x);
	    return x-delta<ceil && x+delta>ceil;
	    
	}//end-method

	/**
	 * Tests if a given real value is an integer, with a given error bound of 10e-9
	 *  
	 * @param x value to test
	 * @return true if the number is an integer, false if not
	 */
	public static boolean isInt(double x) {
		
	    return isInt(x, 0.000000001);
	    
	}//end-method
	
}//end-class
