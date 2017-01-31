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

package javanpst.tests.twoSample.medianTest;

import java.util.Arrays;

import javanpst.data.DataDefinitions;
import javanpst.data.structures.dataTable.DataTable;
import javanpst.distributions.common.continuous.NormalDistribution;
import javanpst.tests.StatisticalTest;
import javanpst.utils.Operations;

/**
 * The median test
 * 
 * This test can be applied to test the null hypotheses of equal medians 
 * between populations.
 * 
 * Exact p-value is computed through U and V statistics (number of values below 
 * of the combined sample median). An asymptotic approximation to the normal distribution
 * is also computed.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class MedianTest extends StatisticalTest{

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
	 * Combined sample
	 */
	private double combined [];
	
	/**
	 * Length of samples
	 */
	private int n,m;
	
	/**
	 * Index of median
	 */
	private int t;
	
	/**
	 * Index of median
	 */
	private double medianValue;
	
	/**
	 * Number of elements below median value
	 */
	private int u,v;
	
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
	 * Lower limit of confidence interval
	 */
	private double leftConfidence;
	
	/**
	 * Upper limit of confidence interval
	 */
	private double rightConfidence;
	
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
	 * Exact confidence
	 */
	private double confidence;
	
	/**
	 * Default builder
	 */
	public MedianTest(){

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
		
		t=0;
		u=0;
		v=0;
		
		sample1=null;
		sample2=null;
		
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
	public MedianTest(DataTable newData){
		
		int nulls;
		int counter1, counter2;
		double value;
		
		setReportFormat();
		
		data=DataTable.newInstance(newData);
		
		if(data.getColumns()!=2){
			System.out.println("Median test only can be employed with two samples");
			clearData();
			return;
		}
		
		nulls=data.getColumnNulls(0);
		sample1=new double [data.getRows()-nulls];
		nulls=data.getColumnNulls(1);
		sample2=new double [data.getRows()-nulls];
		
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
			System.out.println("Median test only can be employed with two samples");
			clearData();
			return;
		}
		
		nulls=data.getColumnNulls(0);
		sample1=new double [data.getRows()-nulls];
		nulls=data.getColumnNulls(1);
		sample2=new double [data.getRows()-nulls];
		
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

		int medianRank;
		
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
		
		if(combined.length%2==0){
			medianRank=(combined.length)/2;
			medianValue=(combined[medianRank-1]+combined[medianRank])/2.0;
		}else{
			medianRank=(combined.length+1)/2;
			medianValue=combined[medianRank-1];
		}
		
		//compute values
		n=sample2.length;
		m=sample1.length;
		
		if(combined.length%2==0){
			t=combined.length/2;
		}else{
			t=(combined.length-1)/2;
		}
		
		//u statistic based on sample2
		u=0;
		while(sample2[u]<medianValue){
			u++;
		}
		
		//v statistic based on sample1
		v=0;
		while(sample1[v]<medianValue){
			v++;
		}
		
		computeStatistics();
		
		performed=true;
	
	}//end-method
	
	/**
	 * Compute p-values and confidence intervals 
	 */
	private void computeStatistics(){
		
		System.out.println((m+n)+" "+t);
		double denominator= Operations.combinatorial(m+n,t);
		double numerator=0.0;
		double numerator1=0.0;
		double numerator2=0.0;
		
		int firstLimit,secondLimit;
		
		//confidence interval 0.05
		int leftValue=-2,rightValue=-1;
		double leftProb=0.0;
		double rightProb=0.0;
		double acum=0.0;
		confidence=0.0;
		
		firstLimit=Math.max(0,t-n);
		secondLimit=Math.min(m,t);
		
		
		for(int i=firstLimit;i<=secondLimit;i++){
			if(i<=u){
				numerator1+=(Operations.combinatorial(m,i)*Operations.combinatorial(n,t-i));
				leftProb+=(numerator1/denominator);
				if((leftProb> 0.05)&&(leftValue==-2)){
					if(i==0){
						leftValue=-1;
						confidence+=leftProb;
					}
					else{
						leftValue=i-1;
						confidence+=leftProb-(numerator1/denominator);
					}
				}
			}
			if(i>=u){
				numerator2+=(Operations.combinatorial(m,i)*Operations.combinatorial(n,t-i));
			}
			
		}
		
		for(int i=secondLimit;(i>=firstLimit)&&(rightValue==-1);i--){
			acum+=(Operations.combinatorial(m,i)*Operations.combinatorial(n,t-i));
			rightProb+=(acum/denominator);
			if(rightProb> 0.05){
				if(i==secondLimit){
					rightValue=secondLimit;
					confidence+=rightProb;
				}
				else{
					rightValue=i+1;
					confidence+=rightProb-(acum/denominator);
				}
	
			}
		}
		
		confidence=1.0-confidence;
		exactLeftTail=numerator2/denominator;
		exactRightTail=numerator1/denominator;
		exactDoubleTail=Math.min(Math.min(exactLeftTail,exactRightTail)*2.0,1.0);
		
		leftConfidence=(sample2[leftValue]-sample1[rightValue-1]);
		rightConfidence=(sample2[rightValue-1]-sample1[leftValue]);
		
		//asymptotic
		
		NormalDistribution normal=new NormalDistribution();
		
		denominator=Math.sqrt((double)m*n*t*((double)combined.length-t)/(double)((double)combined.length*combined.length*combined.length));
		
		numerator=(double)u+0.5-((double)m*t/(double)combined.length);
		
		asymptoticRightTail=normal.getTipifiedProbability(numerator/denominator, false);
		
		denominator=Math.sqrt((double)m*n*t*((double)combined.length-t)/(double)((double)combined.length*combined.length*combined.length));
		
		numerator=(double)v+0.5-((double)n*t/(double)combined.length);

		asymptoticLeftTail=normal.getTipifiedProbability(numerator/denominator, false);
		asymptoticDoubleTail=Math.min(Math.min(asymptoticLeftTail,asymptoticRightTail)*2.0,1.0);
		
	}//end-method
	
	/**
	 * Get U statistic 
	 * 
	 * @return U statistic 
	 */
	public double getU(){
		
		return u;
		
	}//end-method
	
	/**
	 * Get V statistic 
	 * 
	 * @return V statistic 
	 */
	public double getV(){
		
		return v;
		
	}//end-method
	
	/**
	 * Get median of the combined sample
	 * 
	 * @return median value
	 */
	public double getMedian(){
		
		return medianValue;
		
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
	 * Prints a confidence interval for the median difference
	 * 
	 * @return string with the confidence interval
	 */
	public String printConfidenceInterval(){
		
		String text="";
		
		text+="["+leftConfidence+","+rightConfidence+"] Confidence = "+confidence;
		
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
			
		report+="\n***************************************\n";
		report+="Median test for 2 independent samples\n";
		report+="***************************************\n\n";

		report+="Median value: "+nf6.format(medianValue)+"\n";
		report+="U statistic: "+nf6.format(u)+"\n";
		report+="V statistic: "+nf6.format(v)+"\n\n";
					
		report+="Exact P-Value (Left tail, Y > X): "+nf6.format(exactLeftTail)+"\n";
		report+="Exact P-Value (Right tail, Y < X): "+nf6.format(exactRightTail)+"\n";
		report+="Exact P-Value (Double tail, Y != X): "+nf6.format(exactDoubleTail)+"\n\n";
		report+="Asymptotic P-Value (Left tail, Y > X): "+nf6.format(asymptoticLeftTail)+"\n";
		report+="Asymptotic P-Value (Right tail, Y > X): "+nf6.format(asymptoticRightTail)+"\n";
		report+="Asymptotic P-Value (Double tail, Y != X): "+nf6.format(asymptoticDoubleTail)+"\n\n";
			
		report+="Median diference = ["+leftConfidence+","+rightConfidence+"] Confidence = "+nf6.format(confidence)+"\n";

		return report;
			
	}//end-method
		
}//end-class
