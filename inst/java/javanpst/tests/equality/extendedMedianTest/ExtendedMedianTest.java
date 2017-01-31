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

package javanpst.tests.equality.extendedMedianTest;

import java.util.Arrays;

import javanpst.data.structures.dataTable.DataTable;
import javanpst.distributions.common.continuous.ChiSquareDistribution;
import javanpst.tests.StatisticalTest;

/**
 * A extension of the median test for k independent samples
 * 
 * This test can be applied to test the null hypotheses of equal medians 
 * between populations. P-values are computed through a chi-square distribution
 * approximation.
 * 
 * All samples must have the same number of elements.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class ExtendedMedianTest extends StatisticalTest{

	/**
	 * Data to analyze
	 */
	private DataTable data;
	
	/**
	 * Populations samples
	 */
	private double samples [][];
	
	/**
	 * Total number of elements
	 */
	private double N;
	
	/**
	 * Position of the samples' values with respect to median
	 */
	private double position [][];
	
	/**
	 * Median value of the samples
	 */
	private double median;
	
	/**
	 * Array with all the values of the combined sample
	 */
	private double combined[];
	
	/**
	 * Q statistic
	 */
	private double Q;
	
	/**
	 * Q statistic improved to approximate to a chi-square distribution
	 */
	private double improvedQ;
	
	/**
	 * P-value computed
	 */
	private double pValue;
	
	/**
	 * Default builder
	 */
	public ExtendedMedianTest(){

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
		
		Q=0.0;
		improvedQ=0.0;
		median=0;
		
		samples=null;
		position=null;
		
		pValue=-1.0;
		
	}//end-method
	
	/**
	 * Builder. Load data to test.
	 * 
	 * @param newData data to test.
	 */
	public ExtendedMedianTest(DataTable newData){
		
		setReportFormat();
		
		data=DataTable.newInstance(newData);
		
		if(data.getColumns()<3){
			System.out.println("Extended median test only can be employed with more than two samples");
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
		
		samples=new double[data.getRows()][data.getColumns()];
		combined=new double[data.getRows()*data.getColumns()];
		position=new double[2][data.getColumns()];
		Arrays.fill(position[0], 0.0);
		Arrays.fill(position[1], 0.0);
		
		for(int i=0;i<data.getRows();i++){
			
			for(int j=0;j<data.getColumns();j++){
				samples[i][j]=data.get(i, j);
				combined[(i*data.getColumns())+j]=data.get(i, j);
			}
			
		}

		Arrays.sort(combined);
		
		dataReady=true;
		performed=false;
		
	}//end-method
	
	/**
	 * Sets data to test
	 * 
	 * @param newData data to test.
	 */
	public void setData(DataTable newData){
		
		data=DataTable.newInstance(newData);
		
		if(data.getColumns()<3){
			System.out.println("Extended median test only can be employed with more than two samples");
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
		
		samples=new double[data.getRows()][data.getColumns()];
		combined=new double[data.getRows()*data.getColumns()];
		position=new double[2][data.getColumns()];
		Arrays.fill(position[0], 0.0);
		Arrays.fill(position[1], 0.0);
		
		for(int i=0;i<data.getRows();i++){
			
			for(int j=0;j<data.getColumns();j++){
				samples[i][j]=data.get(i, j);
				combined[(i*data.getColumns())+j]=data.get(i, j);
			}
			
		}

		Arrays.sort(combined);
		
		dataReady=true;
		performed=false;
		
	}//end-method

	/**
	 * Performs the test
	 */
	public void doTest(){
		
		int index;
		double acum,aux;
		double t;
		int dF;
		
		if(!dataReady){		
			System.out.println("Data is not ready");
			return;	
		}
		
		N=data.getRows()*data.getColumns();
		
		if(N%2==0){
			t=N/2.0;
		}else{
			t=(N-1)/2.0;
		}
		
		if(N%2==0){
			index=(int)((N-1)/2.0);
			median=combined[index];
			median+=combined[index+1];
			median/=2.0;
		}else{
			median=combined[(int)((N-1)/2.0)];
		}
		
		//position
		for(int j=0;j<samples[0].length;j++){
			for(int i=0;i<samples.length;i++){
				if(samples[i][j]<median){
					position[0][j]+=1.0;
				}
				else{
					position[1][j]+=1.0;
				}
			}
		}
		
		acum=0.0;
		for(int j=0;j<samples[0].length;j++){
			aux=((position[0][j])-(samples.length*t/N));
			aux=aux*aux/samples.length;
			acum+=aux;
		}
		
		Q=(N*N)/(t*(N-t))*acum;
		
		improvedQ=(N-1.0)*Q/N;
		dF=samples[0].length-1;
		
		computePValues(dF);
		
		performed=true;
		
	}//end-method
	
	/**
	 * Compute p-value for hypotheses of equality of medians
	 */
	private void computePValues(int dF){
		
		ChiSquareDistribution chi=new ChiSquareDistribution();
		
		chi.setDegree(dF);
		
		pValue=chi.computeCumulativeProbability(improvedQ);
		
	}//end-method
	
	/**
	 * Get Q statistic 
	 * 
	 * @return Q statistic 
	 */
	public double getQ(){
		
		return Q;
		
	}//end-method
	
	/**
	 * Get improved Q statistic 
	 * 
	 * @return improved Q statistic 
	 */
	public double getImprovedQ(){
		
		return improvedQ;
		
	}//end-method
	
	/**
	 * Get median value
	 * 
	 * @return median value
	 */
	public double getMedian(){
		
		return median;
		
	}//end-method
	
	/**
	 * Get p-value 
	 * 
	 * @return p-value computed
	 */
	public double getPValue(){
		
		return pValue;
		
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
		report+="Extended median test for k independent samples\n";
		report+="***************************************\n\n";

		report+="median value: "+nf6.format(median)+"\n";
		report+="Q statistic: "+nf6.format(Q)+"\n";
		report+="Improved Q statistic: "+nf6.format(improvedQ)+"\n\n";
		
		report+="P-Value computed :"+nf6.format(pValue)+"\n\n";
					
		report+="Position with respect to median\n\n"; 
			
		//Header
		for(int j=0;j<position[0].length;j++){
			report+="S"+(j+1)+"\t";
		}
		report+="\n";
			
		//First row
		for(int j=0;j<position[0].length;j++){
			report+=position[0][j]+"\t";
		}
		report+="\n";
			
		//Second row
		for(int j=0;j<position[1].length;j++){
			report+=position[1][j]+"\t";
		}
			
		report+="\n\n";
			
		return report;
			
	}//end-method
		
}//end-class
