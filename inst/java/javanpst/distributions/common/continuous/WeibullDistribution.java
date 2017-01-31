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
 * A Weibull distribution.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class WeibullDistribution implements Distribution{

	/**
	 * Lambda parameter
	 */
	private double lambda;
	
	/**
	 * K parameter
	 */
	private double k;
	
	/**
	 * Default builder.
	 * 
	 * Creates a Weibull distribution with parameters lambda = 1 and k = 1
	 */
	public WeibullDistribution(){

		lambda=1.0;
		k=1.0;
		
	}//end-method
	
	/**
	 * Builder.
	 * 
	 * Creates a Weibull distribution with specified lambda and k parameters
	 * 
	 * Lambda and k must be positive
	 * 
	 * @param lambda lambda parameter of the distribution
	 * @param k k parameter of the distribution
	 */
	public WeibullDistribution(double lambda, double k){
		
		if((k>0.0)&&(lambda>0.0)){
			this.lambda=lambda;
			this.k=k;
		}
		
	}//end-method
	
	/**
	 * Copy constructor
	 * 
	 * Creates a copy of a given Laplace distribution
	 * 
	 * @param old Laplace distribution to copy
	 */
	public WeibullDistribution(WeibullDistribution old){
		
		this(old.getLambda(),old.getK());
		
	}//end-method
	
	/**
	 * Sets lambda
	 * 
	 * Lambda value must be positive
	 * 
	 * @param value lambda value
	 */
	public void setLambda(double value){
		
		if(value>0.0){
			lambda=value;
		}
		
	}//end-method
	
	/**
	 * Sets k
	 * 
	 * K value must be positive
	 * 
	 * @param value k value
	 */
	public void setK(double value){
		
		if(value>0.0){
			k=value;
		}
		
	}//end-method
	
	/**
	 * Gets the lambda parameter
	 * 
	 * @return value of lambda
	 */
	public double getLambda(){
		
		return lambda;
		
	}//end-method
	
	/**
	 * Gets the k parameter
	 * 
	 * @return value of k
	 */
	public double getK(){
		
		return k;
		
	}//end-method
	
	/**
	 * Computes mass probability at a given point of the Weibull distribution.
	 * 
	 * @param x point selected
	 * @return mass probability at 'x'
	 */  
	public double computeProbability(double x) {
	        
		double value;
    	
		if(x>=0.0){
			value = (k/lambda)*Math.pow(x/lambda, k-1)*Math.pow(Math.E, -1.0*Math.pow(x/lambda, k));
		}
		else{
			value=0.0;
		}
		
	    return value;
	    
	}//end-method
	
	/**
	 * Computes cumulative probability at a given point of the Weibull distribution.
	 * 
	 * @param x point selected
	 * @return cumulative probability at 'x'
	 */
	public double computeCumulativeProbability(double x) {
	        
		double value;
	    	
		if(x>=0.0){
			value = 1.0- Math.pow(Math.E,-1.0*Math.pow(x/lambda, k));
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
		
		text+="Weibull distribution. Lambda: "+lambda+" K: "+k;
		
		return text;
		
	}//end-method
	
}//end-class
