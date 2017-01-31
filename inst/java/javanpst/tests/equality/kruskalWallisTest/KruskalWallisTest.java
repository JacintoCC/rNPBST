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

package javanpst.tests.equality.kruskalWallisTest;

import java.util.Arrays;

import javanpst.data.structures.dataTable.DataTable;
import javanpst.tests.StatisticalTest;
import javanpst.distributions.common.continuous.ChiSquareDistribution;
import javanpst.distributions.common.continuous.NormalDistribution;

/**
 * The Kruskal-Wallis test.
 * 
 * This test is the natural extension of the two-sample Wilcoxon test
 * for location to the case of k independent samples. 
 * 
 * Assuming the location model, the null hypothesis is that the k medians
 * of the populations are equal, whereas the alternative is that at least
 * two location parameter differ.
 * 
 * Ties are broken by the midrank method (e.g. a tie in ranks 6 and 7 is assumed
 * as a 6.5 rank for both values).
 * 
 * A multiple-comparisons procedure (Bonferroni-Dunn), applied at 0.10 and 0.05
 * levels of significance is also provided.  
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class KruskalWallisTest extends StatisticalTest{

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
	 * Array with all the values of the combined sample
	 */
	private double combined[];
	
	/**
	 * Array of ranks
	 */
	private double ranks [];
	
	/**
	 * Sum of ranks for each population
	 */
	private double sumRanks[];
	
	/**
	 * Average rank for each population
	 */
	private double avgRanks[];
	
	/**
	 * H statistic
	 */
	private double H;
	
	/**
	 * P-value computed
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
	public KruskalWallisTest(){

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
		
		H=0.0;
		
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
	public KruskalWallisTest(DataTable newData){
		
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
		ranks=new double[combined.length];
		
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
		ranks=new double[combined.length];
		
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
		
		int counter,i,sample;
		double sum;
		double mediumRank;
		double value;
		boolean found;
		int position;
		double term1, term2;
		
		if(!dataReady){		
			System.out.println("Data is not ready");
			return;	
		}
		
		for(i=0;i<combined.length-1;i++){
			
			if(combined[i]!=combined[i+1]){
				ranks[i]=(i+1);
			}
			else{
				counter=2;
				sum=(2*i)+3;
				while(((i+counter)<combined.length)&&(combined[i]==combined[i+counter])){
					sum+=(i+counter);
					counter++;
				}
				counter--;
				mediumRank=sum/(counter+1);
				for(int j=0;j<counter+1;j++){
					ranks[i+j]=mediumRank;
				}
				i+=counter;
			}
		}
		
		if(i==combined.length-1){
			ranks[i]=(i+1);
		}
		
		sumRanks=new double[samples[0].length];
		avgRanks=new double[samples[0].length];
		
		Arrays.fill(sumRanks, 0.0);
		for(sample=0;sample<samples[0].length;sample++){
			for(int j=0;j<samples.length;j++){
				value=samples[j][sample];
				found=false;
				position=-1;
				for(int k=0;!found;k++){
					if(combined[k]==value){
						found=true;
						position=k;
					}
				}
				sumRanks[sample]+=ranks[position];
			}
		}
		
		for(i=0;i<samples[0].length;i++){
			avgRanks[i]=sumRanks[i]/(double)samples.length;
		}
		
		N=combined.length;
		
		term1=12.0/(N*(N+1)*(N/sumRanks.length));
		
		term2=0.0;
		
		for(i=0;i<sumRanks.length;i++){
			
			value=(sumRanks[i]*sumRanks[i]);
			
			term2+=value;
		}

		H=(term1*term2)-(3.0*(N+1.0));
		int dF=sumRanks.length-1;
		
		computePValue(dF);
		
		multipleComparisonsProcedure();
		
		performed=true;
		
	}//end-method
		
	/**
	 * Compute critical values for a multiple comparisons procedure (Bonferroni-Dunn)
	 */
	private void multipleComparisonsProcedure(){	
	
		//Comparison of groups
		
		double critical90,critical95;
		
		critical90=1.0-(0.1/(double)((sumRanks.length)*(sumRanks.length-1)));
		critical95=1.0-(0.05/(double)((sumRanks.length)*(sumRanks.length-1)));
		
		NormalDistribution normal=new NormalDistribution();
		
		critical90=normal.inverseNormalDistribution(critical90);
		critical95=normal.inverseNormalDistribution(critical95);
		
		double denominator;
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
		
		pValue=chi.computeCumulativeProbability(H);
		
	}//end-method
	
	/**
	 * Get H statistic 
	 * 
	 * @return H statistic 
	 */
	public double getH(){
		
		return H;
		
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
			
		report+="\n***************************************\n";
		report+="Kruskal-Wallis test\n";
		report+="***************************************\n\n";

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
		
		report+="H statistic: "+nf6.format(H)+"\n\n";	
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
		report+="Multiple comparisons procedure (Kruskal-Wallis test)\n";
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
