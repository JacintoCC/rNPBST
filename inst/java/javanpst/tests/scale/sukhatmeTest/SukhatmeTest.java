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

package javanpst.tests.scale.sukhatmeTest;

import java.util.Arrays;

import javanpst.data.DataDefinitions;
import javanpst.data.structures.dataTable.DataTable;
import javanpst.distributions.common.continuous.NormalDistribution;
import javanpst.tests.StatisticalTest;

/**
 * The Sukhatme test.
 * 
 * Assuming the scale model (equal medians), this test can be
 * employed to test whether the two distributions selected have 
 * equal scale parameter (with the null hypothesis of equality).
 *  
 * This test assumes that the median of both populations is 0.
 * Hence, samples' values can be arranged into negative and positive ones,
 * to obtain a Mann-Whitney based statistic to test hypothesis of
 * equal scale.
 *
 * An approximation to the Normal distribution is employed to 
 * compute asymptotic p-values. 
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class SukhatmeTest extends StatisticalTest{

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
	 * Array of negative values
	 */
	private int negative [];
	
	/**
	 * Array of positive values
	 */
	private int positive [];
	
	/**
	 * Test statistic
	 */
	private double Tx;

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
	 * Common definitions to asign values to first
	 * sample, second sample or ties
	 */
	private static final int X = 0; 
	private static final int Y = 1; 
	private static final int TIED_X = 2; 
	private static final int TIED_Y = 3; 

	/**
	 * Default builder
	 */
	public SukhatmeTest(){

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
		
		Tx=0.0;
		
		sample1=null;
		sample2=null;
		negative=null;
		positive=null;
		
		asymptoticLeftTail=-1.0;
		asymptoticRightTail=-1.0;
		asymptoticDoubleTail=-1.0;
		
	}//end-method
	
	/**
	 * Builder. Load data to test.
	 * 
	 * @param newData data to test.
	 */
	public SukhatmeTest(DataTable newData){
		
		int nulls;
		int counter1, counter2;
		double value;
		
		setReportFormat();
		
		data=DataTable.newInstance(newData);
		
		if(data.getColumns()!=2){
			System.out.println("SukhatmeTest test only can be employed with two samples");
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
			System.out.println("SukhatmeTest test only can be employed with two samples");
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
		
		int pointer,pointer2,pointer3,countX,countY;
		double auxValue;
		
		if(!dataReady){		
			System.out.println("Data is not ready");
			return;	
		}
		
		Arrays.sort(sample1);
		Arrays.sort(sample2);
		
		countX=0;
		countY=0;
		for(int i=0;i<sample1.length;i++){
			if(sample1[i]<0.0){
				countX++;
			}
		}
		
		for(int i=0;i<sample2.length;i++){
			if(sample2[i]<0.0){
				countY++;
			}
		}
		
		//create arrays of negative and positive samples
		negative=new int [countX+countY];
		positive=new int [(sample1.length-countX)+(sample2.length-countY)];
		
		pointer=0;
		pointer2=0;
		pointer3=0;
		
		//assign samples to positive and negative arrays
		while(((sample1[pointer]<0.0)||(sample2[pointer2]<0.0))&&(pointer<sample1.length)&&(pointer2<sample2.length)){
			
			if(sample1[pointer]<sample2[pointer2]){
				negative[pointer3]=X;
				pointer++;
				pointer3++;
			}else{
				if(sample1[pointer]>sample2[pointer2]){
					negative[pointer3]=Y;
					pointer2++;
					pointer3++;
					
				}else{
					auxValue=sample1[pointer];
					while((sample1[pointer]==auxValue)&&(pointer<sample1.length)){
						negative[pointer3]=TIED_X;
						pointer++;
						pointer3++;
					}
					
					while((sample2[pointer2]==auxValue)&&(pointer2<sample2.length)){
						negative[pointer3]=TIED_Y;
						pointer2++;
						pointer3++;
					}
				}
			}
			
		}
		
		pointer3=0;
		while((pointer3<positive.length)&&(pointer<sample1.length)&&(pointer2<sample2.length)){
			if(sample1[pointer]<sample2[pointer2]){
				positive[pointer3]=X;
				pointer++;
				pointer3++;
			}else{
				if(sample1[pointer]>sample2[pointer2]){
					positive[pointer3]=Y;
					pointer2++;
					pointer3++;
					
				}else{
					auxValue=sample1[pointer];
					while((sample1[pointer]==auxValue)&&(pointer<sample1.length)){
						positive[pointer3]=TIED_X;
						pointer++;
						pointer3++;
					}
					
					while((sample2[pointer2]==auxValue)&&(pointer2<sample2.length)){
						positive[pointer3]=TIED_Y;
						pointer2++;
						pointer3++;
					}
				}
			}
			
		}
		
		while(pointer<sample1.length){
			positive[pointer3]=X;
			pointer++;
			pointer3++;
		}
		
		while(pointer2<sample2.length){
			positive[pointer3]=X;
			pointer2++;
			pointer3++;
		}
		
		//compute test statistic
		Tx=0.0;
		
		//Negative array
		pointer=0;
		for(pointer=0;(pointer<negative.length)&&((negative[pointer]==X));pointer++);
		
		if(pointer<negative.length){
			for(;pointer<negative.length;pointer++){
				if(negative[pointer]==X){
					Tx+=1.0;
				}
				if(negative[pointer]==TIED_X){
					Tx+=0.5;
				}
			}
			
		}
		
		//positive array
		pointer=0;
		for(pointer=positive.length-1;(pointer>-1)&&((positive[pointer]==X));pointer--);
		
		if(pointer>-1){
			for(;pointer>-1;pointer--){
				if(positive[pointer]==X){
					Tx+=1.0;
				}
				if(positive[pointer]==TIED_X){
					Tx+=0.5;
				}
			}
			
		}
		
		computePValues();
		
		performed=true;
		
	}//end-method
	
	/**
	 * Compute p-values of the test
	 */
	private void computePValues(){

		NormalDistribution normal=new NormalDistribution();
		
		double N=values1+values2;
		
		//Asymptotic

		//compute variance
		double denominator;
		
		denominator=values1*values2*(N+7.0);
		
		denominator=Math.sqrt(denominator);
		double numerator;
		
		numerator=4.0*Math.sqrt(3.0)*(0.5+Tx-((double)values1*values2/4.0));
		
		double z;
		
		z=numerator/denominator;
		
		asymptoticRightTail=1.0-normal.getTipifiedProbability(z, false);
		
		asymptoticLeftTail=normal.getTipifiedProbability(z, false);
		
		asymptoticDoubleTail=Math.min(Math.min(asymptoticLeftTail,asymptoticRightTail)*2.0,1.0);

	}//end-method

	/**
	 * Get the test statistic 
	 * 
	 * @return Sukhatme statistic
	 */
	public double getTestStatistic(){
		
		return Tx;
		
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
		
		report+="\n************\n";
		report+="Sukhatme test\n";
		report+="**************\n\n";
		
		report+="Sukhatme Statistic : "+nf6.format(Tx)+"\n\n";
		
		report+="Asymptotic P-Value (Left tail): "+nf6.format(asymptoticLeftTail)+"\n";
		report+="Asymptotic P-Value (Right tail): "+nf6.format(asymptoticRightTail)+"\n";
		report+="Asymptotic P-Value (Double tail): "+nf6.format(asymptoticDoubleTail)+"\n\n";

		return report;
		
	}//end-method
	
}//end-class
