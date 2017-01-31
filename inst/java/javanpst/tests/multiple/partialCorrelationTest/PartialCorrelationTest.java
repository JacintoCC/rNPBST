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

package javanpst.tests.multiple.partialCorrelationTest;

import java.util.Arrays;

import javanpst.data.structures.dataTable.DataTable;
import javanpst.distributions.tests.PartialCorrelationDistribution;
import javanpst.tests.StatisticalTest;

/**
 * A test based on partial correlation.
 * 
 * This procedure can be applied to test independence of 2 variables, X and Y,
 * when the effect of a third variable, Z, is removed. 
 * 
 * The test statistic is computed by finding the number of concordant (C) and
 * discordant (Q) pairs existing in pairs X-Y, X-Z and Y-Z. Kendall's tau
 * coefficient is also provided.
 * 
 * Note: This test is not recommended for more than 20 samples
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class PartialCorrelationTest extends StatisticalTest{

	/**
	 * Distribution of the test
	 */
	private PartialCorrelationDistribution distribution;
	
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
	 * Concordant pairs
	 */
	private double TxyC,TxzC,TyzC;
	
	/**
	 * Discordant pairs
	 */
	private double TxyQ,TxzQ,TyzQ;
	
	/**
	 * T statistics
	 */
	private double Txy,Txz,Tyz;
	
	/**
	 * Kendall's tau coefficient
	 */
	private double tau;
	
	/**
	 * P-value of no association hypothesis
	 */
	private double pValue;
	
	/**
	 * Default builder
	 */
	public PartialCorrelationTest(){

		distribution=PartialCorrelationDistribution.getInstance();
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
		
		Txy=0.0;
		Txz=0.0;
		Tyz=0.0;

		tau=0.0;
		
		samples=null;
		ranks=null;

		pValue=-1.0;
		
	}//end-method
	
	/**
	 * Builder. Load data to test.
	 * 
	 * @param newData data to test.
	 */
	public PartialCorrelationTest(DataTable newData){
		
		distribution=PartialCorrelationDistribution.getInstance();
		setReportFormat();
		
		data=DataTable.newInstance(newData);
		
		if(data.getColumns()<3){
			System.out.println("Partial correlation test only can be employed with three variables: X, Y and Z");
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
		
		for(int i=0;i<data.getRows();i++){
			Arrays.fill(ranks[i], -1.0);
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
		
		distribution=PartialCorrelationDistribution.getInstance();
		
		data=DataTable.newInstance(newData);
		
		if(data.getColumns()<3){
			System.out.println("Partial correlation test only can be employed with three variables: X, Y and Z");
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
		
		for(int i=0;i<data.getRows();i++){
			Arrays.fill(ranks[i], -1.0);
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
		
		computeRanks();
		
		sortRanksZ();
		
		//compute T partial statistics
		
		//Txz
		TxzC=0.0;
		TxzQ=0.0;

		for(int i=0;i<ranks[0].length-1;i++){

			for(int j=i+1;j<ranks[0].length;j++){
				if(ranks[0][i]>ranks[0][j]){
					TxzQ+=1.0;
				}
				else{
					TxzC+=1.0;
				}
			}
		}
		
		//Tyz
		TyzC=0.0;
		TyzQ=0.0;

		for(int i=0;i<ranks[0].length-1;i++){

			for(int j=i+1;j<ranks[0].length;j++){
				if(ranks[1][i]>ranks[1][j]){
					TyzQ+=1.0;
				}
				else{
					TyzC+=1.0;
				}
			}
		}
		
		sortRanksY();
		
		//Txy
		TxyC=0.0;
		TxyQ=0.0;

		for(int i=0;i<ranks[0].length-1;i++){

			for(int j=i+1;j<ranks[0].length;j++){
				if(ranks[0][i]>ranks[0][j]){
					TxyQ+=1.0;
				}
				else{
					TxyC+=1.0;
				}
			}
		}
		
		//Compute T statistics
		int n=ranks[0].length;
		
		Txy=2*(TxyC-TxyQ)/(double)(n*(n-1));
		Txz=2*(TxzC-TxzQ)/(double)(n*(n-1));
		Tyz=2*(TyzC-TyzQ)/(double)(n*(n-1));
		
		//Compute Kendall's tau
		tau=Txy-(Txz*Tyz);
		
		tau/=Math.sqrt((1.0-(Txz*Txz))*(1.0-(Tyz*Tyz)));
		
		pValue=distribution.computeProbability(n,tau);
		
		performed=true;
		
	}//end-method
	
	/**
	 * Sort ranks according to Z order
	 */
	private void sortRanksZ(){
		
		for(int i=0;i<ranks[0].length-1;i++){
			for(int j=i+1;j<ranks[0].length;j++){
				if(ranks[2][j]<ranks[2][i]){
					swapColumns(i,j);
				}
			}
		}
		
	}//end-method
	
	/**
	 * Sort ranks according to Y order
	 */
	private void sortRanksY(){
		
		for(int i=0;i<ranks[0].length-1;i++){
			for(int j=i+1;j<ranks[0].length;j++){
				if(ranks[1][j]<ranks[1][i]){
					swapColumns(i,j);
				}
			}
		}
		
	}//end-method
	
	/**
	 * Swap two columns of the ranks matrix
	 * 
	 * @param a first column
	 * @param b second column
	 */
	private void swapColumns(int a, int b){
		
		double aux1,aux2,aux3;
		
		aux1=ranks[0][a];
		aux2=ranks[1][a];
		aux3=ranks[2][a];
		
		ranks[0][a]=ranks[0][b];
		ranks[1][a]=ranks[1][b];
		ranks[2][a]=ranks[2][b];
		
		ranks[0][b]=aux1;
		ranks[1][b]=aux2;
		ranks[2][b]=aux3;
		
	}//end-method
	
	/**
	 * Compute ranks of the test. Ties 
	 * are broken with the midrank method.
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
	 * Get tau statistic 
	 * 
	 * @return tau statistic 
	 */
	public double getTau(){
		
		return tau;
		
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
			
		report+="\n**************\n";
		report+="Partial Correlation test\n";
		report+="**************\n\n";
		
		//Partial statistics
		report+="X-Y --- C: "+TxyC+" Q: "+TxyQ+" T: "+Txy+"\n";
		report+="X-Z --- C: "+TxzC+" Q: "+TxzQ+" T: "+Txz+"\n";
		report+="Y-Z --- C: "+TyzC+" Q: "+TyzQ+" T: "+Tyz+"\n";
		
		report+="Tau statistic: "+nf6.format(tau)+"\n";	
		report+="P-value computed : <= "+nf6.format(pValue)+"\n\n";
		
		return report;
			
	}//end-method
	
}//end-class
