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

package javanpst.tests.scale.siegel_TukeyTest;

import java.util.Arrays;

import javanpst.data.DataDefinitions;
import javanpst.data.structures.dataTable.DataTable;
import javanpst.distributions.DistributionDefinitions;
import javanpst.distributions.common.continuous.NormalDistribution;
import javanpst.distributions.tests.WilcoxonRankSumDistribution;
import javanpst.tests.StatisticalTest;

/**
 * The Siegel-Tukey test.
 * 
 * Assuming the scale model (equal medians), this test can be
 * employed to test whether the two distributions selected have 
 * equal scale parameter (with the null hypothesis of equality).
 *  
 * The Siegel-Tukey test ranks elements using the same set of weights 
 * defined for the Wilcoxon Ranks-Sum test. 
 *
 * An approximation to the Normal distribution is employed to 
 * compute asymptotic p-values. Exact p-values, if the size of
 * both samples is lower than 10, are also computed
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class Siegel_TukeyTest extends StatisticalTest{

	/**
	 * Wilcoxon distribution for exact version of the test
	 */
	private WilcoxonRankSumDistribution distribution;
	
	/**
	 * Data to analyze
	 */
	private DataTable data;
	
	/**
	 * First sample
	 */
	private double sample1 [];
	
	/**
	 * Second sample
	 */
	private double sample2 [];
	
	/**
	 * Number of values of each sample
	 */
	private int values1,values2;
	
	/**
	 * Array of combined sample
	 */
	private double combined [];
	
	/**
	 * Number of values in both samples
	 */
	private int N;
	
	/**
	 * Array of weights
	 */
	private int weights [];
	
	/**
	 * Sum of weights
	 */
	private double sumWeights;
	
	/**
	 * Test statistics for first and second samples
	 */
	private double ST1,ST2;
	
	/**
	 * Left tail exact p-value
	 */
	private double exactLeftTail;
	
	/**
	 * Right tail exact p-value
	 */
	private double exactRightTail;
	
	/**
	 * Double tail exact p-value
	 */
	private double exactDoubleTail;
	
	/**
	 * Left tail p-value
	 */
	private double asymptoticLeftTail;
	
	/**
	 * Right tail p-value
	 */
	private double asymptoticRightTail;
	
	/**
	 * Double tail p-value
	 */
	private double asymptoticDoubleTail;
	
	/**
	 * Default builder
	 */
	public Siegel_TukeyTest(){
		
		distribution=WilcoxonRankSumDistribution.getInstance();
		setReportFormat();
		clearData();
		
	}//end-method	
	
	/**
	 * Clears the data stored in the test
	 */
	public void clearData(){
		
		data=new DataTable();
		
		performed=false;
		dataReady=false;
		
		ST1=0.0;
		ST2=0.0;
		
		sample1=null;
		sample2=null;
		weights=null;
		
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
	 * @param newData data to test.
	 */
	public Siegel_TukeyTest(DataTable newData){
		
		int nulls;
		int counter1, counter2;
		double value;
		
		setReportFormat();
		distribution=WilcoxonRankSumDistribution.getInstance();

		data=DataTable.newInstance(newData);
		
		if(data.getColumns()!=2){
			System.out.println("Siegel-Tukey test only can be employed with two samples");
			clearData();
			return;
		}
		
		nulls=data.getColumnNulls(0);
		values1=data.getRows()-nulls;
		sample1=new double [values1];
		nulls=data.getColumnNulls(1);
		values2=data.getRows()-nulls;
		sample2=new double [values2];
		
		counter1=0;
		counter2=0;
		
		for(int i=0;i<data.getRows();i++){
			
			value=data.get(i, 0);
			if(value!=DataDefinitions.NULL_VALUE){
				sample1[counter1]=value;
				counter1++;
			}
			
			value=data.get(i, 1);
			if(value!=DataDefinitions.NULL_VALUE){
				sample2[counter2]=value;
				counter2++;
			}
			
		}
		
		dataReady=true;
		performed=false;
		
	}//end-method
	
	/**
	 * Sets data to test
	 * 
	 * @param newData data to test.
	 */
	public void setData(DataTable newData){
		
		int nulls;
		int counter1, counter2;
		double value;
		
		data=DataTable.newInstance(newData);
		
		if(data.getColumns()!=2){
			System.out.println("Siegel-Tukey test only can be employed with two samples");
			clearData();
			return;
		}
		
		nulls=data.getColumnNulls(0);
		values1=data.getRows()-nulls;
		sample1=new double [values1];
		nulls=data.getColumnNulls(1);
		values2=data.getRows()-nulls;
		sample2=new double [values2];
		
		counter1=0;
		counter2=0;
		
		for(int i=0;i<data.getRows();i++){
			
			value=data.get(i, 0);
			if(value!=DataDefinitions.NULL_VALUE){
				sample1[counter1]=value;
				counter1++;
			}
			
			value=data.get(i, 1);
			if(value!=DataDefinitions.NULL_VALUE){
				sample2[counter2]=value;
				counter2++;
			}
			
		}
		
		dataReady=true;
		performed=false;
		
	}//end-method
	
	/**
	 * Performs the test
	 */
	public void doTest(){
		
		int pointer,pointer2;
		
		if(!dataReady){		
			System.out.println("Data is not ready");
			return;	
		}
		
		combined= new double[sample1.length+sample2.length];
		Arrays.sort(sample1);
		Arrays.sort(sample2);
		
		System.arraycopy(sample1, 0, combined, 0, sample1.length);
		System.arraycopy(sample2, 0, combined, sample1.length, sample2.length);
		
		Arrays.sort(combined);
		
		N=sample1.length+sample2.length;
		weights= new int [N];
		
		generateWeights();

		ST1=0;
		pointer=0;
		pointer2=0;
		if(values1<=values2){
			
			while(pointer<sample1.length){
				
				while(combined[pointer2]!=sample1[pointer]){
					pointer2++;
				}
				
				ST1+=weights[pointer2];
				pointer++;
			}
			ST2=sumWeights-ST1;
			
			computePValues();
		}
		else{
			while(pointer<sample2.length){
				
				while(combined[pointer2]!=sample2[pointer]){
					pointer2++;
				}
				
				ST1+=weights[pointer2];
				pointer++;
			}
			ST2=sumWeights-ST1;
			
			computePValues();
		}
		
		performed=true;
		
	}//end-method
	
	/**
	 * Generate weights array
	 */
	private void generateWeights(){

		int value;
		boolean even;
		
		sumWeights=0;
		
		if(N%2==0){
			even=true;
		}
		else{
			even=false;
		}
		
		for(int i=1;i<=N;i++){
			
			if(even){
				if(i<=(N/2)){
					if(i%2==0){
						value=2*i;
					}
					else{
						value=(2*i)-1;
					}
				}
				else{
					if(i%2==0){
						value=(2*(N-i)+2);
					}
					else{
						value=(2*(N-i)+1);
					}
						
				}
			}
			else{
				if(i<=(N/2)){
					if(i%2==0){
						value=2*i;
					}
					else{
						value=(2*i)-1;
					}
				}
				else{
					if(i==((N/2)+1)){
						value=0;
					}
					else{
						if(i%2==0){
							value=(2*(N-i)+2);
						}
						else{
							value=(2*(N-i)+1);
						}
					}				
				}
			}

			weights[i-1]=value;
			sumWeights+=value;
		}
		
	}//end-method
	
	/**
	 * Compute p-values of the test
	 */
	private void computePValues(){

		NormalDistribution normal=new NormalDistribution();
		
		if((values1<=10)&&(values2<=10)){
			
			exactLeftTail=distribution.computeLeftProbability(values2, values1, (int)ST1);
			exactRightTail=distribution.computeRightProbability(values2, values1, (int)ST1);
			
			if(exactLeftTail==DistributionDefinitions.UNDEFINED){
				exactLeftTail=1.0-exactRightTail;
			}
			
			if(exactRightTail==DistributionDefinitions.UNDEFINED){
				exactRightTail=1.0-exactLeftTail;
			}
			exactDoubleTail=Math.min(Math.min(exactLeftTail,exactRightTail)*2.0,1.0);
		}
		
		//Asymptotic

		//compute variance
		double denominator;
		
		denominator=(double)((double)(values1*values2*(values1+values2+1))/(double)12.0);
		
		denominator=Math.sqrt(denominator);
		double numerator;
		
		numerator=ST1-0.5-((double)values1*((double)values2+values1+1)/(double)2.0);
		
		double z;
		
		z=numerator/denominator;
		
		asymptoticRightTail=1.0-normal.getTipifiedProbability(z, false);
		
		numerator=ST1+0.5-((double)values1*((double)values2+values1+1)/(double)2.0);
		z=numerator/denominator;
		
		asymptoticLeftTail=normal.getTipifiedProbability(z, false);
		
		asymptoticDoubleTail=Math.min(Math.min(asymptoticLeftTail,asymptoticRightTail)*2.0,1.0);

	}//end-method

	/**
	 * Get test statistic associated to the first sample
	 * 
	 * @return normal Statistic of the first sample
	 */
	public double getTestStatistic1(){
		
		return ST1;
		
	}//end-method
	
	/**
	 * Get test statistic associated to the second sample
	 * 
	 * @return normal Statistic of the second sample
	 */
	public double getTestStatistic2(){
		
		return ST2;
		
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
	 * Prints the array of weights
	 * 
	 * @return string with the contents of the array
	 */
	public String printWeights(){
		
		String text="";
		
		for(int i=0;i<weights.length;i++){
			text+=weights[i]+" ";
		}
		
		return text;
		
	}//end-method
	
	/**
	 * Prints the data stored in the test
	 * 
	 * @return Data stored
	 */
	public String printData(){
		
		String text="";
		
		text+="\n"+data;
		
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
		report+="Siegel-Tukey test\n";
		report+="**************\n\n";
		
		report+="Siegel-Tukey Statistic X: "+nf6.format(ST1)+"\n";
		report+="Siegel-Tukey Statistic Y: "+nf6.format(ST2)+"\n\n";
		
		report+="Exact P-Value (Left tail): "+nf6.format(exactLeftTail)+"\n";
		report+="Exact P-Value (Right tail): "+nf6.format(exactRightTail)+"\n";
		report+="Exact P-Value (Double tail): "+nf6.format(exactDoubleTail)+"\n\n";
		report+="Asymptotic P-Value (Left tail): "+nf6.format(asymptoticLeftTail)+"\n";
		report+="Asymptotic P-Value (Right tail): "+nf6.format(asymptoticRightTail)+"\n";
		report+="Asymptotic P-Value (Double tail): "+nf6.format(asymptoticDoubleTail)+"\n\n";

		return report;
		
	}//end-method
	
}//end-class
