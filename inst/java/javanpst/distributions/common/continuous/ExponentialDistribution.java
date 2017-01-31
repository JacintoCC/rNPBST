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

package javanpst.distributions.common.continuous;

import javanpst.distributions.common.Distribution;

/**
 * A exponential distribution.
 * 
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */

public final class ExponentialDistribution implements Distribution{
	
	/**
	 * Lambda parameter
	 */
	private double lambda;
	
	/**
	 * Default builder.
	 * 
	 * Creates a exponential distribution with lambda = 1 
	 */
	public ExponentialDistribution(){

		lambda= 1.0;
		
	}//end-method
	
	/**
	 * Builder.
	 * 
	 * Creates a exponential distribution with specified lambda
	 * 
	 * @param lambda lambda parameer of the distribution
	 */
	public ExponentialDistribution(double lambda){
		
		this.lambda=lambda;
		
	}//end-method
	
	/**
	 * Copy constructor
	 * 
	 * Creates a copy of a given exponential distribution
	 * 
	 * @param old exponential distribution to copy
	 */
	public ExponentialDistribution(ExponentialDistribution old){
		
		this(old.getLambda());
		
	}//end-method
	
	/**
	 * Sets lambda parameter
	 * 
	 * @param value value for the lambda parameter
	 */
	public void setLambda(double value){
		
		lambda=value;
		
	}//end-method
	
	/**
	 * Sets mean of the distribution (mean = 1/lambda)
	 * 
	 * @param value value for the mean
	 */
	public void setMean(double value){
		
		lambda=1.0/value;
		
	}//end-method
	
	/**
	 * Gets mean of the distribution (mean = 1/lambda)
	 * 
	 * @return mean of the distribution 
	 */
	public double getMean(){
		
		return 1.0/lambda;
		
	}//end-method
	
	/**
	 * Gets lambda parameter
	 * 
	 * @return lambda value
	 */
	public double getLambda(){
		
		return lambda;
		
	}//end-method
	
	/**
	 * Computes mass probability at a given point of the exponential distribution.
	 * 
	 * @param x point selected
	 * @return mass probability at 'x'
	 */
	public double computeProbability(double x) {
	        
		double value;
    	
		if(x>=0.0){
			value = lambda*Math.pow(Math.E,-lambda*x);
		}
		else{
			value=0.0;
		}
		
	    return value;
	    
	}//end-method
	
	/**
	 * Computes cumulative probability at a given point of the exponential distribution.
	 * 
	 * @param x point selected
	 * @return cumulative probability at 'x'
	 */
	public double computeCumulativeProbability(double x) {
	        
		double value;
	    	
		if(x>=0.0){
			value = 1.0- Math.pow(Math.E,-lambda*x);
		}
		else{
			value=0.0;
		}
		
	    return value;
	    
	}//end-method
	  
    /**
	 * To String method.
	 * 
	 * @return string representation of the distribution
	 */
	public String toString(){
		
		String text="";
		
		text+="Exponential distribution. Parameters Lambda: "+lambda;
		
		return text;
		
	}//end-method
		
}//end-class
