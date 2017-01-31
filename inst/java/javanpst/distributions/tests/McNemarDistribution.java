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
import javanpst.distributions.common.continuous.NormalDistribution;
import javanpst.distributions.common.discrete.BinomialDistribution;

/**
 * An implementation of the McNemar's test distribution.
 * 
 * Uses a Singleton pattern to ensure only an instance of the class is allowed.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class McNemarDistribution extends TestDistribution{
	
	/**
	 * Binomial distribution
	 */
	private BinomialDistribution binom;
	
	/**
	 * Normal distribution
	 */
	private NormalDistribution normal;
	
	/**
	 * Chi distribution
	 */
	private ChiSquareDistribution chi;
	
	/**
	 * Private default builder.
	 * 
	 * Initializes distribution tables
	 */
	private McNemarDistribution(){
		
		initialize();
		
	}//end-method
	
	/**
	 * Singleton pattern access to the distribution
	 * 
	 * @return the instance of the distribution
	 */
	public static McNemarDistribution getInstance(){
		
		return SingletonHolder.distribution;
		
	}//end-method
	
	/**
	 * Singleton holder for the only instance of the distribution
	 * 
	 * SingletonHolder is loaded on the first execution of getInstance() 
	 * or the first access to SingletonHolder.distribution, not before.
	 */
	private static class SingletonHolder { 
		   
		public static final McNemarDistribution distribution = new McNemarDistribution();
		
	}//end-method
	
	/**
	 * Generate distribution tables
	 */
	protected void generateTables(){
		
		binom=new BinomialDistribution();
		binom.setP(0.5);
		
		normal =new NormalDistribution();
		
		chi=new ChiSquareDistribution();
		chi.setDegree(1);
		
	}//end-method
	
	/**
	 * Load data files to fill distribution tables
	 */
	protected void loadFiles(){
		
		//no files to load here
		
	}//end-method
	
	/**
	 * Compute adjustment to binomial distribution
	 * 
	 * @param S test statistic
	 * @return p-value computed
	 */
	public double computeBinomialAdjustment(double S,double x){
		
		binom.setN((int)S);
		
		return binom.computeCumulativeProbability((int)x);
		
	}//end-method
	
	/**
	 * Compute adjustment to normal distribution
	 * 
	 * @param Z test statistic
	 * @return p-value computed
	 */
	public double computeNormalAdjustment(double Z){

		return normal.getTipifiedProbability(Z, false);
		
	}//end-method
	
	/**
	 * Compute adjustment to chi-square distribution
	 * 
	 * @param T test statistic
	 * @return p-value computed
	 */
	public double computeChiAdjustment(double T){
		
		return chi.computeCumulativeProbability(T);
		
	}//end-method
	
}//end-class

