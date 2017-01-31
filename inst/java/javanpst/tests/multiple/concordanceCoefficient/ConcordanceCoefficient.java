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

package javanpst.tests.multiple.concordanceCoefficient;

import java.util.Arrays;

import javanpst.data.structures.dataTable.DataTable;
import javanpst.tests.StatisticalTest;
import javanpst.distributions.common.continuous.ChiSquareDistribution;

/**
 * A concordance coefficient-based test.
 *
 * This procedure test rankings correlation of k rank procedures.
 * Q statistic (approximated to a chi-square distribution) and W coefficient
 * (Kendall's coefficient of concordance) are provided.
 *
 * All samples must have the same number of values
 *
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class ConcordanceCoefficient extends StatisticalTest{

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
	 * S statistic
	 */
	private double S;

	/**
	 * Q statistic
	 */
	private double Q;

	/**
	 * W coefficient
	 */
	private double W;

	/**
	 * Reduction to variance of Q due to ties
	 */
	private double tiesWeight;

	/**
	 * P-value
	 */
	private double pValue;

	/**
	 * Default builder
	 */
	public ConcordanceCoefficient(){

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
		W=0.0;

		samples=null;
		ranks=null;
		sumRanks=null;

		pValue=-1.0;

	}//end-method

	/**
	 * Builder. Load data to test.
	 *
	 * @param newData data to test.
	 */
	public ConcordanceCoefficient(DataTable newData){

		setReportFormat();

		data=DataTable.newInstance(newData);

		if(data.getColumns()<3){
			System.out.println("Concordance coefficient test only can be employed with more than two samples");
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

		for(int j=0;j<data.getColumns();j++){
			S+=(sumRanks[j]*sumRanks[j]);
		}
		S-=((double)(data.getRows()*data.getRows()*data.getColumns()*(data.getColumns()+1.0)*(data.getColumns()+1.0)))/4.0;

		W=12.0*S;
		W/=(data.getRows()*data.getRows()*data.getColumns()*((data.getColumns()*data.getColumns())-1.0))-tiesWeight;

		Q=data.getRows()*(data.getColumns()-1)*W;

		computePValue(sumRanks.length-1);

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
	 * Compute ranks for the Coefficient concordance test
	 * (n..1, k rows). Ties are handled by the midrank method
	 */
	private void computeRanks(){

		double rank,max;
		double newRank;
		int count;

		tiesWeight=0.0;
		for(int i=0;i<data.getRows();i++){

			rank=1.0;
			do{
				max=Double.MIN_VALUE;
				count=0;
				for(int j=0;j<data.getColumns();j++){
					if((ranks[i][j]==-1.0)&&(samples[i][j]==max)){
						count+=1;
					}
					if((ranks[i][j]==-1.0)&&(samples[i][j]>max)){
						max=samples[i][j];
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

					if(samples[i][j]==max){
						ranks[i][j]=newRank;
					}
				}

				rank+=count;
			}while(rank<=data.getColumns());

		}

	}//end-method

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

		report+="S statistic: "+nf6.format(S)+"\n";
		report+="Q statistic: "+nf6.format(Q)+"\n\n";
		report+="W ratio (Kendall's coefficient of concordance): "+nf6.format(W)+"\n\n";
		report+="P-Value computed :"+nf6.format(pValue)+"\n\n";

		return report;

	}//end-method

}//end-class
