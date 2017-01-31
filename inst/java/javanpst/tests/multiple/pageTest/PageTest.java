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

package javanpst.tests.multiple.pageTest;

import java.util.Arrays;
import javanpst.data.structures.dataTable.DataTable;
import javanpst.distributions.tests.PageDistribution;
import javanpst.tests.StatisticalTest;

/**
 * The Page's test.
 * 
 * This test can be applied to reject as null hypothesis that the n medians
 * of the treatments (columns) are equal when applied to k blocks (rows).
 * 
 * Alternative states that treatments' median are set in increasing order
 * 
 * Ties are broken by the midrank method (e.g. a tie in ranks 6 and 7 is assumed
 * as a 6.5 rank for both values).   
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class PageTest extends StatisticalTest{

	/**
	 * Distribution of the test
	 */
	private PageDistribution distribution;
	
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
	 * L statistic
	 */
	private double L;
	
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
	public PageTest(){

		distribution=PageDistribution.getInstance();
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
		
		L=0.0;

		samples=null;
		ranks=null;
		sumRanks=null;
		
		asymptoticPValue=-1.0;
		exactPValue=-1.0;
		
	}//end-method
	
	/**
	 * Builder. Load data to test.
	 * 
	 * @param newData data to test.
	 */
	public PageTest(DataTable newData){
		
		distribution=PageDistribution.getInstance();
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
			System.out.println("Page test only can be employed with more than two samples");
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
		
		//data is arranged from better to worse method
		L=0.0;
		
		for(int j=0;j<data.getColumns();j++){
			L+=(sumRanks[j]*(j+1.0));
		}
		
		//exact p-value
		exactPValue=distribution.computeExactProbability(data.getColumns(),data.getRows(), L);
		asymptoticPValue=distribution.computeAsymptoticProbability(data.getColumns(),data.getRows(), L);
		
		performed=true;
			
	}//end-method
	
	/**
	 * Compute ranks for the Page test
	 * (1..n, k rows). Ties are handled by the midrank method
	 */
	private void computeRanks(){
		
		double rank,min;
		double newRank;
		int count;
		
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
		
	}//end-method
	
	/**
	 * Get L statistic 
	 * 
	 * @return L statistic 
	 */
	public double getL(){
		
		return L;
		
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
	 * Get exact p-value 
	 * 
	 * @return exact p-value 
	 */
	public double getExactPValue(){
		
		return exactPValue;
		
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
			
		report+="\n**************\n";
		report+="Page test\n";
		report+="**************\n\n";

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
		
		report+="L statistic: "+nf6.format(L)+"\n";	
		report+="Exact p-value: <= "+nf6.format(exactPValue)+"\n";
		report+="Asymptotic p-value:"+nf6.format(asymptoticPValue)+"\n\n";
				
		return report;
			
	}//end-method
	
}//end-class
