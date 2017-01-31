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

package javanpst.tests.scale.david_BartonTest;

import java.util.Arrays;

import javanpst.data.DataDefinitions;
import javanpst.data.structures.dataTable.DataTable;
import javanpst.distributions.common.continuous.NormalDistribution;
import javanpst.tests.StatisticalTest;

/**
 * The David-Barton test.
 * 
 * Assuming the scale model (equal medians), this test can be
 * employed to test whether the two distributions selected have 
 * equal scale parameter (with the null hypothesis of equality).
 *  
 * The David-Barton test ranks elements with the following weights:
 * 
 *  N/2 ... 3,2,1,1,2,3 ... N/2 for N even
 *  N-1/2 ... 3,2,1,0,1,2,3 ... N-1/2 for N odd
 *
 * An approximation to the Normal distribution is employed to 
 * compute p-values.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class David_BartonTest extends StatisticalTest{

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
	private double weights [];
	
	/**
	 * Sum of weights
	 */
	private double sumWeights;
	
	/**
	 * Test statistics
	 */
	private double MNx,MNy;
	
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
	public David_BartonTest(){
		
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
		
		MNx=0.0;
		MNy=0.0;
		
		sample1=null;
		sample2=null;
		weights=null;
		
		asymptoticLeftTail=-1.0;
		asymptoticRightTail=-1.0;
		asymptoticDoubleTail=-1.0;
		
	}//end-method
	
	/**
	 * Builder. Load data to test.
	 * 
	 * @param newData data to test.
	 */
	public David_BartonTest(DataTable newData){
		
		int nulls;
		int counter1, counter2;
		double value;
		
		setReportFormat();
		
		data=DataTable.newInstance(newData);
		
		if(data.getColumns()!=2){
			System.out.println("David-Barton test only can be employed with two samples");
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
			System.out.println("David-Barton test only can be employed with two samples");
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
		weights= new double [N];
		
		generateWeights();

		MNx=0;
		MNy=0;
		pointer=0;
		pointer2=0;
			
		while(pointer<sample1.length){
				
			while(combined[pointer2]!=sample1[pointer]){
				pointer2++;
			}
				
			MNx+=weights[pointer2];
			pointer++;
		}
		MNy=sumWeights-MNx;
			
		computePValues();
		
		performed=true;

	}//end-method
	
	/**
	 * Generate weights array
	 */
	private void generateWeights(){

		double value;
	
		sumWeights=0;
		
		for(int i=1; i<=(N+1)/2; i++){
			
			value=((N+2)/2)-i;
			weights[i-1]=value;
			sumWeights+=value;
		}
		
		for(int i=((N+1)/2)+1; i<=N; i++){
			
			value=i-((N+1)/2);
			weights[i-1]=value;
			sumWeights+=value;
		}
			
	}//end-method
	
	/**
	 * Compute p-values of the test
	 */
	private void computePValues(){

		double denominator,numerator,z;
		boolean even;
		double a,b;
		double m1=values1;
		double m2=values2;

		NormalDistribution normal=new NormalDistribution();
		
		if(N%2==0){
			even=true;
		}
		else{
			even=false;
		}
		
		if(even){
			
			a=m1*m2*((N*N)-4.0);
			b=48.0*(N-1.0);
			
			denominator=Math.sqrt(a/b);

			numerator=MNx-(m1*(N+2.0)/4.0);
			
			z=numerator/denominator;

		}
		else{
			
			a=m1*m2*(m2+1.0)*((N*N)+3.0);
			b=48.0*(N*N);
			
			denominator=Math.sqrt(a/b);

			numerator=MNx-(m1*((N*N)-1.0)/(4.0*N));

			z=numerator/denominator;
		}
		
		asymptoticLeftTail=normal.getTipifiedProbability(z, true);
		asymptoticRightTail=normal.getTipifiedProbability(z, false);
		
		asymptoticDoubleTail=2.0*Math.min(asymptoticLeftTail,asymptoticRightTail);
		
	}//end-method

	/**
	 * Get test statistic associated to the first sample
	 * 
	 * @return normal Statistic of the first sample
	 */
	public double getTestStatistic1(){
		
		return MNx;
		
	}//end-method
	
	/**
	 * Get test statistic associated to the second sample
	 * 
	 * @return normal Statistic of the second sample
	 */
	public double getTestStatistic2(){
		
		return MNy;
		
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
		report+="David-Barton test\n";
		report+="**************\n\n";
		
		report+="David-Barton Statistic X: "+nf6.format(MNx)+"\n";
		report+="David-Barton Statistic Y: "+nf6.format(MNy)+"\n\n";
		
		report+="Asymptotic P-Value (Left tail): "+nf6.format(asymptoticLeftTail)+"\n";
		report+="Asymptotic P-Value (Right tail): "+nf6.format(asymptoticRightTail)+"\n";
		report+="Asymptotic P-Value (Double tail): "+nf6.format(asymptoticDoubleTail)+"\n\n";

		return report;
		
	}//end-method
	
}//end-class
