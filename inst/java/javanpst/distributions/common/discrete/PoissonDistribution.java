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

import javanpst.utils.Operations;
import javanpst.distributions.common.Distribution;

/**
 * A poisson distribution, with a specified mean
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */

public final class PoissonDistribution implements Distribution{
	
	/**
	 * Mean of the distribution
	 */
	private double mean;
	
	/**
	 * Default builder.
	 * 
	 * Creates a Poisson distribution with mean = 1.0
	 */
	public PoissonDistribution(){

		mean=1.0;
		
	}//end-method
	
	/**
	 * Builder.
	 * 
	 * Creates a Poisson distribution with a given mean
	 * 
	 * @param mean Mean for the distribution
	 */
	public PoissonDistribution(double mean){
		
		this.mean=mean;
		
	}//end-method
	
	/**
	 * Copy constructor
	 * 
	 * Creates a copy of a given Poisson distribution
	 * 
	 * @param old Poisson distribution to copy
	 */
	public PoissonDistribution(PoissonDistribution old){
		
		this(old.getMean());
		
	}//end-method

	/**
	 * Sets the mean of the distribution.
	 * 
	 * Mean must be positive
	 * 
	 * @param value Mean value
	 */
	public void setMean(double value){
		
		if(value>0.0){
			mean=value;
		}
		
	}//end-method
	
	/**
	 * Gets the mean of the distribution
	 * 
	 * @return Mean value
	 */
	public double getMean(){
		
		return mean;
		
	}//end-method
	
	/**
	 * Computes mass probability at a given point of the Poisson distribution.
	 * 
	 * Value given is converted to integer before computing probability.
	 * 
	 * @param value point selected
	 * @return mass probability at 'value'
	 */
	public double computeProbability(double value){
		
		double prob=0.0;
		int x = (int) Math.floor(value);
		
		if(x<0){
			prob=0.0;
		}
		else{
			prob=(Math.exp(-mean)*Math.pow(mean, (double)x))/(Operations.factorial(x));
		}
		
		return prob;
		
	}//end-method
	
	/**
	 * Computes cumulative probability at a given point of the Poisson distribution.
	 * 
	 * Value given is converted to integer before computing probability.
	 * 
	 * @param value point selected
	 * @return cumulative probability at 'value'
	 */
	public double computeCumulativeProbability(double value){
		
		double prob=0.0;
		int x = (int) Math.floor(value);
		
		for(int i=0;i<=x;i++){
			prob+=computeProbability(x);
		}
		
		return prob;
		
	}//end-method
	
    /**
	 * To String method.
	 * 
	 * @return string representation of the distribution
	 */
	public String toString(){
		
		String text="";
		
		text+="Poisson distribution. Parameters Mean:"+mean;
		
		return text;
		
	}//end-method
	
}//end-class
