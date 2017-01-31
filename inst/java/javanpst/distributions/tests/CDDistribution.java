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

import javanpst.distributions.common.continuous.NormalDistribution;
import javanpst.utils.Operations;

/**
 * An implementation of the Charkraborti and Desu test test distribution.
 * 
 * Uses a Singleton pattern to ensure only an instance of the class is allowed.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class CDDistribution extends TestDistribution{
	
	/**
	 * Normal distribution
	 */
	private NormalDistribution normal;
	
	/**
	 * Private default builder.
	 * 
	 * Initializes distribution tables
	 */
	private CDDistribution(){
		
		initialize();
		
	}//end-method
	
	/**
	 * Singleton pattern access to the distribution
	 * 
	 * @return the instance of the distribution
	 */
	public static CDDistribution getInstance(){
		
		return SingletonHolder.distribution;
		
	}//end-method
	
	/**
	 * Singleton holder for the only instance of the distribution
	 * 
	 * SingletonHolder is loaded on the first execution of getInstance() 
	 * or the first access to SingletonHolder.distribution, not before.
	 */
	private static class SingletonHolder { 
		   
		public static final CDDistribution distribution = new CDDistribution();
		
	}//end-method
	
	/**
	 * Generate distribution tables
	 */
	protected void generateTables(){
		
		normal=new NormalDistribution();
			
	}//end-method
	
	/**
	 * Load data files to fill distribution tables
	 */
	protected void loadFiles(){
		
		//no files to load here
		
	}//end-method
	
	/**
	 * Compute exact p-value of the CD distribution
	 * 
	 * @param W W statistic
	 * @param N number of elements
	 * @param populations number of populations
	 * @param length number values per sample
	 * @return p-value computed
	 */
	public double computeExactProbability(double W, int N, int populations, int length){
		
		double pValue;
		
		pValue=(double)length/(double)N;
		
		pValue*=Operations.combinatorial(N-length,(int)W)*Operations.combinatorial(length-1,populations-1);
		pValue/=(double)(Operations.combinatorial(N-1,(int)W+populations-1));
		
		return pValue;
		
	}//end-method

	/**
	 * Compute asymptotic p-value, following a normal distribution
	 * 
	 * @param W W statistic
	 * @param N number of elements
	 * @param populations number of populations
	 * @param length number values per sample
	 * @return p-value computed
	 */
	public double computeAsymptoticProbability(double W, int N, int populations, int length){
		
		double stMean,stDev,Z;
		
		//asymptotic p-value
		stMean=(N-length)*(populations/(double)(length+1.0));
		
		stDev=populations*(length-populations+1.0)*(N+1.0)*(N-length);
		stDev/=(double)((length+1.0)*(length+1.0)*(length+2.0));
	
		stDev=Math.sqrt(stDev);
		
		Z=(W-stMean+0.5)/stDev;
		
		return normal.getTipifiedProbability(Z, false);
		
	}//end-method
	
}//end-class

