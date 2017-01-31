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

package javanpst.tests.scale.moodTest;

import java.util.Arrays;

import javanpst.data.DataDefinitions;
import javanpst.data.structures.dataTable.DataTable;
import javanpst.distributions.common.continuous.NormalDistribution;
import javanpst.tests.StatisticalTest;

/**
 * The Mood test.
 * 
 * Assuming the scale model (equal medians), this test can be
 * employed to test whether the two distributions selected have 
 * equal scale parameter (with the null hypothesis of equality).
 *  
 * The Mood test ranks elements with the following weights:
 * 
 * (N-1)/2)^2 ; (N-3)/2)^2 ... (1/2)^2 ... (N-3)/2)^2 ; (N-1)/2)^2
 * 
 * An approximation to the Normal distribution is employed to 
 * compute p-values.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class MoodTest extends StatisticalTest{

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
	 * PValue of x sample dispersion
	 */
	private double xPValue;
	
	/**
	 * PValue of y sample dispersion
	 */
	private double yPValue;

	/**
	 * Default builder
	 */
	public MoodTest(){

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
		
		xPValue=-1.0;
		yPValue=-1.0;
		
	}//end-method
	
	/**
	 * Builder. Load data to test.
	 * 
	 * @param newData data to test.
	 */
	public MoodTest(DataTable newData){
		
		int nulls;
		int counter1, counter2;
		double value;
		
		setReportFormat();
		
		data=DataTable.newInstance(newData);
		
		if(data.getColumns()!=2){
			System.out.println("Mood test only can be employed with two samples");
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
			System.out.println("Mood test only can be employed with two samples");
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

		double value,aux;
		boolean even;
		
		sumWeights=0;
		
		if(N%2==0){
			even=true;
		}
		else{
			even=false;
		}
		
		if(even){
			for(int i=1;i<=(N/2);i++){

				aux=(N-((2*i)-1.0))/2.0;
				value=aux*aux;
				
				weights[i-1]=value;
				weights[N-i]=value;
					
				sumWeights+=value;
				sumWeights+=value;
			}	
		}
		else{
			
			for(int i=1;i<=((N-1)/2);i++){

				aux=(N-((2*i)-1.0))/2.0;
				value=aux*aux;
				
				weights[i-1]=value;
				weights[N-i]=value;
					
				sumWeights+=value;
				sumWeights+=value;
			}	
			
			weights[((N+1)/2)-1]=0.0;
			
		}
		
	}//end-method
	
	/**
	 * Compute p-values of the test
	 */
	private void computePValues(){

		NormalDistribution normal;
		
		double denominator,numerator,z;

		normal=new NormalDistribution();
		
		denominator=Math.sqrt(values1*values2*(N+1)*((N*N)-4.0)/180.0);
		
		//X rank
		
		numerator=MNx-(values1*((N*N)-1.0)/12.0);
		
		z=numerator/denominator;
		
		xPValue=normal.getTipifiedProbability(z, false);
		
		//Y rank
		
		numerator=MNy-(values2*((N*N)-1.0)/12.0);
		
		z=numerator/denominator;
		
		yPValue=normal.getTipifiedProbability(z, false);
		
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
	 * Get first sample p-value
	 * 
	 * @return first sample p-value
	 */
	public double getPValue1(){
		
		return xPValue;
		
	}//end-method
	
	/**
	 * Get second sample p-value
	 * 
	 * @return second sample p-value
	 */
	public double getPValue2(){
		
		return yPValue;
		
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
		report+="Mood test\n";
		report+="**************\n\n";
		
		report+="Mood Statistic X: "+nf6.format(MNx)+"\n";
		report+="Mood Statistic Y: "+nf6.format(MNy)+"\n\n";
		
		report+="Asymptotic P-Value (X): "+nf6.format(xPValue)+"\n";
		report+="Asymptotic P-Value (Y): "+nf6.format(yPValue)+"\n\n";

		return report;
		
	}//end-method
	
}//end-class
