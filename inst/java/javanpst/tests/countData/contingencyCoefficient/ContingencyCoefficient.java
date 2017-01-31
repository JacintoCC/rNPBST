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

package javanpst.tests.countData.contingencyCoefficient;

import javanpst.data.structures.dataTable.DataTable;
import javanpst.distributions.common.continuous.ChiSquareDistribution;
import javanpst.tests.StatisticalTest;

/**
 * A contingency coefficient-based test.
 * 
 * This procedure test that row and columns of a n x m 
 * contingency coefficient are independent or have no association.
 * 
 * Q statistic (approximated to a chi-square distribution) and C and Phi
 * coefficients are computed.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class ContingencyCoefficient extends StatisticalTest{

	/**
	 * Data to analyze
	 */
	private DataTable data;
	
	/**
	 * Contingency matrix
	 */
	private double contingencyTable [][];
	
	/**
	 * Number of elements
	 */
	private double N;
	
	/**
	 * Q statistic
	 */
	private double Q;

	/**
	 * P-value
	 */
	private double pValue;
	
	/**
	 * C contingency coefficient
	 */
	private double C;
	
	/**
	 * Phi contingency coefficient
	 */
	private double phi;
	
	/**
	 * Default builder
	 */
	public ContingencyCoefficient(){

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
		C=0.0;
		phi=0.0;
		
		contingencyTable=null;
		
		pValue=-1.0;
		
	}//end-method
	
	/**
	 * Builder. Load data to test.
	 * 
	 * @param newData data to test.
	 */
	public ContingencyCoefficient(DataTable newData){
		
		setReportFormat();
		
		data=DataTable.newInstance(newData);
		
		for(int i=0;i<data.getColumns();i++){
			if(data.getColumnNulls(i)>0){
				System.out.println("No null values allowed in this test.");
				clearData();
				return;
			}
		}
		
		contingencyTable=new double[data.getRows()][data.getColumns()];
		
		for(int i=0;i<data.getRows();i++){
			for(int j=0;j<data.getColumns();j++){
				contingencyTable[i][j]=data.get(i, j);
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
		
		for(int i=0;i<newData.getColumns();i++){
			if(newData.getRowNulls(i)>0){
				System.out.println("No null values allowed in this test.");
				clearData();
				return;
			}
		}
		
		contingencyTable=new double[data.getRows()][data.getColumns()];
		
		for(int i=0;i<data.getRows();i++){
			for(int j=0;j<data.getColumns();j++){
				contingencyTable[i][j]=data.get(i, j);
			}
			
		}
		
		dataReady=true;
		performed=false;
		
	}//end-method
	
	/**
	 * Perform the test
	 */
	public void doTest(){
		
		double sumRow [];
		double sumColumn [];
		double acum;
		
		if(!dataReady){		
			System.out.println("Data is not ready");
			return;	
		}
		
		sumRow=new double [contingencyTable.length];
		sumColumn=new double [contingencyTable[0].length];
		
		N=0.0;
		for(int i=0;i<contingencyTable.length;i++){
			acum=0.0;
			for(int j=0;j<contingencyTable[0].length;j++){
				acum+=contingencyTable[i][j];
				N+=contingencyTable[i][j];
			}
			sumRow[i]=acum;
		}
		
		for(int j=0;j<contingencyTable[0].length;j++){
			acum=0.0;
			for(int i=0;i<contingencyTable.length;i++){
				acum+=contingencyTable[i][j];
			}
			sumColumn[j]=acum;
		}

		//compute Q statistic
		Q=0;
		
		double term;
		for(int i=0;i<contingencyTable.length;i++){
			for(int j=0;j<contingencyTable[0].length;j++){
				term=(N*contingencyTable[i][j])-(sumRow[i]*sumColumn[j]);
				term=term*term;
				term/=N*sumRow[i]*sumColumn[j];
				Q+=term;
			}
		}
		
		//adjust to a chi-square distribution
		int dF=(sumRow.length-1)*(sumColumn.length-1);
		
		pValue=computePValue(dF);
		
		//compute contingency 
		C=Math.sqrt(Q/(Q+N));
		phi=Math.sqrt(Q/(N));
		
		performed=true;

	}//end-method
	
	/**
	 * Compute p-value of no association between rows
	 * and columns.
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
	 * Get C contingency coefficient
	 * 
	 * @return C contingency coefficient
	 */
	public double getC(){
		
		return C;
		
	}//end-method
	
	/**
	 * Get phi contingency coefficient
	 * 
	 * @return phi contingency coefficient
	 */
	public double getPhi(){
		
		return phi;
		
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
		report+="Contingency coefficient based test\n";
		report+="*************\n\n";

		report+="Q statistic: "+nf6.format(Q)+"\n";
		report+="Asymptotic P-Value: "+nf6.format(pValue)+"\n\n";
		
		report+="C contingency coefficient: "+nf6.format(C)+"\n";
		report+="Phi contingency coefficient: "+nf6.format(phi)+"\n\n";
		
		report+="Contingency table\n\n"; 
		
		for(int i=0;i<contingencyTable.length;i++){
			for(int j=0;j<contingencyTable[0].length;j++){
				report+=contingencyTable[i][j]+"\t";
			}
			report+="\n";
		}
		
		report+="\n";
		
		return report;
		
	}//end-method
	
}//end-class
