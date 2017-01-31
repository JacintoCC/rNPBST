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
 * A chi-square distribution.
 * 
 * Uses a inner gamma distribution.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public final class ChiSquareDistribution implements Distribution{

	/**
	 * Inner Gamma distribution
	 */
	private GammaDistribution gamma;
	
	/**
	 * Degrees of freedom
	 */
	private int freedom;
	
	/**
	 * Default builder.
	 * 
	 * Creates a chi-square distribution with 1 degree of freedom
	 */
	public ChiSquareDistribution(){

		gamma=new GammaDistribution();
		gamma.setAlpha((double)freedom/2.0);
		gamma.setBeta(2.0);
		
		freedom = 1;
		
	}//end-method
	
	/**
	 * Builder.
	 * 
	 * Creates a chi-square distribution with specified degrees of freedom
	 * 
	 * @param degree degrees of freedom
	 */
	public ChiSquareDistribution(int degree){
		
		gamma=new GammaDistribution();
		gamma.setAlpha((double)degree/2.0);
		gamma.setBeta(2.0);
		
		freedom = degree;
		
	}//end-method
	
	/**
	 * Copy constructor
	 * 
	 * Creates a copy of a given chi-square distribution
	 * 
	 * @param old chi-square distribution to copy
	 */
	public ChiSquareDistribution(ChiSquareDistribution old){
		
		this(old.getDegree());
		
	}//end-method
	
	/**
	 * Gets the number of degrees of freedom of the distribution
	 * 
	 * @return number of degrees of freedom
	 */
	public int getDegree(){
		
		return freedom;
		
	}//end-method
	
	/**
	 * Sets the number of degrees of freedom of the distribution
	 * 
	 * @param value number of degrees of freedom (positive)
	 */
	public void setDegree(int value){
		
		if(value>0){
			freedom=value;
			gamma.setAlpha((double)freedom/2.0);
			gamma.setBeta(2.0);
		}
		
	}//end-method
	
	/**
	 * Computes mass probability at a given point of the chi-square distribution.
	 * 
	 * @param value point selected
	 * @return mass probability at 'value'
	 */
	public double computeProbability(double value){
		
		return gamma.computeProbability(value);
		
	}//end-method
	
	/**
	 * Computes cumulative probability at a given point of the chi-square distribution.
	 * 
	 * 
	 * @param value point selected
	 * @return cumulative probability at 'value'
	 */
	public double computeCumulativeProbability(double value){
		
		return 1.0-computeRightTailProbability(value);
		
	}//end-method
	
	
   /**
	* Computes the right tail of chi-square distribution
	*
	* @param value point selected
	* 
	* @return The Chi Square right tail value
	*/
	public double computeRightTailProbability(double value) {
		
		double q,p,k,t,a;
		
		double EPSILON = 0.0000000001;
		
        if ((freedom == 1) & (value > 1000.0)) {
            return 0;
        }
        
        if ((value > 1000) || (freedom > 1000)) {
            q = computeRightTailProbability((value - freedom) * (value - freedom) / (2 * freedom)) / 2;
            if (value > freedom) {
                return q;
            }
            
            return 1 - q;
            
        }
        
        p = Math.exp( -0.5 * value);
        
        if ((freedom % 2) == 1) {
            p = p * Math.sqrt(2 * value / Math.PI);
        }
        
        k = freedom;
        
        while (k >= 2) {
            p = p * value / k;
            k = k - 2;
        }
        
        t = p;
        a = freedom;
        
        while (t > EPSILON * p) {
            a = a + 2;
            t = t * value / a;
            p = p + t;
        }
        
        return p;
        
    }//end-method
	
	/**
	 * To String method.
	 * 
	 * @return string representation of the distribution
	 */
	public String toString(){
		
		String text="";
		
		text+="Chi-square distribution. Degrees of freedom: "+freedom;
		
		return text;
		
	}//end-method
	
}//end-class
	