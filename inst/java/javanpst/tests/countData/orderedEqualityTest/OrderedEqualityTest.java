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

package javanpst.tests.countData.orderedEqualityTest;

import java.util.Arrays;

import javanpst.data.structures.dataTable.DataTable;
import javanpst.distributions.common.continuous.NormalDistribution;
import javanpst.tests.StatisticalTest;

/**
 * A multinomial equality test for ordered categories
 * 
 * This test can be applied in 2 x K tables with ordered categories,
 * with the aim of testing if column probabilities are the same, that is, if 
 * p11=p12, p21 = p22, .. , p1k = p2k. Alternative is that
 * probabilities are in increasing or decreasing order (directional).
 * 
 * A normal approximation, through a modification of the Wilcoxon rank-sum statistic
 * (Mann-Whitney statistic) is provided for all the alternatives.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class OrderedEqualityTest extends StatisticalTest{

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
	 * W statistic for first row (x)
	 */
	private double Wx;
	
	/**
	 * W statistic for second row (y)
	 */
	private double Wy;
	
	/**
	 * Z value for right tail
	 */
	private double Zright;
	
	/**
	 * Z value for left tail
	 */
	private double Zleft;
	
	/**
	 * P-value for right tail
	 */
	private double asymptoticRightTail;
	
	/**
	 * P-value for left tail
	 */
	private double asymptoticLeftTail;
	
	/**
	 * P-value for double tail
	 */
	private double asymptoticDoubleTail;
	
	/**
	 * Default builder
	 */
	public OrderedEqualityTest(){

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
		
		Wx=0.0;
		Wy=0.0;
		
		samples=null;
		
		asymptoticRightTail=-1.0;
		asymptoticLeftTail=-1.0;
		asymptoticDoubleTail=-1.0;
		
	}//end-method
	
	/**
	 * Builder. Load data to test.
	 * 
	 * @param newData data to test.
	 */
	public OrderedEqualityTest(DataTable newData){
		
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

		double sumColumns[];
		double m,n;
		double ranks[];
			
		if(!dataReady){
			System.out.println("Data is not ready");
			return;	
		}
		
		sumColumns=new double [data.getColumns()];
		ranks=new double [data.getColumns()];
		
		Arrays.fill(sumColumns, 0.0);
		m=0;
		n=0;
		N=0;
		
		for(int j=0;j<data.getColumns();j++){
			sumColumns[j]+=samples[0][j];
			sumColumns[j]+=samples[1][j];	
			
			N+=samples[0][j];
			N+=samples[1][j];
			
			m+=samples[0][j];
			n+=samples[1][j];
		}
		
		//compute ranks
		double limit;
		
		ranks[0]=(1.0+sumColumns[0])/2;
		limit=sumColumns[0]+1.0;
		for(int j=1;j<data.getColumns();j++){
			ranks[j]=(limit+(sumColumns[j]+limit-1.0))/2;
			limit+=sumColumns[j];
		}
		
		Wx=0.0;
		Wy=0.0;
		
		for(int j=0;j<data.getColumns();j++){
			Wx+=ranks[j]*samples[0][j];
			Wy+=ranks[j]*samples[1][j];
		}
		
		//compute z values
		
		double denominator;
		
		double sumTies=0.0;
		
		for(int j=0;j<data.getColumns();j++){
			sumTies+=sumColumns[j]*((sumColumns[j]*sumColumns[j])-1.0);
		}
		
		denominator=(double)((double)(m*n*(m+n+1))/(double)12.0);
		denominator-=(double)((double)(m*n*sumTies)/(double)(12.0*(m+n)*(m+n-1)));
		
		denominator=Math.sqrt(denominator);
		
		double numerator;
		
		numerator=Wx-0.5-((double)m*((double)n+m+1)/(double)2.0);

		Zright=numerator/denominator;

		numerator=Wx+0.5-((double)m*((double)n+m+1)/(double)2.0);
		Zleft=numerator/denominator;
		
		computePValues();
		
		performed=true;
		
	}//end-method
	
	/**
	 * Compute p-values for right, left and double tailed directional hypotheses
	 */
	private void computePValues(){
		
		NormalDistribution normal=new NormalDistribution();
		
		asymptoticRightTail=1.0-normal.getTipifiedProbability(Zright, false);
		asymptoticLeftTail=normal.getTipifiedProbability(Zleft, false);

		asymptoticDoubleTail=Math.min(Math.min(asymptoticLeftTail,asymptoticRightTail)*2.0,1.0);
		
	}//end-method
	
	/**
	 * Get W statistic for first row
	 * 
	 * @return W statistic for first row
	 */
	public double getWx(){
		
		return Wx;
		
	}//end-method
	
	/**
	 * Get W statistic for second row
	 * 
	 * @return W statistic for second row
	 */
	public double getWy(){
		
		return Wy;
		
	}//end-method
	
	/**
	 * Get Z value for left tailed hypothesis
	 * 
	 * @return W statistic for second row
	 */
	public double getZleft(){
		
		return Zleft;
		
	}//end-method
	
	/**
	 * Get Z value for right tailed hypothesis
	 * 
	 * @return W statistic for second row
	 */
	public double getZright(){
		
		return Zright;
		
	}//end-method
	
	/**
	 * Get right tail p-value 
	 * 
	 * @return p-value computed
	 */
	public double getRightPValue(){
		
		return asymptoticRightTail;
		
	}//end-method
	
	/**
	 * Get left tail p-value 
	 * 
	 * @return p-value computed
	 */
	public double getLeftPValue(){
		
		return asymptoticLeftTail;
		
	}//end-method
	
	/**
	 * Get double tail p-value 
	 * 
	 * @return p-value computed
	 */
	public double getDoublePValue(){
		
		return asymptoticDoubleTail;
		
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
		report+="Multinomial equality test for ordered categories\n";
		report+="*************\n\n";

		report+="W statistic (first row): "+nf6.format(Wx)+"\n";
		report+="W statistic (second row): "+nf6.format(Wy)+"\n\n";
		
		report+="Asymptotic P-Values:\n"; 
		report+="Left tail p-Value (Y > X) :"+nf6.format(asymptoticLeftTail)+"\n";
		report+="Right tail p-Value (X > Y) :"+nf6.format(asymptoticRightTail)+"\n";
		report+="Double tail p-Value (X != Y) :"+nf6.format(asymptoticDoubleTail)+"\n\n";
				
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
