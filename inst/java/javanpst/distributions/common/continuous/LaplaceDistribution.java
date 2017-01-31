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
 * A Laplace distribution.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class LaplaceDistribution implements Distribution{

	/**
	 * Mean parameter
	 */
	private double mean;
	
	/**
	 * Scale parameter
	 */
	private double scale;
	
	/**
	 * Default builder.
	 * 
	 * Creates a Laplace distribution with mean 0.0 and scale 1.0
	 */
	public LaplaceDistribution(){

		mean=0.0;
		scale=1.0;
		
	}//end-method
	
	/**
	 * Builder.
	 * 
	 * Creates a Laplace distribution with specified mean and scale
	 * 
	 * Scale must be positive
	 * 
	 * @param mean mean of the distribution
	 * @param scale scale of the distribution
	 */
	public LaplaceDistribution(double mean, double scale){
		
		if(scale>0.0){
			this.mean=mean;
			this.scale=scale;
		}
		
	}//end-method
	
	/**
	 * Copy constructor
	 * 
	 * Creates a copy of a given Laplace distribution
	 * 
	 * @param old Laplace distribution to copy
	 */
	public LaplaceDistribution(LaplaceDistribution old){
		
		this(old.getMean(),old.getScale());
		
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
	 * Sets scale
	 * 
	 * Scale value must be positive
	 * 
	 * @param value scale value
	 */
	public void setScale(double value){
		
		if(value>0.0){
			scale=value;
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
	 * Gets the scale parameter
	 * 
	 * @return value of scale
	 */
	public double getScale(){
		
		return scale;
		
	}//end-method
	
	/**
	 * Computes mass probability at a given point of the Laplace distribution.
	 * 
	 * @param x point selected
	 * @return mass probability at 'x'
	 */   
	public double computeProbability(double x) {
        
		double value;
    	
		value=Math.pow(Math.E, -Math.abs(x-mean)/scale);
			
		value/=(2.0*scale);
		
	    return value;
	    
	}//end-method
	
	/**
	 * Computes cumulative probability at a given point of the Laplace distribution.
	 * 
	 * @param x point selected
	 * @return cumulative probability at 'x'
	 */
	public double computeCumulativeProbability(double x) {
	        
		double prob;
		
        if (x < mean) {
            prob = 0.5 * Math.exp((x - mean) / scale);
        } else {
            prob = 0.5 * (2.0 - Math.exp((mean - x) / scale));
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
		
		text+="Laplace distribution. Mean: "+mean+" Scale: "+scale;
		
		return text;
		
	}//end-method
	    
}//end-class

