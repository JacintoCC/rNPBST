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

package javanpst.tests.randomness.vonNeumannTest;

import java.util.Arrays;

import javanpst.data.structures.sequence.NumericSequence;
import javanpst.distributions.common.continuous.NormalDistribution;
import javanpst.distributions.tests.NMDistribution;
import javanpst.distributions.tests.RVNDistribution;
import javanpst.tests.StatisticalTest;

/**
 * The Von Neumann test
 * 
 * This tests check randomness of a numeric sequence by 
 * assigning ranks to each value.
 * 
 * A normal approximation is also provided. 
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class VonNeumannTest extends StatisticalTest{

	/**
	 * NM distribution
	 */
	private NMDistribution distributionNM;
	
	/**
	 * RVN distribution
	 */
	private RVNDistribution distributionRVN;
	
	/**
	 * Sequence to test
	 */
	private NumericSequence sequence;
	
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
	 * Left tail asymptotic p-value
	 */
	private double asymptoticLeftTail;
	
	/**
	 * Right tail asymptotic p-value
	 */
	private double asymptoticRightTail;
	
	/**
	 * Double tail asymptotic p-value
	 */
	private double asymptoticDoubleTail;
	
	/**
	 * Ranks of the sequence
	 */
	private double [] ranks;
	
	/**
	 * NM statistic
	 */
	private double NM;
	
	/**
	 * RVN Statistic
	 */
	private double RVN;

	/**
	 * Default builder
	 */
	public VonNeumannTest(){
		
		distributionNM=NMDistribution.getInstance();
		distributionRVN=RVNDistribution.getInstance();
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

		NM=0.0;
		RVN=0.0;
		
		ranks=null;
		
		exactLeftTail=-1.0;
		exactRightTail=-1.0;
		exactDoubleTail=-1.0;
		
		asymptoticLeftTail=-1.0;
		asymptoticRightTail=-1.0;
		asymptoticDoubleTail=-1.0;
		
	}//end-method

	/**
	 * Builder. Load data to test.
	 * 
	 * @param newSequence data to test.
	 */
	public VonNeumannTest(NumericSequence newSequence){

		distributionNM=NMDistribution.getInstance();
		distributionRVN=RVNDistribution.getInstance();
		setReportFormat();
		
		sequence=new NumericSequence(newSequence);
		
		dataReady=true;
		performed=false;
		
	}//end-method
	
	/**
	 * Load data to test.
	 * 
	 * @param newSequence data to test.
	 */
	public void setData(NumericSequence newSequence){
		
		sequence=new NumericSequence(newSequence);
		
		dataReady=true;
		performed=false;
		
	}//end-method
	
	/**
	 * Perform the test
	 */
	public void doTest(){
	
		if(!dataReady){		
			System.out.println("Data is not ready");
			return;	
		}
		
		computeRanks();
		
		double denominator=0.0;
		double z;
		double variance;
		double n=(double)sequence.size();
		
		//compute test statistics
		NM=0.0;
		
		for(int i=0;i<ranks.length-1;i++){
			 NM+=((ranks[i]-ranks[i+1])*(ranks[i]-ranks[i+1]));
			 denominator+=(ranks[i]-(n+1.0)/2.0)*(ranks[i]-(n+1.0)/2.0);
		}
		denominator+=(ranks[ranks.length-1]-(n+1.0)/2.0)*(ranks[ranks.length-1]-(n+1.0)/2.0);
		
		RVN=NM/denominator;
		
		//compute exact p-values
		
		if(n<=10){
			//use NM table
			exactLeftTail=distributionNM.computeLeftProbability(sequence.size(),NM);
			exactRightTail=distributionNM.computeRightProbability(sequence.size(),NM);
		}
		else{
			//use RVN table
			exactLeftTail=distributionRVN.computeLeftProbability(sequence.size(),RVN);
			exactRightTail=distributionRVN.computeRightProbability(sequence.size(),RVN);	
		}
		
		exactDoubleTail=Math.min(Math.min(exactLeftTail, exactRightTail)*2.0,1.0);
		
		//compute Asymptotic p-value based on RVN
		
		NormalDistribution normal=new NormalDistribution();
		
		variance=4.0*(n-2.0)*(5.0*n*n-2*n-9.0)/(5.0*n*(n+1.0)*(n-1.0)*(n-1.0));
		
		z=(RVN-2.0)/Math.sqrt(variance);
		
		asymptoticLeftTail=normal.getTipifiedProbability(z, false);
		asymptoticRightTail=normal.getTipifiedProbability(z, true);
		asymptoticDoubleTail=Math.min(Math.min(asymptoticLeftTail, asymptoticRightTail)*2.0,1.0);

		performed=true;
		
	}//end-method

	/**
	 * Compute ranks of the sequence (ties are handled with the midrank method)
	 */
	private void computeRanks(){
		
		int rank=1;
		double minValue;
		double rankValue;
		int ties;
		
		ranks=new double [sequence.size()];
		
		Arrays.fill(ranks, -1.0);
		
		while(rank<=sequence.size()){
			
			minValue=Double.MAX_VALUE;
			ties=0;
			
			for(int i=0;i<sequence.size();i++){
				if(ranks[i]==-1.0){
					if(sequence.get(i)==minValue){
						ties++;
					}
					if(sequence.get(i)<minValue){
						minValue=sequence.get(i);
						ties=1;
					}
				}
			}
			
			if(ties==1){
				rankValue=rank;
			}
			else{
				rankValue=((ties+1.0)*(ties)/2)+((rank-1)*ties)/ties;
			}
			
			for(int i=0;i<sequence.size();i++){
				if(sequence.get(i)==minValue){
					ranks[i]=rankValue;
				}
			}
			
			rank+=ties;
		}
				
	}//end-method

	/**
	 * Get the NM statistic
	 * 
	 * @return NM statistic
	 */
	public double getNM(){
		
		return NM;
		
	}//end-method
	
	/**
	 * Get the RVN statistic
	 * 
	 * @return RVN statistic
	 */
	public double getRVN(){
		
		return RVN;
		
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
	 * Get left tail p-value 
	 * 
	 * @return left tail p-value computed
	 */
	public double getLeftPValue(){
		
		return asymptoticLeftTail;
		
	}//end-method
	
	/**
	 * Get right tail p-value 
	 * 
	 * @return right tail p-value computed
	 */
	public double getRightPValue(){
		
		return asymptoticRightTail;
		
	}//end-method
	
	/**
	 * Get double tail p-value 
	 * 
	 * @return double tail p-value computed
	 */
	public double getDoublePValue(){
		
		return asymptoticDoubleTail;
		
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
		
		report+="\n****************************************\n";
		report+="Von Neumann test (ranks test of randomness)\n";
		report+="****************************************\n\n";

		report+="NM statistic: "+nf6.format(NM)+"\n";
		report+="RVN statistic: "+nf6.format(RVN)+"\n\n";
					
		report+="Exact P-Value (Left tail, Too few runs): "+nf6.format(exactLeftTail)+"\n";
		report+="Exact P-Value (Right tail, Too many runs): "+nf6.format(exactRightTail)+"\n";
		report+="Exact P-Value (Double tail, Non randomness): "+nf6.format(exactDoubleTail)+"\n\n";
		report+="Asymptotic P-Value (Left tail, Too few runs): "+nf6.format(asymptoticLeftTail)+"\n";
		report+="Asymptotic P-Value (Right tail, Too many runs): "+nf6.format(asymptoticRightTail)+"\n";
		report+="Asymptotic P-Value (Double tail, Non randomness): "+nf6.format(asymptoticDoubleTail)+"\n\n";

		return report;
			
	}//end-method
		
}//end-class
