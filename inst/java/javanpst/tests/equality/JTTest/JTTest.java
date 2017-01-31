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

package javanpst.tests.equality.JTTest;

import javanpst.data.structures.dataTable.DataTable;
import javanpst.distributions.common.continuous.NormalDistribution;
import javanpst.tests.StatisticalTest;

/**
 * The Jonckheere and Terpstra test (JT test)
 *
 * This test can be applied to test the null hypotheses of equal medians
 * between populations. Alternative is that the location parameter
 * is in increasing order:
 *
 * M1 <= M2 <= ... <= Mn
 *
 * P-values are computed through a normal distribution
 * approximation.
 *
 * All samples must have the same number of elements.
 *
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class JTTest extends StatisticalTest{

	/**
	 * Data to analyze
	 */
	private DataTable data;

	/**
	 * Populations samples
	 */
	private double samples [][];

	/**
	 * B statistic
	 */
	private double B;

	/**
	 * Z value
	 */
	private double Z;

	/**
	 * P-value computed
	 */
	private double pValue;

	/**
	 * Default builder
	 */
	public JTTest(){

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

		B=0.0;
		Z=0.0;

		samples=null;

		pValue=-1.0;

	}//end-method

	/**
	 * Builder. Load data to test.
	 *
	 * @param newData data to test.
	 */
	public JTTest(DataTable newData){

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

		int counter;
		double value;
		int N;
		double stMean;
		double stDev;

		if(!dataReady){
			System.out.println("Data is not ready");
			return;
		}

		B=0.0;

		for(int first=0;first<data.getColumns()-1;first++){
			for(int second=first+1;second<data.getColumns();second++){

				counter=0;
				for(int i=0;i<data.getRows();i++){
					value=samples[i][second];
					for(int j=0;j<data.getRows();j++){
						if(value>samples[j][first]){
							counter++;
						}
					}
				}
				B+=counter;
			}
		}

		//compute standard median and variance of the Z statistic

		N=data.getRows()*data.getColumns();

		stMean=((N*N)-(data.getColumns()*data.getRows()*data.getRows()))/4.0;

		stDev=N*N*((2*N)+3.0)-(data.getColumns()*data.getRows()*data.getRows()*((2*data.getRows())+3.0));
		stDev/=72.0;
		stDev=Math.sqrt(stDev);

		Z=(B-stMean)/stDev;

		computePValue();

		performed=true;

	}//end-method

	/**
	 * Compute p-value for hypotheses of equality of medians
	 */
	private void computePValue(){

		NormalDistribution normal=new NormalDistribution();

		pValue=1.0-normal.getTipifiedProbability(Z, false);

	}//end-method

	/**
	 * Get B statistic
	 *
	 * @return B statistic
	 */
	public double getB(){

		return B;

	}//end-method

	/**
	 * Get Z value
	 *
	 * @return Z value
	 */
	public double getZ(){

		return Z;

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
		report+="Jonckheere and Terpstra test (JT test)\n";
		report+="***************************************\n\n";

		report+="B statistic: "+nf6.format(B)+"\n\n";

		report+="P-Value computed :"+nf6.format(pValue)+"\n\n";

		return report;

	}//end-method

}//end-class
