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

package javanpst.tests.multiple.friedmanTest;

import java.util.Arrays;

import javanpst.data.structures.dataTable.DataTable;
import javanpst.distributions.common.continuous.ChiSquareDistribution;
import javanpst.distributions.common.continuous.NormalDistribution;
import javanpst.tests.StatisticalTest;

/**
 * The Friedman's test.
 * 
 * This test can be applied to reject as null hypothesis that the n medians
 * of the treatments (columns) are equal when applied to k blocks (rows).
 * 
 * Ties are broken by the midrank method (e.g. a tie in ranks 6 and 7 is assumed
 * as a 6.5 rank for both values). Variance of the statistic is adjusted as a 
 * consequence of ties 
 * 
 * A multiple-comparisons procedure (Bonferroni-Dunn), applied at 0.10 and 0.05
 * levels of significance is also provided.  
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class FriedmanTest extends StatisticalTest{

	/**
	 * Data to analyze
	 */
	private DataTable data;
	
	/**
	 * Populations samples
	 */
	private double samples [][];
	
	/**
	 * Array of ranks
	 */
	private double ranks [][];
	
	/**
	 * Sum of ranks for each population
	 */
	private double sumRanks[];
	
	/**
	 * Average rank for each population
	 */
	private double avgRanks[];
	
	/**
	 * S statistic
	 */
	private double S;
	
	/**
	 * Q statistic
	 */
	private double Q;
	
	/**
	 * Reduction to variance of Q due to ties
	 */
	private double tiesWeight;
	
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
	public FriedmanTest(){

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
		
		S=0.0;
		Q=0.0;
		
		samples=null;
		ranks=null;
		sumRanks=null;
		avgRanks=null;
		
		pValue=-1.0;
		
	}//end-method
	
	/**
	 * Builder. Load data to test.
	 * 
	 * @param newData data to test.
	 */
	public FriedmanTest(DataTable newData){
		
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
		ranks=new double[data.getRows()][data.getColumns()];
		sumRanks=new double[data.getColumns()];
		avgRanks=new double [sumRanks.length];
		
		for(int i=0;i<data.getRows();i++){
			Arrays.fill(ranks[i], -1.0);
			for(int j=0;j<data.getColumns();j++){
				samples[i][j]=data.get(i, j);
			}
		}
		
		Arrays.fill(sumRanks, 0.0);
		
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
			System.out.println("Friedman test only can be employed with more than two samples");
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
		ranks=new double[data.getRows()][data.getColumns()];
		sumRanks=new double[data.getColumns()];
		avgRanks=new double [sumRanks.length];
		
		for(int i=0;i<data.getRows();i++){
			Arrays.fill(ranks[i], -1.0);
			for(int j=0;j<data.getColumns();j++){
				samples[i][j]=data.get(i, j);
			}
		}
		
		Arrays.fill(sumRanks, 0.0);
		
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
		
		computeRanks();
		
		for(int i=0;i<data.getRows();i++){
			for(int j=0;j<data.getColumns();j++){
				sumRanks[j]+=ranks[i][j];
			}
		}

		for(int i=0;i<sumRanks.length;i++){
			avgRanks[i]=sumRanks[i]/(double)data.getRows();
		}
		
		for(int j=0;j<data.getColumns();j++){
			S+=(sumRanks[j]*sumRanks[j]);
		}
		S-=((double)(data.getRows()*data.getRows()*data.getColumns()*(data.getColumns()+1.0)*(data.getColumns()+1.0)))/4.0;
		
		Q=12.0*(data.getColumns()-1.0)*S;
		
		Q/=(data.getRows()*data.getColumns()*((data.getColumns()*data.getColumns())-1.0))-tiesWeight;
		
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
		double N=(double)data.getColumns();
		denominator=Math.sqrt(((double)(N*(N+1))/12.0)*((1/(double)data.getRows())+(1/(double)data.getRows())));
		
		criticalZ90=critical90*denominator;
		criticalZ95=critical95*denominator;
	
	}//end-method
	
	/**
	 * Compute p-value for hypotheses of equality of medians
	 */
	private void computePValue(int dF){
		
		ChiSquareDistribution chi=new ChiSquareDistribution();
		
		chi.setDegree(dF);

		pValue=chi.computeCumulativeProbability(Q);
		
	}//end-method
	
	/**
	 * Compute ranks for the Friedman test
	 * (1..n, k rows). Ties are handled by the midrank method
	 */
	private void computeRanks(){
		
		double rank,min;
		double newRank;
		int count;
		
		tiesWeight=0.0;
		for(int i=0;i<data.getRows();i++){
			
			rank=1.0;
			do{
				min=Double.MAX_VALUE;
				count=0;
				for(int j=0;j<data.getColumns();j++){
					if((ranks[i][j]==-1.0)&&(samples[i][j]==min)){
						count+=1;
					}
					if((ranks[i][j]==-1.0)&&(samples[i][j]<min)){
						min=samples[i][j];
						count=1;
					}
				}

				if(count==1){
					newRank=rank;
				}
				else{
					tiesWeight+=count*((count*count)-1.0);
					newRank=0.0;
					for(int k=0;k<count;k++){
						newRank+=(rank+k);
					}

					newRank/=(double)count;
				}
				
				for(int j=0;j<data.getColumns();j++){
				
					if(samples[i][j]==min){
						ranks[i][j]=newRank;
					}
				}
				
				rank+=count;
			}while(rank<=data.getColumns());

		}
		
	}//end.method
	
	/**
	 * Get S statistic 
	 * 
	 * @return S statistic 
	 */
	public double getS(){
		
		return S;
		
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
	 * Get average rank of the selected population
	 * 
	 * @param pop population index (from 1 to k)
	 * @return average rank of the population
	 */
	public double getAvgRanks(int pop){
		
		if(((pop-1)>-1)&&((pop-1)<avgRanks.length)){
			return avgRanks[pop-1];
		}
		
		return -1.0;
		
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
		report+="Friedman test\n";
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
		
		report+="Average ranks:\n";
		
		//Sum ranks
		for(int j=0;j<avgRanks.length;j++){
			report+="S"+(j+1)+"\t";
		}
		report+="\n";
		
		for(int j=0;j<avgRanks.length;j++){
			report+=nf6.format(avgRanks[j])+"\t";
		}
		
		report+="\n";
		report+="\n";
		
		report+="S statistic: "+nf6.format(S)+"\n";	
		report+="Q statistic: "+nf6.format(Q)+"\n\n";
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
		report+="Multiple comparisons procedure (Friedman test)\n";
		report+="***************************************\n\n";

		report+="Critical values: Alpha=0.90: "+criticalZ90+" Alpha=0.95: "+criticalZ95+"\n\n";
		
		report+="Individual comparisons:\n\n";
		
		for(int first=0;first<data.getColumns()-1;first++){
			for(int second=first+1;second<data.getColumns();second++){
				Z=Math.abs(avgRanks[first]-avgRanks[second]);
				report+=(first+1)+" vs "+(second+1)+": Z= "+Z+"\n\n";		
			}
		}
		
		return report;
		
	}//end-method
		
}//end-class
