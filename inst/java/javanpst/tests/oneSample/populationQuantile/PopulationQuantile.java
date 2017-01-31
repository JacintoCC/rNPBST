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

package javanpst.tests.oneSample.populationQuantile;

import javanpst.data.structures.sequence.NumericSequence;
import javanpst.distributions.common.discrete.BinomialDistribution;
import javanpst.tests.StatisticalTest;

/**
 * A procedure to testing hypotheses about population quantiles.
 * 
 * This test allows to test an hypothesis value for a given 
 * population quantile and a given sample. 
 * 
 * A binomial distribution is used to compute left tail p-value 
 * (true value of the quantile is greater than the estimation),
 * right tail p-value (true value is lower than the estimation),
 * and double tail p-value (true value is distinct than the estimation).
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class PopulationQuantile extends StatisticalTest{

	/**
	 * Binomial distribution
	 */
	private BinomialDistribution distribution;
	
	/**
	 * Sample data from the population
	 */
	private NumericSequence sequence;
	
	/**
	 * Quantile estimated
	 */
	private double quantile;
	
	/**
	 * Value to test
	 */
	private double hyphotesisValue;
	
	/**
	 * Test statistic
	 */
	private int K;
	
	/**
	 * Left tail p-value
	 */
	private double exactLeftTail;
	
	/**
	 * Right tail p-value
	 */
	private double exactRightTail;
	
	/**
	 * Double tail p-value
	 */
	private double exactDoubleTail;
	
	/**
	 * Default builder
	 */
	public PopulationQuantile(){
		
		setReportFormat();
		clearData();
		
	}//end-method
	
	/**
	 * Clears the data stored in the test
	 */
	public void clearData(){
		
		K=0;
		quantile=0.0;
		hyphotesisValue=0.0;
		
		exactLeftTail=-1.0;
		exactRightTail=-1.0;
		exactDoubleTail=-1.0;
		
		sequence = new NumericSequence();
		
		dataReady=false;
		performed=false;
		
	}//end-method

	/**
	 * Builder. Set data and all the necessary parameters of the test.
	 * 
	 * @param newSequence sample data
	 * @param quantile quantile to test
	 * @param value hyphotesis value 
	 */
	public PopulationQuantile(NumericSequence newSequence,double quantile,double value){
		
		setReportFormat();
		
		sequence = new NumericSequence(newSequence);
		
		this.quantile = quantile;
		hyphotesisValue = value;

		distribution=new BinomialDistribution();
		
		distribution.setN(sequence.size());
		distribution.setP(1.0-quantile);
		
		dataReady=true;
		
	}//end-method

	/**
	 * Sets data and all the necessary parameters of the test
	 * 
	 * @param newSequence sample data
	 * @param quantile quantile to test
	 * @param value hyphotesis value 
	 */
	public void setData(NumericSequence newSequence,double quantile,double value){
		
		sequence = new NumericSequence(newSequence);
		
		this.quantile = quantile;
		hyphotesisValue = value;

		distribution=new BinomialDistribution();
		
		distribution.setN(sequence.size());
		distribution.setP(1.0-quantile);
		
		dataReady=true;
		
	}//end-method
	
	/**
	 * Set quantile to estimate
	 * 
	 * @param p quantile
	 */
	public void setQuantile(double p){
		
		this.quantile = p;
		
	}//end-method
	
	/**
	 * Set hypothesis value
	 * 
	 * @param value hypothesis value
	 */
	public void setConfidence(double value){
		
		this.hyphotesisValue = value;
		
	}//end-method
	
	/**
	 * Performs the test
	 */
	public void doTest(){
		
		if(!dataReady){		
			System.out.println("Data is not ready");
			return;	
		}
		
		K=0;
		
		sequence.sort();
		
		for(double d : sequence.getSequence()){
			if(d>hyphotesisValue){
				K++;
			}
		}
		
		//computation of p-value
		exactLeftTail=distribution.computeCumulativeProbability(K);
		exactRightTail=1.0-distribution.computeCumulativeProbability(K-1);
		exactDoubleTail=Math.min(exactLeftTail,exactRightTail)*2.0;
		
		performed=true;

	}//end-method
	
	/**
	 * Get K statistic 
	 * 
	 * @return K statistic 
	 */
	public double getK(){
		
		return K;
		
	}//end-method
	
	/**
	 * Get left tail exact p-value 
	 * 
	 * @return left tail p-value computed
	 */
	public double getExactLeftPValue(){
		
		return exactLeftTail;
		
	}//end-method
	
	/**
	 * Get right tail exact p-value 
	 * 
	 * @return right tail p-value computed
	 */
	public double getExactRightPValue(){
		
		return exactRightTail;
		
	}//end-method
	
	/**
	 * Get double tail exact p-value 
	 * 
	 * @return double tail p-value computed
	 */
	public double getExactDoublePValue(){
		
		return exactDoubleTail;
		
	}//end-method
	
	/**
	 * Prints the data stored in the test
	 * 
	 * @return Data stored
	 */
	public String printData(){
			
		String text="";
			
		text+="\n"+sequence;
			
		return text;
			
	}//end-method

	/**
	 * Prints a report with the results of the test
	 * 
	 * @return Output report
	 */
	public String printReport(){
			
		String report="";
		
		if(!performed){
			report+="The test has not been performed.\n";
			return report;
		}
		
		report+="\n***************************************\n";
		report+="Testing a population quantile\n";
		report+="***************************************\n\n";

		report+="Hypothesis value value: "+nf6.format(hyphotesisValue)+"\n";
		report+="Quantile: "+nf6.format(quantile)+"\n";
		report+="K statistic: "+nf6.format(K)+"\n\n";
					
		report+="Exact P-Value (Left tail, Kp < Kp0): "+nf6.format(exactLeftTail)+"\n";
		report+="Exact P-Value (Right tail, Kp > Kp0): "+nf6.format(exactRightTail)+"\n";	
		report+="Exact P-Value (Double tail, Kp != Kp0): "+nf6.format(exactDoubleTail)+"\n\n";

		return report;
			
	}//end-method
		
}//end-class
	