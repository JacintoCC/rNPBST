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

package javanpst.tests.oneSample.confidenceQuantile;

import javanpst.distributions.common.continuous.NormalDistribution;
import javanpst.distributions.common.discrete.BinomialDistribution;
import javanpst.tests.StatisticalTest;

/**
 * A test for estimating population quantiles.
 * 
 * This test estimates a population quantile providing
 * a confidence interval as closer as possible. It does 
 * 
 * Two confidence intervals are provided: an exact version,
 * using the Binomial distribution, and an approximate interval,
 * using the Normal distribution.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class ConfidenceQuantile extends StatisticalTest{

	/**
	 * Sample size
	 */
	private int N;
	
	/**
	 * Quantile to estimate
	 */
	private double p;
	
	/**
	 * Confidence level desired
	 */
	private double confidence;
	
	/**
	 * Limits of the confidence intervals
	 */
	private double upper, lower, r, s;
	
	/**
	 * Exact confidence level 
	 */
	private double exactConfidenceLevel;
	
	/**
	 * Binomial distribution
	 */
	private BinomialDistribution binomial;
	
	/**
	 * Normal distribution
	 */
	private NormalDistribution normal;

	/**
	 * Default builder
	 */
	public ConfidenceQuantile(){
		
		setReportFormat();
		clearData();
		
	}//end-method
	
	/**
	 * Clears the data stored in the test
	 */
	public void clearData(){
		
		N=0;
		p=0.0;
		confidence=0.0;
		
		dataReady=false;
		performed=false;
		
	}//end-method
	
	/**
	 * Builder. Set all the necessary parameters of the test.
	 * 
	 * @param n sample size
	 * @param p quantile to estimate
	 * @param confidence confidence level desired
	 */
	public ConfidenceQuantile(int n, double p, double confidence){
		
		setReportFormat();
		
		this.N = n;
		this.p = p;
		this.confidence = confidence;
		
		binomial=new BinomialDistribution();
		
		binomial.setN(N);
		binomial.setP(p);
		
		normal= new NormalDistribution();
		
		normal.setMean(0.0);
		normal.setS(1.0);
		
		dataReady=true;
		
	}//end-method
	
	/**
	 * Set sample size
	 * 
	 * @param n new sample size
	 */
	public void setSampleSize(int n){
		
		this.N = n;
		
		dataReady=true;
		
	}//end-method
	
	/**
	 * Set quantile to estimate
	 * 
	 * @param p quantile
	 */
	public void setQuantile(double p){
		
		this.p = p;
		
		dataReady=true;
		
	}//end-method
	
	/**
	 * Set confidence desired
	 * 
	 * @param c confidence level
	 */
	public void setConfidence(double c){
		
		this.confidence = c;
		
		dataReady=true;
		
	}//end-method
	
	/**
	 * Performs the test
	 */
	public void doTest(){

		double alpha;
		double term;
		
		if(!dataReady){		
			System.out.println("Data is not ready");
			return;	
		}
		
		alpha=1.0-confidence;
		
		//binomial adjustment
		
		lower=binomial.getLesserCumulativeProbability(alpha/2.0);
		if(lower!=-1){
			lower+=1; //binomial gives r-1;
		}
		
		upper=binomial.getUpperCumulativeProbability(1.0-(alpha/2.0));
		if(upper<=N){
			upper+=1; //binomial gives s-1;
		}
		
		exactConfidenceLevel=binomial.computeCumulativeProbability(upper-1)-binomial.computeCumulativeProbability(lower-1);
			
		//Normal approximation
		
		term= normal.inverseNormalDistribution(1.0-(alpha/2.0));
		
		term*=Math.sqrt(N*p*(1.0-p));
		
		r=(N*p)+0.5-term;
		s=(N*p)+0.5+term;
		
		r=Math.floor(r);
		s=Math.ceil(s);
		
		performed=true;

	}//end-method
	
	/**
	 * Get lower limit of the exact confidence interval 
	 * 
	 * @return exact lower limit. NaN if the limit is -INFINITE
	 */
	public double getExactLowerLimit(){
		
		return lower;
		
	}//end-method
	
	/**
	 * Get upper limit of the exact confidence interval 
	 * 
	 * @return exact upper limit. NaN if the limit is +INFINITE
	 */
	public double getExactUpperLimit(){
		
		return upper;
		
	}//end-method
	
	/**
	 * Get lower limit of the approximate confidence interval 
	 * 
	 * @return approximate lower limit. NaN if the limit is -INFINITE
	 */
	public double getApproximateLowerLimit(){
		
		return r;
		
	}//end-method
	
	/**
	 * Get upper limit of the approximate confidence interval
	 * 
	 * @return approximate upper limit. NaN if the limit is +INFINITE
	 */
	public double getApproximateUpperLimit(){
		
		return s;
		
	}//end-method

	/**
	 * Prints the data stored in the test
	 * 
	 * @return Data stored
	 */
	public String printData(){
			
		String text="";
			
		text+="N: "+N+" P: "+p+" Confidence: "+confidence+"\n";
			
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
			
		String lowerLimit,upperLimit,rLimit,sLimit;
		
		if(lower<1){
			lowerLimit="-INFINITY";
		}
		else{
			lowerLimit=nf6.format(lower);
		}
		
		if(upper<=N){
			upperLimit=nf6.format(upper);
		}
		else{
			upperLimit="+INFINITY";
		}
		
		if(r<1){
			rLimit="-INFINITY";
		}
		else{
			rLimit=nf6.format(r);
		}
		
		if(s<=N){
			sLimit=nf6.format(s);
		}
		else{
			sLimit="+INFINITY";
		}
		
		report+="\n***************************************\n";
		report+="Confidence interval for a population quantile\n";
		report+="***************************************\n\n";

		report+="Exact confidence interval (binomial): ["+lowerLimit+","+upperLimit+"]\n";
		report+="Confidence: "+nf6.format(exactConfidenceLevel)+"\n\n";

		report+="Approximate confidence interval (normal): ["+rLimit+","+sLimit+"]\n";
		report+="Confidence: "+nf6.format(confidence)+"\n\n";			

		return report;
			
	}//end-method
		
}//end-class
	