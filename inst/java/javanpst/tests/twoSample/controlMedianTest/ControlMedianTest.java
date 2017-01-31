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

package javanpst.tests.twoSample.controlMedianTest;

import java.util.Arrays;

import javanpst.data.DataDefinitions;
import javanpst.data.structures.dataTable.DataTable;
import javanpst.distributions.common.continuous.NormalDistribution;
import javanpst.tests.StatisticalTest;
import javanpst.utils.Operations;

/**
 * The control median test
 * 
 * This test can be applied to test the null hypotheses of equal medians 
 * between populations. P-values are computed through a chi-square distribution
 * approximation.
 * 
 * Exact p-value is computed through U and V statistics (number of values below 
 * of the opposite sample median). An asymptotic approximation to the normal distribution
 * is also computed.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class ControlMedianTest extends StatisticalTest{

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
	 * Length of samples
	 */
	private int n,m;
	
	/**
	 * Index of median
	 */
	private int r;
	
	/**
	 * Index of median
	 */
	private double medianValue,medianValue2;
	
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
	public ControlMedianTest(){

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
		
		r=0;
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
	public ControlMedianTest(DataTable newData){
		
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

		
		for(int i=0;i<data.getColumns();i++){
			if(data.getColumnNulls(i)>0){
				System.out.println("No null values allowed in this test.");
				clearData();
				return;
			}
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
		
		Arrays.sort(sample1);
		Arrays.sort(sample2);
		
		//compute median of sample 2	
		if(sample2.length%2==0){
			medianRank=(sample2.length)/2;
			medianValue2=(sample2[medianRank-1]+sample2[medianRank])/2.0;
			r=sample2.length/2;
		}else{
			medianRank=(sample2.length+1)/2;
			medianValue2=sample2[medianRank-1];
			r=(sample2.length-1)/2;
		}
		
		//compute median of sample 1	
		if(sample1.length%2==0){
			medianRank=(sample1.length)/2;
			medianValue=(sample1[medianRank-1]+sample1[medianRank])/2.0;
		}else{
			medianRank=(sample1.length+1)/2;
			medianValue=sample1[medianRank-1];
		}
		
		//compute values
		n=sample2.length;
		m=sample1.length;
				
		//u statistic based on sample 2
		u=0;
		while(sample1[u]<medianValue){
			u++;
		}
		
		//v statistic based on sample 1
		v=0;
		while(sample1[v]<medianValue2){
			v++;
		}

		computeStatistics();
		
		performed=true;
		
	}//end-method
	
	/**
	 * Compute p-values
	 */
	private void computeStatistics(){
		
		double denominator = Operations.combinatorial(m+(2*r)+1,m);
		double numerator=0.0;
		double numerator2=0.0;
		
		for(int i=0;i<=v;i++){
			numerator+=Operations.combinatorial(m+r-i,m-i)*Operations.combinatorial(i+r,i);
		}
		
		exactLeftTail=numerator/denominator;
		
		for(int i=0;i>=v;i++){
			numerator2+=Operations.combinatorial(m+r-i,m-i)*Operations.combinatorial(i+r,i);
		}
		
		exactRightTail=numerator/denominator;
		exactDoubleTail=Math.min(Math.min(exactLeftTail,exactRightTail)*2.0,1.0);
		
		//asymptotic p-values
		NormalDistribution normal=new NormalDistribution();
		
		denominator=Math.sqrt((double)m*(m+n));
		
		numerator=Math.sqrt((double)n)*((2*v)-m+1);
		
		asymptoticLeftTail=normal.getTipifiedProbability(numerator/denominator, false);
		
		numerator=Math.sqrt((double)n)*((2*u)-m+1);
		
		asymptoticRightTail=normal.getTipifiedProbability(numerator/denominator, false);
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
	 * Get median of the sample 1
	 * 
	 * @return median value
	 */
	public double getMedian(){
		
		return medianValue;
		
	}//end-method
	
	/**
	 * Get median of the sample 2
	 * 
	 * @return median value
	 */
	public double getMedian2(){
		
		return medianValue2;
		
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
		report+="Control Median test for 2 independent samples\n";
		report+="***************************************\n\n";

		report+="Median value of sample 1: "+nf6.format(medianValue)+"\n";
		report+="Median value of sample 2: "+nf6.format(medianValue)+"\n";
		report+="U statistic: "+nf6.format(u)+"\n";
		report+="V statistic: "+nf6.format(v)+"\n\n";
					
		report+="Exact P-Value (Left tail, Y > X): "+nf6.format(exactLeftTail)+"\n";
		report+="Exact P-Value (Right tail, Y < X): "+nf6.format(exactRightTail)+"\n";
		report+="Exact P-Value (Double tail, Y != X): "+nf6.format(exactDoubleTail)+"\n\n";
		report+="Asymptotic P-Value (Left tail, Y > X): "+nf6.format(asymptoticLeftTail)+"\n";
		report+="Asymptotic P-Value (Right tail, Y > X): "+nf6.format(asymptoticRightTail)+"\n";
		report+="Asymptotic P-Value (Double tail, Y != X): "+nf6.format(asymptoticDoubleTail)+"\n\n";

		return report;
			
	}//end-method
		
}//end-class
