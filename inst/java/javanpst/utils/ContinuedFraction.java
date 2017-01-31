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

package javanpst.utils;

/**
 * A class to evaluate continued fractions for Gamma function.
 *
 * Based on http://kickjava.com/src/org/apache/commons/math/util/ContinuedFraction.java.htm#ixzz1GZzT663e
 *
 * @see "http://mathworld.wolfram.com/ContinuedFraction.html"
 * @see "http://kickjava.com/src/org/apache/commons/math/util/ContinuedFraction.java.htm#ixzz1GZzT663e"
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */

public class ContinuedFraction {

	private static final double EPSILON = 10e-9;
	private static final int ITERATIONS = 1000;
	private double a;

	/**
	 * Builder.
	 *
	 * @param value Value for a parameter
	 */
	public ContinuedFraction(double value) {

		a=value;

	}//end-method

	/**
	 * Computes value A of the fraction for n and x parameters
	 *
	 * @param n n parameter
	 * @param x x parameter
	 * @return value A of the fraction
	 */
	private double getA(int n, double x) {

        return ((2.0 * n) + 1.0) - a + x;

    }//end-method

	/**
	 * Computes value B of the fraction for n and x parameters
	 *
	 * @param n n parameter
	 * @param x x parameter
	 * @return value B of the fraction
	 */
    private double getB(int n, double x) {

        return n * (a - n);

    }//end-method

    /**
     * Evaluates the continued fraction
     * @param x x parameter
     * @return value of the continued fraction
     */
    public double evaluate(double x){

    	double r,a,b,c,p0,p1,p2,q0,q1,q2;
    	int n;
    	double relativeError;

    	p0 = 1.0;
    	p1 = getA(0, x);
    	q0 = 0.0;
    	q1 = 1.0;
    	c = p1 / q1;
    	n = 0;

    	relativeError = Double.MAX_VALUE;

    	while (n < ITERATIONS && relativeError > EPSILON) {
    		n++;

    		a = getA(n, x);
    		b = getB(n, x);
    		p2 = a * p1 + b * p0;
    		q2 = a * q1 + b * q0;

    		if (Double.isInfinite(p2) || Double.isInfinite(q2)) {

    			// need to scale
    			if (a != 0.0) {
    				p2 = p1 + (b / a * p0);
    				q2 = q1 + (b / a * q0);
    			} else if (b != 0) {
    				p2 = (a / b * p1) + p0;
    				q2 = (a / b * q1) + q0;

    			} else {
    				return -1.0;
    			}
    		}

    		r = p2 / q2;

    		relativeError = Math.abs(r / c - 1.0);

    		// next iteration

    		c = p2 / q2;

    		p0 = p1;
    		p1 = p2;
    		q0 = q1;
    		q1 = q2;

    	}

    	return c;

    }//end-method

}//end-class
