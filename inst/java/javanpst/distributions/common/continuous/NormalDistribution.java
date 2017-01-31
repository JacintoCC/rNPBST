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
 * A Normal distribution.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class NormalDistribution implements Distribution{
	
	/**
	 * Mean parameter
	 */
	private double mean;
	
	/**
	 * Sigma parameter
	 */
	private double sigma;
	
	/**
	 * Default builder.
	 * 
	 * Creates a Normal distribution with parameters mean = 0 and sigma = 1
	 */
	public NormalDistribution(){

		mean=0.0;
		sigma=1.0;
		
	}//end-method
	
	/**
	 * Builder.
	 * 
	 * Creates a Normal distribution with the specified parameters
	 * 
	 * Sigma must be positive
	 * 
	 * @param mean mean of the distribution
	 * @param sigma standard deviation
	 */
	public NormalDistribution(double mean, double sigma){
		
		if(sigma>0.0){	
			this.mean=mean;
			this.sigma=sigma;
		}
		
	}//end-method
	
	/**
	 * Copy constructor
	 * 
	 * Creates a copy of a given Normal distribution
	 * 
	 * @param old Normal distribution to copy
	 */
	public NormalDistribution(NormalDistribution old){
		
		this(old.getMean(),old.getSigma());
		
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
	 * Sets sigma
	 * 
	 * Sigma value must be positive
	 * 
	 * @param value sigma value
	 */
	public void setS(double value){
		
		if(value>0.0){
			sigma=value;
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
	 * Gets the sigma parameter
	 * 
	 * @return value of sigma
	 */
	public double getSigma(){
		
		return sigma;
		
	}//end-method


	/**
	 * Computes mass probability at a given point of the Normal distribution.
	 * 
	 * @param x point selected
	 * @return mass probability at 'x'
	 */ 
	public double computeProbability(double x){
		
		double value=0.0;
		
		value=Math.pow(Math.E,-(x-mean)*(x-mean)/(2.0*sigma*sigma));
		
		value/=Math.sqrt(2.0*Math.PI*sigma*sigma);
		
		return value;
		
	}//end-method
	
	/**
	 * Computes cumulative probability at a given point of the Normal distribution.
	 * 
	 * @param x point selected
	 * @return cumulative probability at 'x'
	 */
	public double computeCumulativeProbability(double x){
		
		double value;
		
		double z= ((x-mean)/sigma);

		value=getTipifiedProbability(z, false);
		
		return value;
		
	}//end-method
	
	/**
     * Computes cumulative N(0,1) distribution. 
     * 
     * Based on Algorithm AS66 Applied Statistics (1973) vol 22 no.3
     * 
     * @param z x value
     * @param upper A boolean value, if true the integral is evaluated from z to infinity, from minus infinity to z otherwise
     * @return The value of the cumulative N(0,1) distribution for z
     */  
    public double getTipifiedProbability(double z, boolean upper) {
        //Algorithm AS 66: "The Normal Integral"
        //Applied Statistics
    	double ltone = 7.0,
        utzero = 18.66,
        con = 1.28,
        a1 = 0.398942280444,
        a2 = 0.399903438504,
        a3 = 5.75885480458,
        a4 = 29.8213557808,
        a5 = 2.62433121679,
        a6 = 48.6959930692,
        a7 = 5.92885724438,
        b1 = 0.398942280385,
        b2 = 3.8052e-8,
        b3 = 1.00000615302,
        b4 = 3.98064794e-4,
        b5 = 1.986153813664,
        b6 = 0.151679116635,
        b7 = 5.29330324926,
        b8 = 4.8385912808,
        b9 = 15.1508972451,
        b10 = 0.742380924027,
        b11 = 30.789933034,
        b12 = 3.99019417011;
		
        double y, alnorm;
        
        if (z < 0) {
            upper = !upper;
            z = -z;
        }
        if (z <= ltone || upper && z <= utzero) {
            y = 0.5 * z * z;
            if (z > con) {
                alnorm = b1 * Math.exp( -y) /
                         (z - b2 +
                          b3 /
                          (z + b4 +
                           b5 /
                           (z - b6 +
                            b7 / (z + b8 - b9 / (z + b10 + b11 / (z + b12))))));
            } else {
                alnorm = 0.5 -
                         z *
                         (a1 - a2 * y / (y + a3 - a4 / (y + a5 + a6 / (y + a7))));
            }
        } else {
            alnorm = 0;
        }
        if (!upper) {
            alnorm = 1 - alnorm;
        }
        
        return (alnorm);
        
    }//end-method
    
    /**
     * Computes inverse normal distribution value (N[0,1]).
     * 
     * Error is bounded to 1.15E-09.
     * 
     * Based on http://home.online.no/~pjacklam/notes/invnorm/
     * 
     * @param p probability value
     * @return z value associated to p
     * @see "http://home.online.no/~pjacklam/notes/invnorm/"
     */
    public double inverseNormalDistribution(double p){
    	
    	   double a1 = -3.969683028665376e+01;
    	   double a2 = 2.209460984245205e+02;
    	   double a3 = -2.759285104469687e+02;
    	   double a4 = 1.383577518672690e+02;
    	   double a5 = -3.066479806614716e+01;
    	   double a6 = 2.506628277459239e+00;

    	   double b1 = -5.447609879822406e+01;
    	   double b2 = 1.615858368580409e+02;
    	   double b3 = -1.556989798598866e+02;
    	   double b4 = 6.680131188771972e+01;
    	   double b5 = -1.328068155288572e+01;

    	   double c1 = -7.784894002430293e-03;
    	   double c2 = -3.223964580411365e-01;
    	   double c3 = -2.400758277161838e+00;
    	   double c4 = -2.549732539343734e+00;
    	   double c5 = 4.374664141464968e+00;
    	   double c6 = 2.938163982698783e+00;

    	   double d1 = 7.784695709041462e-03;
    	   double d2 = 3.224671290700398e-01;
    	   double d3 = 2.445134137142996e+00;
    	   double d4 = 3.754408661907416e+00;

    	   double p_low = 0.02425;
    	   double p_high = 1.0 - p_low;
    	   
    	   double q;
    	   double x=0.0;
    	   double r;

    	   if(p<=0){
    		   return Double.NEGATIVE_INFINITY;
    	   }
    	   
    	   if(p>=1){
    		   return Double.POSITIVE_INFINITY;
    	   }
    	   
    	   //Rational approximation for lower region.

    	   if (p < p_low){
    		   q = Math.sqrt(-2.0*Math.log(p));
    		   x = (((((c1*q+c2)*q+c3)*q+c4)*q+c5)*q+c6) /
     	            ((((d1*q+d2)*q+d3)*q+d4)*q+1.0);
    		   
    		   return x;
    	   }
    	     
    	   //Rational approximation for central region.
    	   
    	   if (p <= p_high){
    		   q = p - 0.5;
    		   r = q*q;
    		   x = (((((a1*r+a2)*r+a3)*r+a4)*r+a5)*r+a6)*q /
     	           (((((b1*r+b2)*r+b3)*r+b4)*r+b5)*r+1.0);
    		   
    		   return x;
    	   }
    	  
    	   //Rational approximation for upper region.
    	   
    	   if (p_high < p){
    	      q = Math.sqrt(-2.0*Math.log(1.0-p));
    	      x = -(((((c1*q+c2)*q+c3)*q+c4)*q+c5)*q+c6) /
    	             ((((d1*q+d2)*q+d3)*q+d4)*q+1.0);
    	      
    	      return x;
    	   }

    	   return x;
    	   
    }//end-method
    
    /**
	 * To String method.
	 * 
	 * @return string representation of the distribution
	 */
	public String toString(){
		
		String text="";
		
		text+="Normal distribution. Mean: "+mean+" Sigma: "+sigma;
		
		return text;
		
	}//end-method
	    
}//end-class
