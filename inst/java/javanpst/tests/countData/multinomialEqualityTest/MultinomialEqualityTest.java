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

package javanpst.tests.countData.multinomialEqualityTest;

import java.util.Arrays;

import javanpst.data.structures.dataTable.DataTable;
import javanpst.distributions.common.continuous.ChiSquareDistribution;
import javanpst.tests.StatisticalTest;

/**
 * A mutinomial equality test
 * 
 * This test can be applied in 2 x K tables with the aim of 
 * testing if column probabilities are the same, that is, if 
 * p11=p12, p21 = p22, .. , p1k = p2k. Alternative is that
 * probabilities are not the same (non-directional).
 * 
 * A chi-square approximation of the Q statistic is provided
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class MultinomialEqualityTest extends StatisticalTest{

	/**
	 * Data to analyze
	 */
	private DataTable data;
	
	/**
	 * Multinomial samples
	 */
	private double samples [][];
	
	/**
	 * Total number of elements
	 */
	private double N;
	
	/**
	 * Q statistic
	 */
	private double Q;
	
	/**
	 * P-value of the test
	 */
	private double pValue;
	
	/**
	 * Default builder
	 */
	public MultinomialEqualityTest(){

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
		
		samples=null;
		
		pValue=-1.0;
		
	}//end-method
	
	/**
	 * Builder. Load data to test.
	 * 
	 * @param newData data to test.
	 */
	public MultinomialEqualityTest(DataTable newData){
		
		setReportFormat();
		
		data=DataTable.newInstance(newData);
		
		if((data.getRows()>2)||(data.getRows()<2)){
			System.out.println("Data must be represented in two rows");
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
		
		for(int i=0;i<data.getRows();i++){
			for(int j=0;j<data.getColumns();j++){
				samples[i][j]=data.get(i, j);
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
		
		data=DataTable.newInstance(newData);
		
		if((data.getRows()>2)||(data.getRows()<2)){
			System.out.println("Data must be represented in two rows");
			clearData();
			return;
		}
		
		for(int i=0;i<newData.getColumns();i++){
			if(newData.getRowNulls(i)>0){
				System.out.println("No null values allowed in this test.");
				clearData();
				return;
			}
		}
		
		samples=new double[data.getRows()][data.getColumns()];
		
		for(int i=0;i<data.getRows();i++){
			for(int j=0;j<data.getColumns();j++){
				samples[i][j]=data.get(i, j);
			}
			
		}
		
		dataReady=true;
		performed=false;
		
		
	}//end-method
	
	/**
	 * Perform the test
	 */
	public void doTest(){
		
		double expected[][];
		double sumColumns[];
		double sum1,sum2;
			
		if(!dataReady){
			System.out.println("Data is not ready");
			return;	
		}
		
		sumColumns=new double [data.getColumns()];
		
		Arrays.fill(sumColumns, 0.0);
		sum1=0;
		sum2=0;
		N=0;
		
		for(int j=0;j<data.getColumns();j++){
			sumColumns[j]+=samples[0][j];
			sumColumns[j]+=samples[1][j];	
			
			N+=samples[0][j];
			N+=samples[1][j];
			
			sum1+=samples[0][j];
			sum2+=samples[1][j];
		}
		
		
		expected=new double [2][data.getColumns()];
		
		for(int j=0;j<data.getColumns();j++){
			expected[0][j]=sumColumns[j]*sum1/N;
			expected[1][j]=sumColumns[j]*sum2/N;
		}
		
		//Q statistic computation
		Q=0;
		
		for(int j=0;j<data.getColumns();j++){
			Q+=((samples[0][j]-expected[0][j])*(samples[0][j]-expected[0][j])/expected[0][j]);
			Q+=((samples[1][j]-expected[1][j])*(samples[1][j]-expected[1][j])/expected[1][j]);
		}
		
		//adjust to a chi-square distribution
		int dF=data.getColumns()-1;
		
		pValue=computePValue(dF);
		
		performed=true;
		
	}//end-method
	
	/**
	 * Compute p-value of equal probability between columns.
	 * 
	 * @param dF degrees of freedom
	 * @return p-value computed
	 */
	private double computePValue(int dF){
		
		ChiSquareDistribution chi=new ChiSquareDistribution();
		
		chi.setDegree(dF);
		pValue=chi.computeCumulativeProbability(Q);

		return pValue;
		
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
		
		report+="\n*************\n";
		report+="Multinomial equality test\n";
		report+="*************\n\n";

		report+="Q statistic: "+nf6.format(Q)+"\n";
		report+="Asymptotic P-Value: "+nf6.format(pValue)+"\n\n";
				
		report+="Contingency table\n\n"; 
		
		//First row
		for(int j=0;j<samples[0].length;j++){
			report+=samples[0][j]+"\t";
		}
		report+="\n";
		
		//Second row
		for(int j=0;j<samples[1].length;j++){
			report+=samples[1][j]+"\t";
		}
		report+="\n\n";
		
		return report;
		
	}//end-method
	
}//end-class
