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

package javanpst.distributions.common.discrete;

import javanpst.distributions.common.Distribution;

/**
 * A uniform distribution, with parameter N
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */

public final class UniformDistribution implements Distribution{
	
	/**
	 * N parameter (number of elements in the distribution)
	 */
	private int n;
	
	/**
	 * Default builder.
	 * 
	 * Creates a uniform distribution with parameter N = 1
	 */
	public UniformDistribution(){

		n=1;
		
	}//end-method
	
	/**
	 * Builder.
	 * 
	 * Creates a uniform distribution with parameter N
	 * 
	 * @param n N parameter
	 */
	public UniformDistribution(int n){
		
		this.n=n;
		
	}//end-method
	
	/**
	 * Copy constructor
	 * 
	 * Creates a copy of a given uniform distribution
	 * 
	 * @param old uniform distribution to copy
	 */
	public UniformDistribution(UniformDistribution old){
		
		this(old.getN());
		
	}//end-method
	
	/**
	 * Sets the N parameter.
	 * 
	 * N must be positive
	 * 
	 * @param value N parameter value
	 */
	public void setN(int value){
		
		if(value > 0){
			n=value;
		}
		
	}//end-method
	
	/**
	 * Gets the N parameter value
	 * 
	 * @return value of N
	 */
	public int getN(){
		
		return n;
		
	}//end-method
	
	/**
	 * Computes mass probability at a given point of the uniform distribution.
	 * 
	 * Value given is converted to integer before computing probability.
	 * 
	 * @param value point selected
	 * @return mass probability at 'value'
	 */
	public double computeProbability(double value){
		
		double prob=0.0;
		int x = (int) Math.floor(value);
		
		if(x<1){
			prob=0.0;
		}
		else{
			if(x<=n){
				prob=1.0/(double)n;
			}
			else{
				prob=0.0;
			}
		}
		
		return prob;
		
	}//end-method
	
	/**
	 * Computes cumulative probability at a given point of the uniform distribution.
	 * 
	 * Value given is converted to integer before computing probability.
	 * 
	 * @param value point selected
	 * @return cumulative probability at 'value'
	 */
	public double computeCumulativeProbability(double value){
		
		double prob=0.0;
		int x = (int) Math.floor(value);
		
		if(x<1){
			prob=0.0;
		}
		else{
			if(x<n){
				prob=(double)x/(double)n;
			}
			else{
				prob=1.0;
			}
		}
		
		return prob;
		
	}//end-method
	
	/**
	 * To String method.
	 */
	public String toString(){
		
		String text="";
		
		text+="Uniform distribution. Parameters N:"+n;
		
		return text;
		
	}//end-method
	
}//end-class
