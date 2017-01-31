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

package javanpst.tests.location.wilcoxonRankSumTest;

import java.util.Arrays;

import javanpst.data.DataDefinitions;
import javanpst.data.structures.dataTable.DataTable;
import javanpst.distributions.DistributionDefinitions;
import javanpst.distributions.common.continuous.NormalDistribution;
import javanpst.distributions.tests.WilcoxonRankSumDistribution;
import javanpst.tests.StatisticalTest;

/**
 * The Wilcoxon Ranks-Sum test.
 * 
 * This test can be used for testing the null hypothesis 
 * of equal medians between two samples, assuming the location
 * model.
 * 
 * An exact and a normal approximation of p-values is provided.
 * (exact p-values are only computes if samples' size is equal or 
 * lower than 10). Moreover, confidence intervals for the 
 * differences of medians (at 0.90 and 0.95 level) are computed.
 * 
 * Asymptotic P-value is computed through a normal approximation.
 * 
 * Ties are handled by the midranks method, and an appropriate
 * reduction of variance is considered.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class WilcoxonRankSumTest extends StatisticalTest{

	/**
	 * Exact distribution of the test
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
	 * Ties array
	 */
	private boolean ties [];
	
	/**
	 * Wilcoxon statistic
	 */
	private double WRank;
	
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
	 * Confidence intervals at 0.90 and 0.95 levels
	 */
	String confidenceIntervals95, confidenceIntervals90;
	
	/**
	 * Exact confidence for both intervals
	 */
	double exactConfidence90,exactConfidence95;

	/**
	 * Default builder
	 */
	public WilcoxonRankSumTest(){

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
		
		WRank=0.0;
		
		sample1=null;
		sample2=null;
		
		asymptoticLeftTail=-1.0;
		asymptoticRightTail=-1.0;
		asymptoticDoubleTail=-1.0;
		exactLeftTail=-1.0;
		exactRightTail=-1.0;
		exactDoubleTail=-1.0;
		
		confidenceIntervals95="";
		confidenceIntervals90="";
		
		exactConfidence90=-1.0;
		exactConfidence95=-1.0;
		
	}//end-method
	
	/**
	 * Builder. Load data to test.
	 * 
	 * @param newData data to test.
	 */
	public WilcoxonRankSumTest(DataTable newData){
		
		int nulls;
		int counter1, counter2;
		double value;
		
		distribution=WilcoxonRankSumDistribution.getInstance();
		setReportFormat();
		
		data=DataTable.newInstance(newData);
		
		if(data.getColumns()!=2){
			System.out.println("Wilcoxon Ranks-Sum test only can be employed with two samples");
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
			System.out.println("Wilcoxon Ranks-Sum test only can be employed with two samples");
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
	 * Perform the test
	 */		
	public void doTest(){
		
		double val;
		double sumRank;
		
		double ranks[];
		int pointer, pointer2;
		
		if(!dataReady){		
			System.out.println("Data is not ready");
			return;	
		}
		
		combined= new double[sample1.length+sample2.length];
		Arrays.sort(sample1);
		Arrays.sort(sample2);
		
		System.arraycopy(sample1, 0, combined, 0, sample1.length);
		System.arraycopy(sample2, 0, combined, sample1.length, sample2.length);
		
		//compute median
		Arrays.sort(combined);
		
		//mark ties
		ties= new boolean [combined.length];
		Arrays.fill(ties, false);
		
		val=combined[0];
		
		if(combined[1]==val){
			ties[0]=true;
		}
		
		for(int i=1;i<combined.length;i++){
			
			if(combined[i]==val){
				ties[i]=true;
				ties[i-1]=true;
			}
			val=combined[i];
			
		}
		
		//compute ranks
		ranks= new double [combined.length];
		
		for(int i=0;i<combined.length;i++){
			
			if(!ties[i]){
				ranks[i]=i+1;

			}
			else{
				int j=1;
				sumRank=i+1;
				while(((i+j)<combined.length)&&(ties[i+j]==true)){			
					sumRank+=i+1+j;
					j++;
				}

				for(int k=0;k<j;k++){
					ranks[i]=sumRank/(double)j;

					i++;					
				}
				i--;
			}
		}
		
		WRank=0.0;
		pointer=0;
		pointer2=0;
		
		//compute statistic
		if(values1<=values2){
			
			while(pointer<sample1.length){
				
				while(combined[pointer2]!=sample1[pointer]){
					pointer2++;
				}
				
				WRank+=ranks[pointer2];
				pointer++;
			}
			computePValues();
		}
		else{
			while(pointer<sample2.length){
				
				while(combined[pointer2]!=sample2[pointer]){
					pointer2++;
				}

				WRank+=ranks[pointer2];
				pointer++;
			}
			computePValues();
		}
		
		performed=true;

	}//end-method
	
	/**
	 * Compute p-values of the Wilcoxon Ranks-Sum test
	 */
	private void computePValues(){
		
		int rank;
		int criticalN;
		double differences[];
		
		NormalDistribution normal=new NormalDistribution();
		
		if((values1<=10)&&(values2<=10)){
			rank=(int)WRank;
			
			exactLeftTail=distribution.computeLeftProbability(values2, values1, rank);
			exactRightTail=distribution.computeRightProbability(values2, values1, rank);
			
			if(exactLeftTail==DistributionDefinitions.UNDEFINED){
				exactLeftTail=1.0-exactRightTail;
			}
			
			if(exactRightTail==DistributionDefinitions.UNDEFINED){
				exactRightTail=1.0-exactLeftTail;
			}
			exactDoubleTail=Math.min(Math.min(exactLeftTail,exactRightTail)*2.0,1.0);
		}
		
		//Asymptotic
		
		//compute reduction of variance due to ties
		int pointer=0;
		int sumTies=0;
		int actualTie=0;
		
		while(pointer<ties.length){
			
			if(ties[pointer]){
				actualTie++;
			}
			else{
				if(actualTie>0){
					sumTies+=actualTie*((actualTie*actualTie)-1);
					actualTie=0;
				}
			}
			pointer++;
		}
		if(ties[pointer-1]){
			sumTies+=actualTie*((actualTie*actualTie)-1);
		}
		
		//compute variance
		double denominator;
		
		denominator=(double)((double)(values1*values2*(values1+values2+1))/(double)12.0);
		denominator-=(double)((double)(values1*values2*sumTies)/(double)(12.0*(values1+values2)*(values1+values2-1)));
		
		denominator=Math.sqrt(denominator);
		double numerator;
		
		numerator=WRank-0.5-((double)values1*((double)values1+values2+1)/(double)2.0);
		
		double z;
		
		z=numerator/denominator;
		
		asymptoticRightTail=1.0-normal.getTipifiedProbability(z, false);
		
		numerator=WRank+0.5-((double)values1*((double)values2+values1+1)/(double)2.0);
		z=numerator/denominator;
		
		asymptoticLeftTail=normal.getTipifiedProbability(z, false);
		
		asymptoticDoubleTail=Math.min(Math.min(asymptoticLeftTail,asymptoticRightTail)*2.0,1.0);
	
		//confidence interval
		
		criticalN=distribution.findCriticalValue(values1,values2,0.1);
		criticalN=Math.max(criticalN,0);

		differences=new double [sample1.length*sample2.length];
		
		pointer=0;
		
		for(int i=0;i<sample1.length;i++){
			for(int j=0;j<sample2.length;j++){
				differences[pointer]=sample2[j]-sample1[i];
				pointer++;
			}
		}
		
		Arrays.sort(differences);
		
		confidenceIntervals90="["+nf6.format(differences[criticalN-1])+","+nf6.format(differences[differences.length-criticalN])+"]";
		exactConfidence90=1.0-(distribution.inverseFindCriticalValue(values1, values2, criticalN));
		
		criticalN=distribution.findCriticalValue(values1,values2,0.05);
		criticalN=Math.max(criticalN,0);
		
		confidenceIntervals95="["+nf6.format(differences[criticalN-1])+","+nf6.format(differences[differences.length-criticalN])+"]";
		exactConfidence95=1.0-(distribution.inverseFindCriticalValue(values1, values2, criticalN));
		
	}//end-method
	
	/**
	 * Get Wilcoxon Statistic 
	 * 
	 * @return Wilcoxon Statistic 
	 */
	public double getStatistic1(){
		
		return WRank;
		
	}//end-method
	
	/**
	 * Get exact left tail p-value 
	 * 
	 * @return left tail p-value computed
	 */
	public double getExactLeftPValue(){
		
		return exactLeftTail;
		
	}//end-method
	
	/**
	 * Get exact right tail p-value 
	 * 
	 * @return right tail p-value computed
	 */
	public double getExactRightPValue(){
		
		return exactRightTail;
		
	}//end-method
	
	/**
	 * Get exact double tail p-value 
	 * 
	 * @return double tail p-value computed
	 */
	public double getExactDoublePValue(){
		
		return exactDoubleTail;
		
	}//end-method
	
	/**
	 * Get asymptotic left tail p-value 
	 * 
	 * @return left tail p-value computed
	 */
	public double getAsymptoticLeftPValue(){
		
		return asymptoticLeftTail;
		
	}//end-method
	
	/**
	 * Get asymptotic right tail p-value 
	 * 
	 * @return right tail p-value computed
	 */
	public double getAsymptoticRightPValue(){
		
		return asymptoticRightTail;
		
	}//end-method
	
	/**
	 * Get exact confidence of the 0.90 confidence interval
	 * 
	 * @return double tail p-value computed
	 */
	public double getExactConfidence90(){
		
		return exactConfidence90;
		
	}//end-method
	
	/**
	 * Get exact confidence of the 0.95 confidence interval
	 * 
	 * @return double tail p-value computed
	 */
	public double getExactConfidence95(){
		
		return exactConfidence95;
		
	}//end-method
	
	/**
	 * Prints the confidence interval for median 
	 * difference at 0.90 level.
	 * 
	 * @return Data stored
	 */
	public String printConfidenceInterval90(){
		
		return confidenceIntervals90;
		
	}//end-method
	
	/**
	 * Prints the confidence interval for median 
	 * difference at 0.95 level.
	 * 
	 * @return Data stored
	 */
	public String printConfidenceInterval95(){
		
		return confidenceIntervals95;
		
	}//end-method
	
	/**
	 * Get asymptotic double tail p-value 
	 * 
	 * @return double tail p-value computed
	 */
	public double getAsymptoticDoublePValue(){
		
		return asymptoticDoubleTail;
		
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
		
		report+="\n*****************************\n";
		report+="Wilcoxon Ranks-Sum test\n";
		report+="*****************************\n\n";
		
		report+="Wilcoxon Statistic X: "+nf6.format(WRank)+"\n\n";
		
		if((values1<=10)&&(values2<=10)){
			
			report+="Exact P-Value (left tail): "+nf6.format(exactLeftTail)+"\n";
			report+="Exact P-Value (right tail): "+nf6.format(exactRightTail)+"\n";
			report+="Exact P-Value (double tail): "+nf6.format(exactDoubleTail)+"\n\n";
			report+="Confidence interval for median difference (Alpha: 0.90): "+confidenceIntervals90+" Exact confidence: "+nf6.format(exactConfidence90)+"\n";
			report+="Confidence interval for median difference (Alpha: 0.95): "+confidenceIntervals95+" Exact confidence: "+nf6.format(exactConfidence95)+"\n\n";

		}
		else{
			report+="Using normal approximation for more than 10 values for sample 1 or 2\n";			
		}
		
		report+="Asymptotic P-Value (left tail): "+nf6.format(asymptoticLeftTail)+"\n";
		report+="Asymptotic P-Value (right tail): "+nf6.format(asymptoticRightTail)+"\n";
		report+="Asymptotic P-Value (double tail): "+nf6.format(asymptoticDoubleTail)+"\n\n";

		return report;
		
	}//end-method
	
}//end-class

