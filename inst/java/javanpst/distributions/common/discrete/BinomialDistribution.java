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
 * A binomial distribution, with parameters N and P
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */

public final class BinomialDistribution implements Distribution{
	
	/**
	 * N parameter of the distribution
	 */
	private int n;
	
	/**
	 * P parameter of the distribution
	 */
	private double p;
	
	/**
	 * Default builder.
	 * 
	 * Creates a binomial distribution with parameters N=1, P=0.0
	 */
	public BinomialDistribution(){
		
		n=1;
		p=0.0;
		
	}//end-method
	
	/**
	 * Builder.
	 * 
	 * Creates a binomial distribution with parameters N and P
	 * 
	 * @param n N parameter
	 * @param p P parameter
	 */
	public BinomialDistribution(int n, double p){
		
		this.n=n;
		this.p=p;
		
	}//end-method
	
	/**
	 * Copy constructor
	 * 
	 * Creates a copy of a given binomial distribution
	 * 
	 * @param old Binomial distribution to copy
	 */
	public BinomialDistribution(BinomialDistribution old){
		
		this(old.getN(),old.getP());
		
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
	 * Sets the N parameter.
	 * 
	 * Only works if N is greater than 0
	 * 
	 * @param value N parameter value
	 */
	public void setN(int value){
		
		if(value>0){
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
	 * Computes mass probability at a given point of the binomial distribution.
	 * 
	 * Value given is converted to integer before computing probability.
	 * 
	 * @param value point selected
	 * @return mass probability at 'value'
	 */
	public double computeProbability(double value){
		
		double prob;
		int x = (int) Math.floor(value);
		
		if(x<0){
			prob=0.0;
		}
		else{
			if(x>n){
				prob=0.0;
			}
			else{
				prob= Operations.combinatorial(n, x)*Math.pow(p, x)*Math.pow(1-p, n-x);
			}
		}
		
		return prob;
		
	}//end-method
	
	/**
	 * Computes cumulative probability at a given point of the distribution.
	 * 
	 * Value given is converted to integer before computing probability.
	 * 
	 * @param value point selected
	 * @return cumulative probability at 'value'
	 */
	public double computeCumulativeProbability(double value){
		
		double prob=0.0;
		int x = (int) Math.floor(value);
		
		if(x>=0){
			for(int i=0;i<=x && prob<1.0;i++){
				prob+=computeProbability(i);
			}
		}
		
		return prob;
		
	}//end-method
	
	/**
	 * Computes the greatest integer whose cumulative probability is lower
	 * than a given value
	 * 
	 * @param limit Maximum probability allowed
	 * @return greatest integer found. -1 if there is not any.
	 */
	public int getLesserCumulativeProbability(double limit){
		
		double prob=0.0;
		
		if(computeProbability(0)>limit){
			return -1;
		}
		
		for(int i=0;i<n;i++){
			prob+=computeProbability(i);
			if(prob>limit){
				return i-1;
			}
		}
		
		return n;
		
	}//end-method
	
	/**
	 * Computes the lowest integer whose cumulative probability is greater
	 * than a given value
	 * 
	 * @param limit Minimum probability allowed
	 * @return lowest integer found. -1 if there is not any.
	 */
	public int getUpperCumulativeProbability(double limit){
		
		double prob=1.0;
		
		for(int i=n;i>-1;i--){
			prob-=computeProbability(i);
			if(prob<limit){
				return i;
			}
		}
		
		return n;
		
	}//end-method
	
    /**
	 * To String method.
	 * 
	 * @return string representation of the distribution
	 */
	public String toString(){
		
		String text="";
		
		text+="Binomial distribution. Parameters N:"+n+" P:"+p;
		
		return text;
		
	}//end-method
	
}//end-class
