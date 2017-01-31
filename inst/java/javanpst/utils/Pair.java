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
 * A implementation of a pair of double values
 * 
 * The pair can be sorted by its first value (a)
 * 
 * @author Joaquin
 * @version 1.0
 */
public class Pair implements Comparable<Object>{
	
	/**
	 * First value
	 */
	public double a;
	
	/**
	 * Second value
	 */
	public double b;
	
	/**
	 * Default builder.
	 */
	public Pair(){
		
		a=0;
		b=0;
		
	}//end-method
	
	/**
	 * Builder
	 * 
	 * @param aVal first value
	 * @param bVal second value
	 */
	public Pair(double aVal, double bVal){
		
		a=aVal;
		b=bVal;
		
	}//end-method
	
	/**
	 * To string method
	 */
	public String toString(){
		
		return "("+a+","+b+")";
		
	}//end-method

	@Override
	/**
	 * Compare to method.
	 * Sort pairs in ascendent order, according
	 * to their first value 
	 */
	public int compareTo(Object arg0) {
		
		Pair other=(Pair)arg0;
		if(a<other.a){
			return -1;
		}
		if(a>other.a){
			return 1;
		}
		
		return 0;
		
	}//end-method
	
}//end-class
