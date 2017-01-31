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
 * A Continuous Uniform distribution.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class UniformCDistribution implements Distribution{
  
	/**
	 * Start of the distribution
	 */
	private double start;
	
	/**
	 * End of the distribution
	 */
	private double end;
	
	/**
	 * Default builder.
	 * 
	 * Creates a Continuous Uniform distribution with range [0,1]
	 */
	public UniformCDistribution(){

		start=0.0;
		end=1.0;
		
	}//end-method
	
	/**
	 * Builder.
	 * 
	 * Creates a Continuous Uniform distribution with specified range
	 * 
	 * Start must be lower than end
	 * 
	 * @param start first value of the distribution
	 * @param end last value of the distribution
	 */
	public UniformCDistribution(double start, double end){
		
		if(start<end){
			
			this.start=start;
			this.end=end;
		}
		
	}//end-method
	
	/**
	 * Copy constructor
	 * 
	 * Creates a copy of a given Continuous Uniform distribution
	 * 
	 * @param old Continuous Uniform distribution to copy
	 */
	public UniformCDistribution(UniformCDistribution old){
		
		this(old.getStart(),old.getEnd());
		
	}//end-method
	
	/**
	 * Gets the start parameter
	 * 
	 * @return value of start
	 */
	public double getStart(){
		
		return start;
		
	}//end-method
	
	/**
	 * Gets the end parameter
	 * 
	 * @return value of end
	 */
	public double getEnd(){
		
		return end;
		
	}//end-method
	
	/**
	 * Sets start
	 * 
	 * Start value must be lower than end
	 * 
	 * @param value start value
	 */
	public void setStart(double value){
		
		if(value<end){
			start=value;
		}
		
	}//end-method
	
	/**
	 * Sets end
	 * 
	 * End value must be greter than start
	 * 
	 * @param value end value
	 */
	public void setEnd(double value){
		
		if(value>start){
			end=value;
		}
		
	}//end-method
	
	/**
	 * Computes mass probability at a given point of the Continuous Uniform distribution.
	 * 
	 * @param x point selected
	 * @return mass probability at 'x'
	 */  
	public double computeProbability(double x) {
        
    	double value;
    	
    	if((start<=x)&&(x<=end)){
    		value= 1.0/(end-start);
    	}
    	else{
    		value=0.0;
    	}
   	
    	return value;
    	
    }//end-method

	/**
	 * Computes cumulative probability at a given point of the Continuous Uniform distribution.
	 * 
	 * @param x point selected
	 * @return cumulative probability at 'x'
	 */
    public double computeCumulativeProbability(double x) {
        
    	double value;
    	
    	value= (x-start)/(end-start);
    	value=Math.max(Math.min(value, 1.0), 0.0);
    	
    	return value;
    	
    }//end-method
    
    /**
	 * To String method.
	 * 
	 * @return string representation of the distribution
	 */
	public String toString(){
		
		String text="";
		
		text+="Continuous Uniform distribution. Start: "+start+" End: "+end;
		
		return text;
		
	}//end-method
	
}//end-class
