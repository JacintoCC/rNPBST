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

package javanpst.tests.equality.CDTest;

import javanpst.data.structures.dataTable.DataTable;
import javanpst.distributions.tests.CDDistribution;
import javanpst.tests.StatisticalTest;
import java.util.Arrays;

/**
 * The Charkraborti and Desu test (CD test)
 * 
 * This test can be applied to test whether the median of several
 * populations are equal or greater than the median of a control.
 * The null hypotheses states equal medians, whereas alternative 
 * means that at least one median is greater than the control one. 
 * 
 * 
 * P-values are computed through the exact distribution of the test.
 * A normal distribution approximation, with continuity correction,
 * is also provided. 
 * 
 * All samples must have the same number of elements.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class CDTest extends StatisticalTest{

	/**
	 * Distribution of the test
	 */
	private CDDistribution distribution;
	
	/**
	 * Data to analyze
	 */
	private DataTable data;
	
	/**
	 * Populations samples
	 */
	private double samples [][];
	
	/**
	 * Median of the control sample
	 */
	private double median;
	
	/**
	 * Control sample
	 */
	private double control [];
	
	/**
	 * W statistic
	 */
	private double W;
	
	/**
	 * Asymptotic p-value
	 */
	private double asymptoticPValue;
	
	/**
	 * Exact p-value
	 */
	private double exactPValue;

	/**
	 * Default builder
	 */
	public CDTest(){

		distribution=CDDistribution.getInstance();
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
		
		W=0.0;
		median=0.0;
		
		samples=null;

		asymptoticPValue=-1.0;
		exactPValue=-1.0;
		
	}//end-method
	
	/**
	 * Builder. Load data to test.
	 * 
	 * @param newData data to test.
	 */
	public CDTest(DataTable newData){
		
		distribution=CDDistribution.getInstance();
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
		
		for(int i=0;i<data.getRows();i++){
			
			for(int j=0;j<data.getColumns();j++){
				samples[i][j]=data.get(i, j);
			}
			
		}
		
		dataReady=true;
		performed=false;
		
	}//end-method
	
	/**
	 * Performs the test
	 */
	public void doTest(){
	
		if(!dataReady){		
			System.out.println("Data is not ready");
			return;	
		}
		
		control= new double [data.getRows()];
		
		for(int i=0;i<data.getRows();i++){
			control[i]=data.get(i, 0);
		}
		
		Arrays.sort(control);
		//Compute median of sample 0
		if(samples[0].length%2==0){
			median=(control[((control.length)/2)]+control[(((control.length)/2))-1])/2.0;
		}
		else{
			median=control[((control.length-1)/2)];
		}
		
		//compute W
		W=0.0;
		for(int sample=1;sample<samples[0].length;sample++){
			for(int j=0;j<samples.length;j++){
				if(samples[j][sample]<median){
					W+=1.0;
				}
			}
		}
		
		exactPValue=distribution.computeExactProbability(W,data.getRows()*data.getColumns(),data.getColumns(),data.getRows());
		asymptoticPValue=distribution.computeAsymptoticProbability(W,data.getRows()*data.getColumns(),data.getColumns(),data.getRows());

		performed=true;
		
	}//end-method

	/**
	 * Get W statistic 
	 * 
	 * @return W statistic 
	 */
	public double getW(){
		
		return W;
		
	}//end-method
	
	/**
	 * Get control median value
	 * 
	 * @return median value
	 */
	public double getZ(){
		
		return median;
		
	}//end-method
	
	/**
	 * Get exact p-value 
	 * 
	 * @return exact p-value 
	 */
	public double getExactPValue(){
		
		return exactPValue;
		
	}//end-method
	
	/**
	 * Get asymptotic p-value 
	 * 
	 * @return asymptotic p-value 
	 */
	public double getAsymptoticPValue(){
		
		return asymptoticPValue;
		
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
		report+="Charkraborti and Desu test (CD test)\n";
		report+="***************************************\n\n";

		report+="Median value of the control: "+nf6.format(median)+"\n\n";
		report+="W statistic: "+nf6.format(W)+"\n\n";
		
		report+="Exact P-Value:"+nf6.format(exactPValue)+"\n";
		report+="Asymptotic P-Value:"+nf6.format(asymptoticPValue)+"\n\n";
					
		return report;
			
	}//end-method
		
}//end-class
