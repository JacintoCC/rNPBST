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

package javanpst.tests.goodness.A_DTest;

import javanpst.data.structures.sequence.NumericSequence;
import javanpst.distributions.DistributionDefinitions;
import javanpst.distributions.common.continuous.ChiSquareDistribution;
import javanpst.distributions.common.continuous.ExponentialDistribution;
import javanpst.distributions.common.continuous.GammaDistribution;
import javanpst.distributions.common.continuous.LaplaceDistribution;
import javanpst.distributions.common.continuous.LogisticDistribution;
import javanpst.distributions.common.continuous.NormalDistribution;
import javanpst.distributions.common.continuous.UniformCDistribution;
import javanpst.distributions.common.continuous.WeibullDistribution;
import javanpst.tests.StatisticalTest;

/**
 * The A_D test.
 * 
 * The Anderson-Darling test can be used to adjust a given
 * sample to either a Normal or a Exponential distribution.
 * 
 * It is possible to select which parameters are defined for each
 * distribution: Mean, sigma, both, or none. 
 * 
 * Additionally, adjust to other continuous distributions is supported,
 * (Uniform, Chi-Square, Laplace, Logistic, Gamma and Weibull)
 * but they have to be completely defined 
 * 
 * @author Joaquin
 * @version 1.0
 */
public class A_DTest extends StatisticalTest{

	/**
	 * Some constants defining the type of test
	 */
	private static final int COMPLETE = 0;
	private static final int NORMAL_NO_MEAN = 1;
	private static final int NORMAL_NO_VARIANCE = 2;
	private static final int NORMAL_NO_MEAN_VARIANCE = 3;
	private static final int EXPONENTIAL_NO_MEAN = 4;
	
	/**
	 * Sample to adjust
	 */
	private NumericSequence sequence;
	
	/**
	 * True distribution selected
	 */
	private double F0 [];
	
	/**
	 * Exponential distribution
	 */
	private ExponentialDistribution exponential;
	
	/**
	 * Normal distribution
	 */
	private NormalDistribution normal;
	
	/**
	 * Chi-square distribution
	 */
	private ChiSquareDistribution chisquare;

	/**
	 * Uniform distribution
	 */
	private UniformCDistribution uniform;
	
	/**
	 * Gamma distribution
	 */
	private GammaDistribution gamma;
	
	/**
	 * Laplace distribution
	 */
	private LaplaceDistribution laplace;
	
	/**
	 * Logistic distribution
	 */
	private LogisticDistribution logistic;
	
	/**
	 * Weibull distribution
	 */
	private WeibullDistribution weibull;
	
	/**
	 * Type of test selected
	 */
	private int typeTest;
	
	/**
	 * Type of distribution selected
	 */
	private int typeDistribution;
	
	/**
	 * Mean to adjust
	 */
	private double mean;
	
	/**
	 * Sigma parameter to adjust
	 */
	private double sigma;
	
	/**
	 * Tests if the distribution to adjust is defined
	 */
	private boolean distributionReady;
	
	/**
	 * W2 statistic
	 */
	private double W2;
	
	/**
	 * A statistic
	 */
	private double A;
	
	/**
	 * P-value of the adjustment
	 */
	private double pValue;
	
	/**
	 * Default builder
	 */
	public A_DTest(){

		setReportFormat();
		clearData();
		
	}//end-method
	
	/**
	 * Clears the data stored in the test
	 */
	public void clearData(){
		
		sequence=new NumericSequence();
		
		performed=false;
		dataReady=false;
		distributionReady=false;
		
		typeTest=-1;
		typeDistribution=-1;
		W2=0.0;
		A=0.0;
		
		F0=null;
		
		pValue=-1.0;
		
	}//end-method
	
	/**
	 * Builder. Load data to test.
	 * 
	 * @param newSequence data to test.
	 */
	public A_DTest(NumericSequence newSequence){
		
		setReportFormat();

		sequence=new NumericSequence(newSequence);
		
		sequence.sort();
		
		F0=new double [sequence.size()];
		
		performed=false;
		dataReady=true;
		distributionReady=false;

	}//end-method

	/**
	 * Load data to test.
	 * 
	 * @param newSequence data to test.
	 */
	public void setData(NumericSequence newSequence){
		
		sequence=new NumericSequence(newSequence);
		
		sequence.sort();
		
		F0=new double [sequence.size()];
		
		performed=false;
		dataReady=true;
		distributionReady=false;
		
	}//end-method
	
	/**
	 * Sets adjustment to a Normal distribution,
	 * without specifying mean or sigma
	 */
	public void adjustNormal(){
		
		typeDistribution=DistributionDefinitions.NORMAL;
		typeTest=NORMAL_NO_MEAN_VARIANCE;
		
		normal=new NormalDistribution();
		
		//parameters are estimated
		estimateParameters();
		
		normal.setMean(mean);
		normal.setS(sigma);
		
		distributionReady=true;
		
	}//end-method
	
	/**
	 * Sets adjustment to a Normal distribution,
	 * specifying its mean.
	 * 
	 * @param m mean of the distribution
	 */
	public void adjustNormalMean(double m){
		
		typeDistribution=DistributionDefinitions.NORMAL;
		typeTest=NORMAL_NO_VARIANCE;
		
		normal=new NormalDistribution();
		
		//parameters are estimated
		estimateParameters();
		
		normal.setMean(m);
		normal.setS(sigma);
		
		distributionReady=true;
		
	}//end-method
	
	/**
	 * Sets adjustment to a Normal distribution,
	 * specifying its mean.
	 * 
	 * @param s sigma parameter of the distribution
	 */
	public void adjustNormalVariance(double s){
		
		typeDistribution=DistributionDefinitions.NORMAL;
		typeTest=NORMAL_NO_MEAN;
		
		normal=new NormalDistribution();
		
		//parameters are estimated
		estimateParameters();
		
		normal.setMean(mean);
		normal.setS(s);
		
		distributionReady=true;
		
	}//end-method
	
	/**
	 * Sets adjustment to a Normal distribution,
	 * specifying mean and sigma.
	 * 
	 * @param m mean of the distribution
	 * @param s sigma parameter of the distribution
	 */
	public void adjustNormal(double m, double s){
		
		typeDistribution=DistributionDefinitions.NORMAL;
		typeTest=COMPLETE;
		
		normal=new NormalDistribution();
		
		normal.setMean(m);
		normal.setS(s);
		
		distributionReady=true;
		
	}//end-method
	
	/**
	 * Sets adjustment to a Exponential distribution,
	 * without specifying mean.
	 */
	public void adjustExponential(){
		
		typeDistribution=DistributionDefinitions.EXPONENTIAL;
		typeTest=EXPONENTIAL_NO_MEAN;
		
		exponential=new ExponentialDistribution();
		
		//parameters are estimated
		estimateParameters();
		
		exponential.setMean(mean);
		
		distributionReady=true;
		
	}//end-method
	
	/**
	 * Sets adjustment to a Exponential distribution
	 * fully defined
	 * 
	 * @param m mean of the distribution
	 */
	public void adjustExponential(double m){
		
		typeDistribution=DistributionDefinitions.EXPONENTIAL;
		typeTest=COMPLETE;
		
		exponential=new ExponentialDistribution();

		exponential.setMean(m);
		
		distributionReady=true;
		
	}//end-method
	
	/**
	 * Sets adjustment to a Uniform distribution
	 * fully defined
	 *
	 * @param start lower limit of the distribution
	 * @param end upper limit of the distribution
	 */
	public void adjustUniform(double start,double end){
		
		typeDistribution=DistributionDefinitions.UNIFORMC;
		typeTest=COMPLETE;
		
		uniform=new UniformCDistribution();

		uniform.setStart(start);
		uniform.setEnd(end);
		
		distributionReady=true;
		
	}//end-method
	
	/**
	 * Sets adjustment to a Chi-square distribution
	 * fully defined
	 * 
	 * @param freedom number of degrees of freedom
	 */
	public void adjustChiSquare(int freedom){
		
		typeDistribution=DistributionDefinitions.CHI_SQUARE;
		typeTest=COMPLETE;
		
		chisquare=new ChiSquareDistribution();

		chisquare.setDegree(freedom);
		
		distributionReady=true;
		
	}//end-method
	
	/**
	 * Sets adjustment to a Gamma distribution
	 * fully defined
	 * 
	 * @param K K parameter of the distribution
	 * @param lambda lambda parameter of the distribution
	 */
	public void adjustGamma(double K, double lambda){
		
		typeDistribution=DistributionDefinitions.GAMMA;
		typeTest=COMPLETE;
		
		gamma=new GammaDistribution();

		gamma.setK(K);
		gamma.setLambda(lambda);
		
		distributionReady=true;
		
	}//end-method
	
	/**
	 * Sets adjustment to a Laplace distribution
	 * fully defined
	 * 
	 * @param mean mean of the distribution
	 * @param scale scale parameter of the distribution
	 */
	public void adjustLaplace(double mean, double scale){
		
		typeDistribution=DistributionDefinitions.LAPLACE;
		typeTest=COMPLETE;
		
		laplace=new LaplaceDistribution();

		laplace.setMean(mean);
		laplace.setScale(scale);
		
		distributionReady=true;
		
	}//end-method
	
	/**
	 * Sets adjustment to a Logistic distribution
	 * fully defined
	 * 
	 * @param mean mean of the distribution
	 * @param S S parameter of the distribution
	 */
	public void adjustLogistic(double mean, double S){
		
		typeDistribution=DistributionDefinitions.LOGISTIC;
		typeTest=COMPLETE;
		
		logistic=new LogisticDistribution();

		logistic.setMean(mean);
		logistic.setS(S);
		
		distributionReady=true;
		
	}//end-method
	
	/**
	 * Sets adjustment to a Weibull distribution
	 * fully defined
	 * 
	 * @param K K parameter of the distribution
	 * @param lambda lambda parameter of the distribution
	 */
	public void adjustWeibull(double K, double lambda){
		
		typeDistribution=DistributionDefinitions.WEIBULL;
		typeTest=COMPLETE;
		
		weibull=new WeibullDistribution();

		weibull.setK(K);
		weibull.setLambda(lambda);
		
		distributionReady=true;
		
	}//end-method
	
	/**
	 * Performs the test
	 */
	public void doTest(){

		double term1,term2,term3;
		double n=sequence.size();

		if(!dataReady){		
			System.out.println("Data is not ready");
			return;	
		}
		
		if(!distributionReady){		
			System.out.println("Distribution to fit is not set");
			return;	
		}
		
		F0=new double [sequence.size()];
	
		//compute F0
		for(int i=0;i<n;i++){
			F0[i]=computeExpected(sequence.getSequence().get(i));
		}
		
		//Compute W2
		W2=0.0;
		for(int i=0;i<n;i++){
			term1=(2.0*(i+1))-1.0;
			term2=Math.log(F0[i]);
			term3=Math.log(1.0-F0[(int)n-(i+1)]);
			W2+=term1*(term2+term3);
		}
		
		W2*=-1.0/n;
		
		W2-=n;
		
		//compute A
		switch(typeTest){
		
			case EXPONENTIAL_NO_MEAN: 
				A=W2*(1.0+(0.3/n));
				break;
			case NORMAL_NO_MEAN_VARIANCE: 
				A=W2*(1.0+(0.75/n)+(2.25/(n*n)));
				break;
			case NORMAL_NO_VARIANCE: 	
			case NORMAL_NO_MEAN: 
			case COMPLETE:
			default:
				A=W2;
				break;
		}
		
		pValue=computePValue(A);
		
		performed=true;

	}//end-method
	
	/**
	 * Computes p-value given the A statistic
	 * 
	 * @param A statistic
	 * @return p-value computed
	 */
	private double computePValue(double A){
	
		double criticalValues [][]={
				{3.857,3.070,2.492,1.933,1.610},
				{1.551,1.285,1.087,0.894,0.782},
				{3.702,2.898,2.308,1.743,1.430},
				{1.035,0.873,0.752,0.631,0.561},
				{1.959,1.591,1.321,1.062,0.916}				
		};
		
		double criticalLevels [] = {0.01,0.025,0.05,0.10,0.15};
		
		for(int i=0;i<5;i++){
			if(A>=criticalValues[typeTest][i]){
				return criticalLevels[i];
			}
		}
		
		return 1.0;
		
	}//end-method
	
	/**
	 * Compute expected cumulative value for the distribution to adjust
	 * 
	 * @param value value of the distribution
	 * @return value computed
	 */
	private double computeExpected(double value){
		
		double prob=0.0;
		
		switch(typeDistribution){
		
			case DistributionDefinitions.NORMAL:
					prob=normal.computeCumulativeProbability(value);
				break;
				
			case DistributionDefinitions.UNIFORMC:
					prob=uniform.computeCumulativeProbability(value);
				break;
				
			case DistributionDefinitions.CHI_SQUARE:
					prob=chisquare.computeCumulativeProbability(value);
				break;
				
			case DistributionDefinitions.EXPONENTIAL:
					prob=exponential.computeCumulativeProbability(value);
				break;
				
			case DistributionDefinitions.GAMMA:
					prob=gamma.computeCumulativeProbability(value);
				break;
				
			case DistributionDefinitions.LAPLACE:
					prob=laplace.computeCumulativeProbability(value);
				break;
				
			case DistributionDefinitions.LOGISTIC:
					prob=logistic.computeCumulativeProbability(value);
				break;
				
			case DistributionDefinitions.WEIBULL:
					prob=weibull.computeCumulativeProbability(value);
				break;
	
		}
		
		return prob;
		
	}//end-method
	
	/**
	 * Estimate mean and sigma parameters from the sequence
	 * to adjust
	 */
	private void estimateParameters(){
		
		mean=0.0;
		sigma=0.0;
		
		for(double d: sequence.getSequence()){
			mean+=d;
		}
		
		mean/=(double)sequence.size();
		
		for(double d: sequence.getSequence()){
			sigma+=((d-mean)*(d-mean));
		}
		
		sigma/=((double)sequence.size()-1.0);
		sigma=Math.sqrt(sigma);
	
	}//end-method

	/**
	 * Get W2 statistic 
	 * 
	 * @return W2 Statistic
	 */
	public double W2(){
		
		return A;
		
	}//end-method
	
	/**
	 * Get A statistic 
	 * 
	 * @return A Statistic
	 */
	public double getA(){
		
		return A;
		
	}//end-method
	
	/**
	 * Get p-value of the test
	 * 
	 * @return p-value computed
	 */
	public double getPValue(){
		
		return pValue;
		
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
		
		report+="\n************\n";
		report+="Anderson-Darling goodness of fit test\n";
		report+="**************\n\n";
		
		switch(typeDistribution){
		
			case DistributionDefinitions.NORMAL:
				report+="Fitting data to Normal distribution with Mean = "+nf6.format(normal.getMean())+" and Sigma = "+nf6.format(normal.getSigma())+"\n\n";
				break;
			
			case DistributionDefinitions.EXPONENTIAL:
				report+="Fitting data to Exponential distribution with Mean = "+nf6.format(exponential.getMean())+"\n\n";
				break;
			
			case DistributionDefinitions.UNIFORMC:
				report+="Fitting data to Uniform distribution between ["+nf6.format(uniform.getStart())+","+nf6.format(uniform.getEnd())+"]\n\n";
				break;
			
			case DistributionDefinitions.CHI_SQUARE:
				report+="Fitting data to Chi-square distribution with "+nf6.format(chisquare.getDegree())+" degrees of freedom\n\n";
				break;
			
			case DistributionDefinitions.GAMMA:
				report+="Fitting data to Gamma distribution with K = "+nf6.format(gamma.getK())+" and Lambda = "+nf6.format(gamma.getLambda())+"\n\n";
				break;
				
			case DistributionDefinitions.WEIBULL:
				report+="Fitting data to Weibull distribution with K = "+nf6.format(weibull.getK())+" and Lambda = "+nf6.format(weibull.getLambda())+"\n\n";
				break;
				
			case DistributionDefinitions.LAPLACE:
				report+="Fitting data to Laplace distribution with Mean = "+nf6.format(laplace.getMean())+" and Scale = "+nf6.format(laplace.getScale())+"\n\n";
				break;

			case DistributionDefinitions.LOGISTIC:
				report+="Fitting data to Logistic distribution with Mean = "+nf6.format(logistic.getMean())+" and S = "+nf6.format(logistic.getS())+"\n\n";
				break;
		}

		report+="W2 Statistic: "+nf6.format(W2)+"\n";
		report+="A Statistic: "+nf6.format(A)+"\n\n";
		
		report+="Exact p-value : <= "+nf6.format(pValue)+"\n";
	
		return report;
		
	}//end-method
	
}//end-class
