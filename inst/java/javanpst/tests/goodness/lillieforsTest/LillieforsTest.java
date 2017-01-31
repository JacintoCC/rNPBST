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

package javanpst.tests.goodness.lillieforsTest;

import javanpst.data.structures.sequence.NumericSequence;
import javanpst.distributions.DistributionDefinitions;
import javanpst.distributions.common.continuous.ExponentialDistribution;
import javanpst.distributions.common.continuous.NormalDistribution;
import javanpst.distributions.tests.LillieforsDistribution;
import javanpst.tests.StatisticalTest;

/**
 * The Lilliefors test.
 * 
 * This goodness of fit test can be used to adjust a given
 * sample to a Normal distribution, with unknown mean and
 * sigma parameters. Adjustment to an Exponential distribution,
 * with unknown mean, is also possible.
 * 
 * @author Joaquin
 * @version 1.0
 */
public class LillieforsTest extends StatisticalTest{

	/**
	 * Distribution of the test
	 */
	private LillieforsDistribution distribution;
	
	/**
	 * Sample to adjust
	 */
	private NumericSequence sequence;
	
	/**
	 * Sequence quantiles
	 */
	private double Sn [];
	
	/**
	 * True distribution selected
	 */
	private double F0 [];
	
	/**
	 * Test statistic
	 */
	private double Dn;
	
	/**
	 * Tests if the distribution to adjust is defined
	 */
	private boolean distributionReady;
	
	/**
	 * Distribution to adjust
	 */
	private int typeDist;
	
	/**
	 * Normal distribution
	 */
	private NormalDistribution normal;
	
	/**
	 * P-value of the adjustment
	 */
	private double pValue;
	
	/**
	 * Exponential distribution
	 */
	private ExponentialDistribution exponential;

	/**
	 * Default builder
	 */
	public LillieforsTest(){
		
		distribution= LillieforsDistribution.getInstance();
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
		
		typeDist=-1;
		Dn=0.0;
		
		F0=null;
		Sn=null;
		
		pValue=-1.0;
		
	}//end-method
	
	/**
	 * Builder. Load data to test.
	 * 
	 * @param newSequence data to test.
	 */
	public LillieforsTest(NumericSequence newSequence){
		
		distribution= LillieforsDistribution.getInstance();
		setReportFormat();

		sequence=new NumericSequence(newSequence);
		
		sequence.sort();
		
		Sn=new double [sequence.size()];
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
		
		Sn=new double [sequence.size()];
		F0=new double [sequence.size()];
		
		performed=false;
		dataReady=true;
		distributionReady=false;

	}//end-method
	
	/**
	 * Sets adjustment to a normal distribution
	 */
	public void adjustNormal(){
		
		typeDist=DistributionDefinitions.NORMAL;
		
		distributionReady=true;
		
	}//end-method
	
	/**
	 * Sets adjustment to a exponential distribution
	 */
	public void adjustExponential(){
		
		typeDist=DistributionDefinitions.EXPONENTIAL;
		
		distributionReady=true;
		
	}//end-method
	
	/**
	 * Performs the test
	 */
	public void doTest(){
	
		double value,value2;

		if(!dataReady){		
			System.out.println("Data is not ready");
			return;	
		}
		
		if(!distributionReady){		
			System.out.println("Distribution to fit is not set");
			return;	
		}
		
		Dn=0.0;

		//fill Sn
		for(int i=0;i<Sn.length;i++){
			Sn[i]=((double)(i+1)/Sn.length);
		}
		
		//fill F0
		estimateParameters();
		
		if(typeDist==DistributionDefinitions.NORMAL){
			for(int i=0;i<F0.length;i++){
				F0[i]=normal.computeCumulativeProbability(sequence.getSequence().get(i));	
			}
		}else{
			for(int i=0;i<F0.length;i++){
				F0[i]=exponential.computeCumulativeProbability(sequence.getSequence().get(i));
			}		
		}
		
		//Compute Dn
		
		for(int i=0;i<F0.length;i++){
			
			//Sn - F0	
			value=Math.abs(Sn[i]-F0[i]);
			Dn=Math.max(Dn,value);
			
			//Sn(X-e) - F0
			if(i==0){
				value2=0.0;
			}
			else{
				value2=Sn[i-1];
				
			}
			
			value=Math.abs(value2-F0[i]);
			Dn=Math.max(Dn,value);

		}
		
		if(typeDist==DistributionDefinitions.NORMAL){
			pValue=distribution.computeProbabilityNormal(sequence.size(), Dn);
		}
		else{
			pValue=distribution.computeProbabilityExponential(sequence.size(), Dn);
		}
		
		performed=true;
		
	}//end-method
	
	/**
	 * Estimate parameters of the selected distribution
	 */
	private void estimateParameters(){
		
		if(typeDist==DistributionDefinitions.NORMAL){
			normal= new NormalDistribution();
			
			double mean=0.0;
			double sigma=0.0;
			
			for(double d: sequence.getSequence()){
				mean+=d;
			}
			
			mean/=(double)sequence.size();
			
			normal.setMean(mean);
			
			for(double d: sequence.getSequence()){
				sigma+=((d-mean)*(d-mean));
			}
			
			sigma/=((double)sequence.size()-1.0);
			sigma=Math.sqrt(sigma);
			
			normal.setS(sigma);
		}
		else{
			exponential = new ExponentialDistribution ();
			
			double mean=0.0;
			
			for(double d: sequence.getSequence()){
				mean+=d;
			}
			
			mean/=(double)sequence.size();
			
			exponential.setMean(mean);
		}
		
	}//end-method

	/**
	 * Get Dn statistic 
	 * 
	 * @return Dn Statistic
	 */
	public double getDn(){
		
		return Dn;
		
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
		report+="Lilliefors goodness of fit test\n";
		report+="**************\n\n";
		
		switch(typeDist){
		
			case DistributionDefinitions.NORMAL:
				report+="Fitting data to Normal distribution with Mean = "+nf6.format(normal.getMean())+" and Sigma = "+nf6.format(normal.getSigma())+" )\n\n";
				break;
			
			case DistributionDefinitions.EXPONENTIAL:
				report+="Fitting data to Exponential distribution with Mean = "+nf6.format(exponential.getMean())+" )\n\n";
				break;

	
		}

		report+="Dn Statistic: "+nf6.format(Dn)+"\n\n";
		
		if(distribution.isApproximate()){
			report+="Asymptotic p-value : <= "+nf6.format(pValue)+"\n";
		}
		else{
			report+="Exact p-value : <= "+nf6.format(pValue)+"\n";
		}

		return report;
		
	}//end-method
	
}//end-class
