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

package javanpst.distributions.tests;

import javanpst.distributions.common.continuous.ChiSquareDistribution;
import javanpst.utils.Operations;

/**
 * An implementation of the Fisher's test distribution.
 * 
 * Uses a Singleton pattern to ensure only an instance of the class is allowed.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class FisherDistribution extends TestDistribution{
	
	/**
	 * Chi distribution
	 */
	private ChiSquareDistribution chi;
	
	/**
	 * Private default builder.
	 * 
	 * Initializes distribution tables
	 */
	private FisherDistribution(){
		
		initialize();
		
	}//end-method
	
	/**
	 * Singleton pattern access to the distribution
	 * 
	 * @return the instance of the distribution
	 */
	public static FisherDistribution getInstance(){
		
		return SingletonHolder.distribution;
		
	}//end-method
	
	/**
	 * Singleton holder for the only instance of the distribution
	 * 
	 * SingletonHolder is loaded on the first execution of getInstance() 
	 * or the first access to SingletonHolder.distribution, not before.
	 */
	private static class SingletonHolder { 
		   
		public static final FisherDistribution distribution = new FisherDistribution();
		
	}//end-method
	
	/**
	 * Generate distribution tables
	 */
	protected void generateTables(){
		
		chi=new ChiSquareDistribution();
		
		
	}//end-method
	
	/**
	 * Load data files to fill distribution tables
	 */
	protected void loadFiles(){
		
		//no files to load here
		
	}//end-method
	
	/**
	 * Computes exact left tail of Fisher distribution.
	 * 
	 * @param N number of samples
	 * @param n1 sum of the samples of the first row
	 * @param n2 sum of the samples of the second row
	 * @param Y sum of the samples of the first column
	 * @param n00 samples in first column, first row
	 * @return p-value computed
	 */
	public double computeLeftExactProbability(int N,int n1, int n2, int Y,int n00){
		
		double pValue;
		
		//sample+extreme
		double numerator=0.0;
		
		for(int i=n00;i<=n1;i++){
			
			numerator+=Operations.combinatorial((int)n1,i)*Operations.combinatorial(n2,(Y-i));
			
		}
	
		double denominator=Operations.combinatorial((int)N, (int)Y);

		pValue=numerator/denominator;
		
		return pValue;
		
	}//end-method
	
	/**
	 * Computes exact right tail of Fisher distribution.
	 * 
	 * @param N number of samples
	 * @param n1 sum of the samples of the first row
	 * @param n2 sum of the samples of the second row
	 * @param Y sum of the samples of the first column
	 * @param n00 samples in first column, first row
	 * @return p-value computed
	 */
	public double computeRightExactProbability(int N,int n1, int n2, int Y,int n00){
		
		double pValue;
		
		//sample+extreme
		double numerator=0.0;
		
		for(int i=n00;i>=0;i--){
			
			numerator+=Operations.combinatorial((int)n1,i)*Operations.combinatorial(n2,(Y-i));
			
		}
	
		double denominator=Operations.combinatorial((int)N, (int)Y);

		pValue=numerator/denominator;
		
		return pValue;

	}//end-method

	/**
	 * Compute asymptotic p-value, following a chi-square distribution
	 * 
	 * @param Q Fisher statistic
	 * @param freedom degrees of freedom
	 * @return p-value computed
	 */
	public double computeAsymptoticProbability(double Q,int freedom){
		
		chi.setDegree(freedom);
		
		return chi.computeCumulativeProbability(Q);
		
	}//end-method
	
}//end-class

