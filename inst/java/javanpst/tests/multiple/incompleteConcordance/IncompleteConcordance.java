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

package javanpst.tests.multiple.incompleteConcordance;

import java.util.Arrays;

import javanpst.data.DataDefinitions;
import javanpst.data.structures.dataTable.DataTable;
import javanpst.distributions.common.continuous.ChiSquareDistribution;
import javanpst.distributions.common.continuous.NormalDistribution;
import javanpst.tests.StatisticalTest;

/**
 * A concordance coefficient-based test for incomplete classifications
 * 
 * This procedure test rankings correlation of k rank procedures
 * applied to incomplete classifications. Q statistic 
 * (approximated to a chi-square distribution) and W coefficient
 * (Kendall's coefficient of concordance) are provided.
 * 
 * Missing values are expressed as NULL values in the table
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class IncompleteConcordance extends StatisticalTest{

	/**
	 * Data to analyze
	 */
	private DataTable data;

	/**
	 * Array of ranks
	 */
	private double ranks [][];
	
	/**
	 * Sum of ranks for each population
	 */
	private double sumRanks[];

	/**
	 * Parameters of the incomplete experiment
	 */
	private double k,n,m,lambda;
	
	/**
	 * Q statistic
	 */
	private double Q;
	
	/**
	 * W coefficient
	 */
	private double W;

	/**
	 * P-value
	 */
	private double pValue;
	
	/**
	 * Critical value at 0.90 confidence level
	 */
	private double criticalZ90;
	
	/**
	 * Critical value at 0.95 confidence level
	 */
	private double criticalZ95;
	
	/**
	 * Default builder
	 */
	public IncompleteConcordance(){
		
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
		W=0.0;

		ranks=null;
		sumRanks=null;
		
		pValue=-1.0;
		
	}//end-method
	
	/**
	 * Builder. Load data to test.
	 * 
	 * @param newData data to test.
	 * @param lambda number of objects ranked by each observer
	 */
	public IncompleteConcordance(DataTable newData,double lambda){
		
		setReportFormat();
		
		data=DataTable.newInstance(newData);
		
		if(data.getColumns()<3){
			System.out.println("Incomplete concordance test only can be employed with more than two samples");
			clearData();
			return;
		}
		
		n=data.getColumns();
		this.lambda=lambda;
		ranks=new double[data.getRows()][data.getColumns()];
		sumRanks=new double[data.getColumns()];
		
		m=0;
		
		double mAux;
		for(int i=0;i<data.getRows();i++){
			mAux=0;
			for(int j=0;j<data.getColumns();j++){
				ranks[i][j]=data.get(i, j);
				if(ranks[i][j]==DataDefinitions.NULL_VALUE){
					ranks[i][j]=0.0;
				}
				else{
					mAux++;
				}
			}
			m=Math.max(m, mAux);
			
		}
		
		k=0;
		double kAux;
		for(int j=0;j<data.getColumns();j++){
			kAux=0;
			for(int i=0;i<data.getRows();i++){
				if(ranks[i][j]!=0.0){
					kAux++;
				}
			}
			k=Math.max(k, kAux);
		}
		Arrays.fill(sumRanks, 0.0);
		
		dataReady=true;
		performed=false;
		
	}//end-method
	
	/**
	 * Sets data to test
	 * 
	 * @param newData data to test.
	 * @param lambda number of objects ranked by each observer
	 */
	public void setData(DataTable newData, double lambda){
		
		data=DataTable.newInstance(newData);
		
		if(data.getColumns()<3){
			System.out.println("Extended median test only can be employed with more than two samples");
			clearData();
			return;
		}
		
		n=data.getColumns();
		this.lambda=lambda;
		ranks=new double[data.getRows()][data.getColumns()];
		sumRanks=new double[data.getColumns()];
		
		m=0;
		
		double mAux;
		for(int i=0;i<data.getRows();i++){
			mAux=0;
			for(int j=0;j<data.getColumns();j++){
				ranks[i][j]=data.get(i, j);
				if(ranks[i][j]==DataDefinitions.NULL_VALUE){
					ranks[i][j]=0.0;
				}
				else{
					mAux++;
				}
			}
			m=Math.max(m, mAux);
			
		}
		
		k=0;
		double kAux;
		for(int j=0;j<data.getColumns();j++){
			kAux=0;
			for(int i=0;i<data.getRows();i++){
				if(ranks[i][j]!=0.0){
					kAux++;
				}
			}
			k=Math.max(k, kAux);
		}
		Arrays.fill(sumRanks, 0.0);
		
		dataReady=true;
		performed=false;
		
	}//end-method
	
	/**
	 * Performs the test
	 */
	public void doTest(){

		double quadR;
		
		if(!dataReady){		
			System.out.println("Data is not ready");
			return;	
		}
		
		for(int i=0;i<data.getRows();i++){
			for(int j=0;j<data.getColumns();j++){
				sumRanks[j]+=ranks[i][j];
			}
		}
		
		quadR=0.0;
		for(int j=0;j<data.getColumns();j++){
			quadR+=(sumRanks[j]*sumRanks[j]);
		}
		
		W=(12.0*quadR)-(3.0*k*k*n*(m+1.0)*(m+1.0));
		W/=lambda*lambda*n*((n*n)-1.0);
		
		Q=(12.0*quadR/(lambda*n*(m+1.0)))-(3.0*k*k*(m+1.0)/lambda);

		computePValue(sumRanks.length-1);
		
		multipleComparisonsProcedure();
		
		performed=true;
		
	}//end-method
	
	/**
	 * Compute critical values for a multiple comparisons procedure (Bonferroni-Dunn)
	 */
	private void multipleComparisonsProcedure(){	
		
		double critical90,critical95;
		
		critical90=1.0-(0.1/(double)((sumRanks.length)*(sumRanks.length-1)));
		critical95=1.0-(0.05/(double)((sumRanks.length)*(sumRanks.length-1)));
		
		NormalDistribution normal=new NormalDistribution();
		
		critical90=normal.inverseNormalDistribution(critical90);
		critical95=normal.inverseNormalDistribution(critical95);
		
		double denominator;
		denominator=Math.sqrt(((double)(k*n*(n+1))/6.0));
		
		criticalZ90=critical90*denominator;
		criticalZ95=critical95*denominator;
	
	}//end-method
	
	/**
	 * Compute p-value of no association between observers
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
	 * Get W ratio
	 * 
	 * @return W ratio
	 */
	public double getW(){
		
		return W;
		
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
			
		report+="\n******************\n";
		report+="Concordance coefficient based test\n";
		report+="for incomplete classifications\n";
		report+="******************\n\n";

		report+="Sum of ranks:\n";
		
		//Sum ranks
		for(int j=0;j<sumRanks.length;j++){
			report+="S"+(j+1)+"\t";
		}
		report+="\n";
		
		for(int j=0;j<sumRanks.length;j++){
			report+=nf6.format(sumRanks[j])+"\t";
		}
		
		report+="\n";
		report+="\n";
		
		report+="Q statistic: "+nf6.format(Q)+"\n\n";
		report+="W ratio (Kendall's coefficient of concordance): "+nf6.format(W)+"\n\n";
		report+="P-Value computed :"+nf6.format(pValue)+"\n\n";

		return report;
			
	}//end-method
	
	/**
	 * Prints a report with the results of the multiple comparisons procedure 
	 * 
	 * @return Output report
	 */
	public String printMultipleComparisonsProcedureReport(){
		
		String report="";
		
		if(!performed){
			report+="The test has not been performed.\n";
			return report;
		}
		
		double Z;
		
		report+="\n***************************************\n";
		report+="Multiple comparisons procedure (Incomplete concordance test)\n";
		report+="***************************************\n\n";

		report+="Critical values: Alpha=0.90: "+criticalZ90+" Alpha=0.95: "+criticalZ95+"\n\n";
		
		report+="Individual comparisons:\n\n";
		
		for(int first=0;first<data.getColumns()-1;first++){
			for(int second=first+1;second<data.getColumns();second++){
				Z=Math.abs(sumRanks[first]-sumRanks[second]);
				report+=(first+1)+" vs "+(second+1)+": Z= "+Z+"\n\n";		
			}
		}
		
		return report;
		
	}//end-method
	
}//end-class
