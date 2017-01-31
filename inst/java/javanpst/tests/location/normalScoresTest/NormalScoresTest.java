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

package javanpst.tests.location.normalScoresTest;

import java.util.Arrays;

import javanpst.data.DataDefinitions;
import javanpst.data.structures.dataTable.DataTable;
import javanpst.distributions.common.continuous.NormalDistribution;
import javanpst.tests.StatisticalTest;

/**
 * The Normal scores test.
 * 
 * The Normal scores test (van der Warden test) uses
 * inverse normal scores to asign ranks to data, before
 * testing the null hypothesis of equal location.
 * 
 * P-value is computed through a normal approximation.
 * 
 * Ties are handled by the midranks method, and an appropriate
 * reduction of variance is considered.
 *  
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class NormalScoresTest extends StatisticalTest{

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
	 * Test statistic for the first sample
	 */
	private double normalStatistic;
	
	/**
	 * Test statistic for the second sample
	 */
	private double normalStatistic2;
	
	/**
	 * Left tail p-value
	 */
	private double asymptoticLeft;
	
	/**
	 * Right tail p-value
	 */
	private double asymptoticRight;
	
	/**
	 * Double tail p-value
	 */
	private double asymptoticDouble;
	
	/**
	 * A normal distribution is used to asign the ranks
	 */
	private NormalDistribution normal;
	
	/**
	 * Default builder
	 */
	public NormalScoresTest(){
		
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
		
		normalStatistic=0.0;
		normalStatistic2=0.0;
		
		sample1=null;
		sample2=null;
		
		asymptoticLeft=-1.0;
		asymptoticRight=-1.0;
		asymptoticDouble=-1.0;
		
	}//end-method
	
	/**
	 * Builder. Load data to test.
	 * 
	 * @param newData data to test.
	 */
	public NormalScoresTest(DataTable newData){
		
		int nulls;
		int counter1, counter2;
		double value;
		
		setReportFormat();
		
		data=DataTable.newInstance(newData);
		
		if(data.getColumns()!=2){
			System.out.println("Normal Scores test only can be employed with two samples");
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
			System.out.println("Normal Scores test only can be employed with two samples");
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
		
		normal=new NormalDistribution();
		ranks= new double [combined.length];
		int nValues=combined.length;
		
		for(int i=0;i<combined.length;i++){
			
			if(!ties[i]){
				ranks[i]=normal.inverseNormalDistribution((double)(i+1)/(double)(nValues+1.0));
			}
			else{
				int j=1;
				sumRank=normal.inverseNormalDistribution((double)(i+1)/(double)(nValues+1.0));
				while(((i+j)<combined.length)&&(ties[i+j]==true)){			
					sumRank+=normal.inverseNormalDistribution((double)(i+1+j)/(double)(nValues+1.0));
					j++;
				}

				for(int k=0;k<j;k++){
					ranks[i]=sumRank/(double)j;

					i++;					
				}
				i--;
			}
		}
		
		
		normalStatistic=0.0;
		normalStatistic2=0.0;
		pointer=0;
		pointer2=0;
		
		while(pointer<sample1.length){
				
			while(combined[pointer2]!=sample1[pointer]){
				pointer2++;
			}
				
			normalStatistic+=ranks[pointer2];
			pointer++;
		}
		
		pointer=0;
		pointer2=0;
		
		while(pointer<sample2.length){
			
			while(combined[pointer2]!=sample2[pointer]){
				pointer2++;
			}

			normalStatistic2+=ranks[pointer2];
			pointer++;
		}
		
		computePValues();

		performed=true;
		
	}//end-method
	
	/**
	 * Compute p-values of the Normal Scores test
	 */
	private void computePValues(){
		
		double variance,z;
		double N=values1+values2;
		double value;
		
		double numerator,denominator;

		numerator=0.0;
		
		for(int i=1;i<=N;i++){
			value=normal.inverseNormalDistribution((double)(i)/(double)(N+1.0));
			numerator+=(value*value);
		}
		
		denominator=N*(N-1.0);
		variance=values1*values2*(numerator/denominator);
		
		z=normalStatistic/Math.sqrt(variance);
		
		asymptoticLeft=normal.getTipifiedProbability(z, false);
		
		z=normalStatistic2/Math.sqrt(variance);
		
		asymptoticRight=normal.getTipifiedProbability(z, false);
		
		asymptoticDouble=2.0*Math.min(asymptoticLeft,asymptoticRight);
		
	}//end-method
	
	/**
	 * Get Normal Statistic of the first sample
	 * 
	 * @return normal Statistic of the first sample
	 */
	public double getNormalStatistic1(){
		
		return normalStatistic;
		
	}//end-method
	
	/**
	 * Get Normal Statistic of the second sample
	 * 
	 * @return normal Statistic of the second sample
	 */
	public double getNormalStatistic2(){
		
		return normalStatistic2;
		
	}//end-method
	
	/**
	 * Get left tail p-value 
	 * 
	 * @return left tail p-value computed
	 */
	public double getLeftPValue(){
		
		return asymptoticLeft;
		
	}//end-method
	
	/**
	 * Get right tail p-value 
	 * 
	 * @return right tail p-value computed
	 */
	public double getRightPValue(){
		
		return asymptoticRight;
		
	}//end-method
	
	/**
	 * Get double tail p-value 
	 * 
	 * @return double tail p-value computed
	 */
	public double getDoublePValue(){
		
		return asymptoticDouble;
		
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
		report+="Normal Scores test (van der Warden)\n";
		report+="*****************************\n\n";
		
		report+="Normal Statistic X: "+nf6.format(normalStatistic)+"\n";
		report+="Normal Statistic Y: "+nf6.format(normalStatistic2)+"\n\n";
		
		report+="Asymptotic P-Value (left tail): "+nf6.format(asymptoticLeft)+"\n";
		report+="Asymptotic P-Value (right tail): "+nf6.format(asymptoticRight)+"\n";
		report+="Asymptotic P-Value (double tail): "+nf6.format(asymptoticDouble)+"\n\n";
		
		return report;
		
	}//end-method
	
}//end-class
