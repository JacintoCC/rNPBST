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
import javanpst.utils.ContinuedFraction;

/**
 * A Gamma distribution.
 *  
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public final class GammaDistribution implements Distribution{
	
	/**
	 * Alpha parameter (K)
	 */
	private double alpha;
	
	/** 
	 * Beta parameter (1/lambda)
	 */
	private double beta;
	
	
	/**
	 * Default builder.
	 * 
	 * Creates a chi-square distribution with 1 degree of freedom
	 */
	 public GammaDistribution(){
		 
		 alpha=1.0;
		 beta=1.0;
		 
	 }//end-method
	
	/**
	 * Builder.
	 * 
	 * Creates a gamma distribution with specified values of alpha and beta
	 * 
	 * @param alpha
	 * @param beta
	 * 
	 */
	public GammaDistribution(double alpha,double beta){
		
		this.alpha=alpha;
		this.beta=beta;
		
	}//end-method
	
	/**
	 * Copy constructor
	 * 
	 * Creates a copy of a given gamma distribution
	 * 
	 * @param old uniform distribution to copy
	 */
	public GammaDistribution(GammaDistribution old){
		
		this(old.getAlpha(),old.getBeta());
		
	}//end-method
	
	/**
	 * Sets alpha value
	 * 
	 * Value must be positive
	 * 
	 * @param value alpha value
	 */
	public void setAlpha(double value){
		
		if(value > 0.0){
			alpha=value;
		}
		
	}//end-method
	
	/**
	 * Sets K value (an alias of alpha)
	 * 
	 * Value must be positive
	 * 
	 * @param value alpha value
	 */
	public void setK(double value){
		
		this.setAlpha(value);
		
	}//end-method
	
	/**
	 * Sets beta value
	 * 
	 * Value must be positive
	 * 
	 * @param value alpha value
	 */
	public void setBeta(double value){
		
		if(value > 0.0){
			beta=value;	
		}
		
	}//end-method
	
	/**
	 * Sets lambda value (an alias of (1/beta))
	 * 
	 * Value must be positive
	 * 
	 * @param value alpha value
	 */
	public void setLambda(double value){
		
		this.setBeta(1.0/value);
		
	}//end-method
	
	/**
	 * Gets the alpha parameter
	 * 
	 * @return value of alpha
	 */
	public double getAlpha(){
		
		return alpha;
		
	}//end-method
	
	/**
	 * Gets the K parameter (an alias of alpha)
	 * 
	 * @return value of K
	 */
	public double getK(){
		
		return alpha;
		
	}//end-method
	
	/**
	 * Gets the beta parameter
	 * 
	 * @return value of beta
	 */
	public double getBeta(){
		
		return beta;
		
	}//end-method
	
	/**
	 * Gets the lambda parameter (an alias of 1/beta)
	 * 
	 * @return value of lambda
	 */
	public double getLambda(){
		
		return (1.0/beta);
		
	}//end-method
	
	/**
	 * Computes mass probability at a given point of the gamma distribution.
	 * 
	 * @param value point selected
	 * @return mass probability at 'value'
	 */   
	public double computeProbability(double value){
		 
		double prob;
		 
		prob=Math.pow(value,alpha-1)*Math.pow(Math.E, -value/beta);
		 
		prob/=(Math.pow(beta, alpha)*Math.exp(logGamma(value)));
		 
		return prob;
		
	}//end-method
	
	/**
	 * Computes natural logarithm of gamma function.
	 * 
	 * Uses Lanczos coefficients
	 * 
	 * @param value value to compute
	 * @return natural logarithm of gamma function
	 */
	private double logGamma(double value) {
        
		double prob;
		
		double tmp, sum;
		
		// Lanczos coefficients.
        double[] coefficients = { 0.99999999999999709182, 57.156235665862923517, -59.597960355475491248, 
    		   				14.136097974741747174, -0.49191381609762019978,	0.33994649984811888699e-4,
    		   				0.46523628927048575665e-4, -0.98374475304879564677e-4, 0.15808870322491248884e-3, 
    		   				-0.21026444172410488319e-3,	0.21743961811521264320e-3, -0.16431810653676389022e-3,
    		   				0.84418223983852743293e-4, -0.26190838401581408670e-4, 0.36899182659531622704e-5, 
    		   			};
        
        if (Double.isNaN(value) || (value <= 0.0)) {
            prob = Double.NaN;
        } 
        else {

            sum = 0.0;
            
            for (int i = 1; i < coefficients.length; ++i) {
            	sum += (coefficients[i] / (value + i));
            }
            sum += coefficients[0];

            tmp = value + (607.0 / 128.0) + 0.5;
            
            prob = ((value + 0.5) * Math.log(tmp)) - tmp + (0.5 * Math.log(Math.PI + Math.PI))+ Math.log(sum) - Math.log(value);
        }

        return prob;
        
    }//end-method
	
	/**
	 * Computes cumulative probability at a given point of the gamma distribution.
	 * 
	 * @param value point selected
	 * @return cumulative probability at 'value'
	 */
    public double computeCumulativeProbability(double value){
	        
    	double prob;

	    if (value <= 0.0) {
	    	prob = 0.0;
	    } else if (Double.isInfinite(value)) {
	    	prob = 1.0;
	    } else {
	    	prob = regularizedGammaP(alpha, value / beta);
	    }

	    return prob;
	    
	}//end-method
   
    /**
     * Computes regularized gamma P function
     * 
     * @param a a parameter
     * @param x x value
     * @return value of the regularized gamma P function
     */
    public double regularizedGammaP(double a, double x){
    
    	double prob;
    	double EPSILON = 10e-9;
    	int ITERATIONS = 1000;

    	if (x == 0.0) {
    		prob = 0.0;
    	} else if (x >= (a + 1.0)) {
    		prob = 1.0 - regularizedGammaQ(a, x);
    	} else {
            double n = 0.0; 
            double an = 1.0 / a; 
            double sum = an; 
            while (Math.abs(an) > EPSILON && n < ITERATIONS) {

                n += 1.0;
                an *= (x / (a + n));
                sum += an;
            }
            prob = Math.exp(-x + (a * Math.log(x)) - logGamma(a)) * sum;
        }

    	return prob;
    	
    }//end-method
    
    /**
     * Computes regularized gamma Q function
     * 
     * @param a a parameter
     * @param x x value
     * @return value of the regularized gamma Q function
     */
    public double regularizedGammaQ(double a, double x){
        
        double prob;

        if (x == 0.0) {
        	prob = 1.0;
        } else if (x < (a + 1.0)) {
        	prob = 1.0 - regularizedGammaP(a, x);
        } else {
            // create continued fraction
            ContinuedFraction cf = new ContinuedFraction(a);

            prob = 1.0 / cf.evaluate(x);
            prob = Math.exp(-x + (a * Math.log(x)) - logGamma(a)) * prob;
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
		
		text+="Gamma distribution. Parameters Alpha: "+alpha+" Beta: "+beta;
		
		return text;
		
	}//end-method
	
}//end-class
