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

package javanpst.tests.randomness.runsUpDownMedianTest;

import javanpst.data.structures.sequence.NumericSequence;
import javanpst.distributions.DistributionDefinitions;
import javanpst.distributions.tests.TotalNumberOfRunsDistribution;
import javanpst.tests.StatisticalTest;

/**
 * A test based on runs up and down
 * 
 * This tests check randomness of a numeric sequence by 
 * analyzing the changes in direction of its runs (below
 * or under the median).
 * 
 * A normal approximation is also provided. 
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class RunsUpDownMedianTest extends StatisticalTest{

	/**
	 * Distribution of the test
	 */
	private TotalNumberOfRunsDistribution distribution;
	
	/**
	 * Sequence to test
	 */
	private NumericSequence sequence;
	
	/**
	 * Median of the sequence
	 */
	private double median;
	
	/**
	 * Number of elements in the sequence
	 */
	private int n1, n2;
	
	/**
	 * Number of runs in the sequence
	 */
	private int runs;
	
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
	 * Default builder
	 */
	public RunsUpDownMedianTest(){
		
		distribution=TotalNumberOfRunsDistribution.getInstance();
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
		
		n1=0;
		n2=0;
		runs=0;
		median=0.0;
		
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
	public RunsUpDownMedianTest(NumericSequence newSequence){

		distribution=TotalNumberOfRunsDistribution.getInstance();
		setReportFormat();
		
		sequence=new NumericSequence(newSequence);
		
		dataReady=true;
		performed=false;
		
	}//end-method
	
	/**
	 * Builder. Load data to test.
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
		
		countElementsMedian();
		
		computeRuns();
		
		//compute p-values
		
		exactLeftTail=distribution.computeLeftTailProbability(n1, n2, runs);
		exactRightTail=distribution.computeRightTailProbability(n1, n2, runs);
		
		if(exactLeftTail==DistributionDefinitions.UNDEFINED){
			exactLeftTail=DistributionDefinitions.ALL;
		}
		
		if(exactRightTail==DistributionDefinitions.UNDEFINED){
			exactRightTail=DistributionDefinitions.ALL;
		}
		
		exactDoubleTail=Math.min(Math.min(exactLeftTail, exactRightTail)*2.0, 1.0);
		
		asymptoticLeftTail=distribution.computeAsymptoticLeftTailProbability(n1, n2, runs);
		asymptoticRightTail=distribution.computeAsymptoticRightTailProbability(n1, n2, runs);
		asymptoticDoubleTail=distribution.computeAsymptoticDoubleTailProbability(n1, n2, runs);
		
		performed=true;

	}//end-method
	
	/**
	 * Count the number of elements below or under the median in the sequence
	 */
	private void countElementsMedian(){
	
		int index;
		
		//compute median
		NumericSequence copy = new NumericSequence(sequence);
		
		copy.sort();
		
		index=copy.size()/2;
		
		if(copy.size()%2==1){
			median=copy.get(index);
		}else{
			median=(copy.get(index-1)+copy.get(index))/2.0;
		}
		
		n1=0;
		n2=0;
		
		for(int i=0;i<sequence.size();i++){
			
			if(sequence.get(i)>median){
				n1++;
			}
			
			if(sequence.get(i)<median){
				n2++;
			}
			
		}

	}//end-method
	
	private void computeRuns(){
		
		double value;
		boolean greater;
		int i=0;
		
		runs=1;
		
		while((sequence.get(i)==median)&&(i<sequence.size())){
			i++;
		}
		
		value=sequence.get(i);
		
		if(value>median){
			greater=true;
		}else{
			greater=false;
		}

		for(;i<sequence.size();i++){
			
			value=sequence.get(i);
			if(greater){
				if(value<median){
					runs++;
					greater=false;
				}
			}
			else{
				if(value>median){
					runs++;
					greater=true;
				}
			}
		}
		
	}//end-method
	
	/**
	 * Get the median of the sequence
	 * 
	 * @return number of runs
	 */
	public double getMedian(){
		
		return median;
		
	}//end-method

	/**
	 * Get the number of runs
	 * 
	 * @return number of runs
	 */
	public double getRuns(){
		
		return runs;
		
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
		
		report+="\n**********************************\n";
		report+="Runs up down (based on median) test\n";
		report+="************************************\n\n";

		report+="Median of the sequence: "+nf6.format(median)+"\n";
		report+="Number of runs: "+nf6.format(runs)+"\n\n";
					
		report+="Exact P-Value (Left tail, Too few runs): "+nf6.format(exactLeftTail)+"\n";
		report+="Exact P-Value (Right tail, Too many runs): "+nf6.format(exactRightTail)+"\n";
		report+="Exact P-Value (Double tail, Non randomness): "+nf6.format(exactDoubleTail)+"\n\n";
		report+="Asymptotic P-Value (Left tail, Too few runs): "+nf6.format(asymptoticLeftTail)+"\n";
		report+="Asymptotic P-Value (Right tail, Too many runs): "+nf6.format(asymptoticRightTail)+"\n";
		report+="Asymptotic P-Value (Double tail, Non randomness): "+nf6.format(asymptoticDoubleTail)+"\n\n";

		return report;
			
	}//end-method
		
}//end-class
