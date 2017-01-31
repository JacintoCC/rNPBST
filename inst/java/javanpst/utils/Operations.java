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

import java.math.BigInteger;

/**
 * A class with some useful mathematical functions.
 * BigInteger class supports some of the operations
 * performed here
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class Operations{

	/**
	 * Array with the factorial values from N = 0 to N = 11
	 */
	private static final int[] factorialValues = {
          1,
          1,
          2,
          6,
          24,
          120,
          720,
          5040,
          40320,
          362880,
          3628800,
          39916800
    };
	  
	/**
	 * Computes the factorial of a given integer
	 * 
	 * @param n integer value
	 * @return Factorial of n
	 */
	public static double factorial(int n){
		
		if(n>-1){
			if(n<=20){
				return factorialValues[n];
			}
			else{
			
				double  value = 1.0;
			
	        	value=fact(n).doubleValue();
	
	        	return value;
			}
		}
		
		return n;
        
    }//end-method
	
	/**
	 * Computes the binomial coefficient (m over n)
	 *  
	 * @param m m value
	 * @param n n value
	 * @return binomial coefficient
	 */
	public static double combinatorial(int m,int n){
		
		double  value;

		value = (fact(m).divide((fact(n).multiply(fact(m-n))))).doubleValue();
	
        return value;
        
    }//end-method
	
	/**
	 * Computes the factorial of a given integer
	 * Uses BigInteger class to provide a robust implementation 
	 * for medium values of n
	 * 
	 * @param n n value
	 * @return Factorial of n,as a BigInteger
	 */
	private static BigInteger fact(int n){
		
		if(n>-1){
			if(n<=11){
				return new BigInteger(""+factorialValues[n]);
			}
			else{
				
				BigInteger k = new BigInteger(""+factorialValues[11]);
		        for (int i=12; i<=n; i++) {
		            k = k.multiply(BigInteger.valueOf(i));
		        }

		        return k;
		        
			}
		}else{
			
			return BigInteger.ZERO;
			
		}
        
    }//end-method

}//end-class
