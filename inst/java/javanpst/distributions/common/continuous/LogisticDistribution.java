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
 * A Logistic distribution.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class LogisticDistribution implements Distribution{

	/**
	 * Mean parameter
	 */
	private double mean;
	
	/**
	 * S parameter
	 */
	private double s;
	
	/**
	 * Default builder.
	 * 
	 * Creates a Logistic distribution with parameters mean = 0 and s = 1
	 */
	public LogisticDistribution(){

		mean=0.0;
		s=1.0;
		
	}//end-method
	
	/**
	 * Builder.
	 * 
	 * Creates a Logistic distribution with the specified parameters
	 * 
	 * S must be positive
	 * 
	 * @param mean mean of the distribution
	 * @param s scale of the distribution
	 */
	public LogisticDistribution(double mean, double s){
		
		if(s>0){	
			this.mean=mean;
			this.s=s;
		}
		
	}//end-method
	
	/**
	 * Copy constructor
	 * 
	 * Creates a copy of a given Logistic distribution
	 * 
	 * @param old Logistic distribution to copy
	 */
	public LogisticDistribution(LogisticDistribution old){
		
		this(old.getMean(),old.getS());
		
	}//end-method
	
	/**
	 * Sets mean
	 * 
	 * @param value mean value
	 */
	public void setMean(double value){
		
		mean=value;
		
	}//end-method
	
	
	/**
	 * Sets S
	 * 
	 * S value must be positive
	 * 
	 * @param value S value
	 */
	public void setS(double value){
		
		if(value>0.0){
			s=value;
		}
		
	}//end-method
	
	/**
	 * Gets the mean parameter
	 * 
	 * @return value of mean
	 */
	public double getMean(){
		
		return mean;
		
	}//end-method
	
	/**
	 * Gets the s parameter
	 * 
	 * @return value of s
	 */
	public double getS(){
		
		return s;
		
	}//end-method
	
	/**
	 * Computes mass probability at a given point of the Logistic distribution.
	 * 
	 * @param x point selected
	 * @return mass probability at 'x'
	 */ 
	public double computeProbability(double x) {
        
		double value;
		double aux;
    	
		value=Math.pow(Math.E,-(x-mean)/s);
		
		aux=1.0+Math.pow(Math.E,-(x-mean)/s);
		
		value/=(s*aux*aux);
		
	    return value;
	    
	}//end-method	
	
	/**
	 * Computes cumulative probability at a given point of the Logistic distribution.
	 * 
	 * @param x point selected
	 * @return cumulative probability at 'x'
	 */
	public double computeCumulativeProbability(double x) {
	        
		double value;
	    	
		value=1.0/(1.0+Math.pow(Math.E,-(x-mean)/s));
		
	    return value;
	    
	}//end-method	
	
	/**
	 * To String method.
	 * 
	 * @return string representation of the distribution
	 */
	public String toString(){
		
		String text="";
		
		text+="Logistic distribution. Mean: "+mean+" S: "+s;
		
		return text;
		
	}//end-method
	    
}//end-class

