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
 * A geometric distribution, with parameter P
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */

public final class GeometricDistribution implements Distribution{
	
	/**
	 * P parameter of the distribution
	 */
	private double p;
	
	
	/**
	 * Default builder.
	 * 
	 * Creates a geometric distribution with parameter P=0.0
	 */
	public GeometricDistribution(){

		p=0.0;
		
	}//end-method
	
	/**
	 * Builder.
	 * 
	 * Creates a geometric distribution with parameter P
	 * 
	 * @param p P parameter
	 */
	public GeometricDistribution(double p){
		
		this.p=p;
		
	}//end-method
	
	/**
	 * Copy constructor
	 * 
	 * Creates a copy of a given geometric distribution
	 * 
	 * @param old geometric distribution to copy
	 */
	public GeometricDistribution(GeometricDistribution old){
		
		this(old.getP());
		
	}//end-method

	/**
	 * Sets the P parameter.
	 * 
	 * Only works if P is in [0.0,1.0]
	 * 
	 * @param value P parameter value
	 */
	public void setP(double value){
		
		if((value>=0.0)&&(value<=1.0)){
			p=value;
		}
		
	}//end-method
	
	/**
	 * Gets the P parameter value
	 * 
	 * @return value of P
	 */
	public double getP(){
		
		return p;
		
	}//end-method
	
	/**
	 * Computes mass probability at a given point of the geometric distribution.
	 * 
	 * Value given is converted to integer before computing probability.
	 * 
	 * @param value point selected
	 * @return mass probability at 'value'
	 */
	public double computeProbability(double value){
		
		double prob;
		int x = (int) Math.floor(value);
		
		if(x<1){
			prob=0.0;
		}
		else{
			prob=Math.pow(1.0-p, (double)x-1.0)*p;
		}
		
		return prob;
		
	}//end-method
	
	/**
	 * Computes cumulative probability at a given point of the geometric distribution.
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
			prob=1.0-Math.pow(p,x+1.0);
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
		
		text+="Geometric distribution. Parameters P:"+p;
		
		return text;
		
	}//end-method
	
}//end-class

