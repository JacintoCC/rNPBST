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

package javanpst.tests.goodness.chiSquareTest;

import javanpst.data.structures.dataTable.DataTable;
import javanpst.distributions.DistributionDefinitions;
import javanpst.distributions.common.continuous.ChiSquareDistribution;
import javanpst.distributions.common.discrete.BinomialDistribution;
import javanpst.distributions.common.discrete.GeometricDistribution;
import javanpst.distributions.common.discrete.PoissonDistribution;
import javanpst.distributions.common.discrete.UniformDistribution;
import javanpst.tests.StatisticalTest;

/**
 * The Chi-square test.
 * 
 * This goodness of fit can be used to test if a given sample follows
 * a discrete distribution. This implementation allow to adjust the data
 * to a uniform, binomial, poisson or geometric distribution.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class ChiSquareTest extends StatisticalTest{

	/**
	 * Data to fit
	 */
	private DataTable data;
	
	/**
	 * Type of distribution to fit
	 */
	private int typeDist;
	
	/**
	 * Number of degrees of the chi-square distribution
	 */
	private int freedomDegree; 
	
	/**
	 * Number of estimated parameters
	 */
	private int nEstimated;
	
	/**
	 * Number of values in the sample
	 */
	private int nCount;
	
	/**
	 * Error obtained in each category
	 */
	private double errorTable [][];
	
	/**
	 * distribution defined by data
	 */
	private double distribution [][];
	
	/**
	 * Tests if the distribution to adjust is completely defined
	 */
	private boolean distributionReady;
	
	/**
	 * Q statistic
	 */
	private double Q;
	
	/**
	 * P-value of the test
	 */
	private double pValue;
	
	/**
	 * Uniform distribution
	 */
	private UniformDistribution uniform;
	
	/**
	 * Binomial distribution
	 */
	private BinomialDistribution binomial;
	
	/**
	 * Poisson distribution
	 */
	private PoissonDistribution poisson;
	
	/**
	 * Geometric distribution
	 */
	private GeometricDistribution geometric;
	
	/**
	 * Chi-square distribution
	 */
	private ChiSquareDistribution chiSquare;
	
	/**
	 * Default builder
	 */
	public ChiSquareTest(){
		
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
		distributionReady=false;
		
		typeDist=-1;
		freedomDegree=0;
		nEstimated=0;
		
		errorTable=null;
		
		pValue=-1.0;
		
	}//end-method
	
	/**
	 * Builder. Load data to test.
	 * 
	 * @param newData data to test.
	 */
	public ChiSquareTest(DataTable newData){
		
		setReportFormat();
		
		data=DataTable.newInstance(newData);
		
		if(data.getColumns()!=2){
			System.out.println("Chi-square test only accept tables in the following format:");
			System.out.println("<category> <frequency>");
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
	
		errorTable = new double [data.getRows()][2];
		distribution = new double [data.getRows()][2];
		
		nCount=0;
		for(int i=0;i<data.getRows();i++){
			distribution[i][0]=data.get(i, 0);
			distribution[i][1]=data.get(i, 1);
			
			nCount+=data.get(i, 1);
		}
		
		dataReady=true;
		performed=false;
		distributionReady=false;
		
	}//end-method
	
	/**
	 * Load data to test.
	 * 
	 * @param newData data to test.
	 */
	public void setData(DataTable newData){
		
		data=DataTable.newInstance(newData);
		
		if(data.getColumns()!=2){
			System.out.println("Chi-square test only accept tables in the following format:");
			System.out.println("<category> <frequency>");
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
		
		errorTable = new double [data.getRows()][2];
		distribution = new double [data.getRows()][2];
		
		nCount=0;
		for(int i=0;i<data.getRows();i++){
			distribution[i][0]=data.get(i, 0);
			distribution[i][1]=data.get(i, 1);
			
			nCount+=data.get(i, 1);
		}
		
		dataReady=true;
		performed=false;
		distributionReady=false;
		
	}//end-method
	
	/**
	 * Sets the distribution to adjust as a uniform distribution with N parameter
	 * 
	 * @param N N parameter of the distribution
	 */
	public void adjustUniform(int N){
		
		uniform= new UniformDistribution();
		
		uniform.setN(N);
		
		typeDist=DistributionDefinitions.UNIFORM;
		nEstimated=0;
		
		distributionReady=true;
		
	}//end-method
	
	/**
	 * Sets the distribution to adjust as a binomial distribution with N and P parameters
	 * 
	 * @param N N parameter of the distribution
	 * @param P P parameter of the distribution
	 */
	public void adjustBinomial(int N, double P){
		
		binomial= new BinomialDistribution();
		
		binomial.setN(N);
		binomial.setP(P);
		
		typeDist=DistributionDefinitions.BINOMIAL;
		nEstimated=0;
		
		distributionReady=true;
		
	}//end-method
	
	/**
	 * Sets the distribution to adjust as a binomial distribution with N parameter.
	 * P parameter will be estimated from data
	 * 
	 * @param N N parameter of the distribution
	 */
	public void adjustBinomial(int N){
		
		binomial= new BinomialDistribution();
		
		binomial.setN(N);
		binomial.setP(estimateProbability());
		
		typeDist=DistributionDefinitions.BINOMIAL;
		nEstimated=1;
		
		distributionReady=true;
		
	}//end-method
	
	/**
	 * Sets the distribution to adjust as a Poisson distribution with K mean.
	 * 
	 * @param K mean of the distribution of the distribution
	 */
	public void adjustPoisson(int K){
		
		poisson= new PoissonDistribution();
		
		poisson.setMean(K);
		
		typeDist=DistributionDefinitions.POISSON;
		nEstimated=0;
		
		distributionReady=true;
		
	}//end-method
	
	/**
	 * Sets the distribution to adjust as a Poisson distribution.
	 * K parameter (mean) will be estimated from data
	 */
	public void adjustPoisson(){
		
		poisson= new PoissonDistribution();
		
		poisson.setMean(estimateMean());
		
		typeDist=DistributionDefinitions.POISSON;
		nEstimated=1;
		
		distributionReady=true;
		
	}//end-method
	
	/**
	 * Sets the distribution to adjust as a Geometric distribution with P parameter.
	 * 
	 * @param P parameter of the distribution of the distribution
	 */
	public void adjustGeometric(double P){
		
		geometric= new GeometricDistribution();
		
		geometric.setP(P);
		
		typeDist=DistributionDefinitions.GEOMETRIC;
		nEstimated=0;
		
		distributionReady=true;
		
	}//end-method
	
	/**
	 * Performs the test
	 */
	public void doTest(){

		int less=0;
		
		if(!dataReady){		
			System.out.println("Data is not ready");
			return;	
		}
		
		if(!distributionReady){		
			System.out.println("Distribution to fit is not set");
			return;	
		}
		
		chiSquare = new ChiSquareDistribution();
		errorTable = new double [data.getRows()][2];
		
		sortDistribution();
		
		for(int i=0;i<distribution.length;i++){
			errorTable[i][0]=distribution[i][1];
			errorTable[i][1]=computeExpected((int)distribution[i][0]);
			errorTable[i][1]*=nCount;
			if(errorTable[i][1]<1.0){
				less++;
			}
		}
		
		if(less>0 && less<data.getRows()){
			trimTable(less);
		}

		Q=0;
		
		double value;
		
		for(int i=0;i<errorTable.length;i++){
			value=((errorTable[i][0]-errorTable[i][1])*(errorTable[i][0]-errorTable[i][1])/errorTable[i][1]);
			Q+=value;
		}
		
		freedomDegree=Math.max(1, errorTable.length-1-nEstimated);
		
		chiSquare.setDegree(freedomDegree);
		pValue=chiSquare.computeCumulativeProbability(Q);
		
		performed=true;
		
	}//end-method
	
	/**
	 * Trim error table, so no category has an error
	 * lower than 1.0.
	 * 
	 * @param delete number of categories to delete
	 */
	private void trimTable(int delete){
		
		double aux [][]= new double [errorTable.length][2];
		int pointer;
		
		for(int i=0;i<errorTable.length;i++){
			aux[i][0]=errorTable[i][0];
			aux[i][1]=errorTable[i][1];
		}
		
		errorTable= new double [errorTable.length-delete][2];
		
		for(int i=0;i<errorTable.length;i++){;
			errorTable[i][1]=0;
		}
		pointer=0;
		
		for(int i=0;i<aux.length;i++){
			errorTable[pointer][0]+=aux[i][0];
			errorTable[pointer][1]+=aux[i][1];
			if(errorTable[pointer][1]>=1.0 && pointer<errorTable.length-1){
				pointer++;
			}
		}
		
		if(pointer!=errorTable.length-1){	
			trimTable(errorTable.length-pointer);
		}
	
	}//end-method
	
	/**
	 * Estimates mean of the distribution provided
	 * 
	 * @return mean estimated
	 */
	private double estimateMean(){
	
		double mean=0.0;
		double sum=0.0;
		
		for (int i=0;i<distribution.length;i++){
			mean+=(double)(distribution[i][0]*distribution[i][1]);
			sum+=(double)distribution[i][1];
        }
		
		mean/=sum;

		return mean;
		
	}//end-method
	
	/**
	 * Estimates probability of the distribution provided
	 * 
	 * @return probability estimated
	 */
	private double estimateProbability(){
		
		double value=0.0;
		double sum=0.0;
		
		for (int i=0;i<distribution.length;i++){
			value+=(double)(distribution[i][0]*distribution[i][1]);
			sum+=(double)distribution[i][1];
        }
		
		value/=(sum*binomial.getN());
		
		return value;
		
	}//end-method
	
	/**
	 * Compute the expected probability for a given value
	 * 
	 * @param value value to estimate
	 * @return expected probability
	 */
	private double computeExpected(int value){
		
		double prob=0.0;
		
		switch(typeDist){
			
			case DistributionDefinitions.UNIFORM:
				prob=uniform.computeProbability(value);
				break;
				
			case DistributionDefinitions.BINOMIAL:
				prob=binomial.computeProbability(value);
				break;
				
			case DistributionDefinitions.POISSON:
				prob=poisson.computeProbability(value);
				break;
				
			case DistributionDefinitions.GEOMETRIC:
				prob=geometric.computeProbability(value);
				break;
		}
		
		return prob;
		
	}//end-method
	
	/**
	 * Sorts distribution to adjust
	 */
	private void sortDistribution(){
		
		for(int i=0;i<distribution.length;i++){
			for(int j=i+1;j<distribution.length;j++){
				if(distribution[i][0]>distribution[j][0]){
					swapRow(distribution[i][0],distribution[i][1],distribution[j][0],distribution[j][1]);
				}
			}
		}
		
	}//end-method
	
	/**
	 * Swaps two rows of the distribution table
	 * 
	 * @param a1 first row, first column value
	 * @param a2 first row, second column value
	 * @param b1 second row, first column value
	 * @param b2 second row, second column value
	 */
	private void swapRow(double a1,double a2,double b1, double b2){
		
		double aux1,aux2;
		
		aux1=a1;
		a1=b1;
		b1=aux1;
		
		aux2=a2;
		a2=b2;
		b2=aux2;
		
	}//end-method

	/**
	 * Get Q statistic 
	 * 
	 * @return Q Statistic
	 */
	public double getQ(){
		
		return Q;
		
	}//end-method
	
	/**
	 * Get p-value of the test
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
		
		report+="\n************\n";
		report+="Chi-square goodness of fit test\n";
		report+="**************\n\n";
		
		switch(typeDist){
		
			case DistributionDefinitions.UNIFORM:
				report+="Fitting data to Uniform distribution (N="+nf6.format(uniform.getN())+")\n\n";
				break;
			
			case DistributionDefinitions.BINOMIAL:
				report+="Fitting data to Binomial distribution (N="+nf6.format(binomial.getN())+" , P="+nf6.format(binomial.getP())+")\n\n";
				break;
				
			case DistributionDefinitions.POISSON:
				report+="Fitting data to Poisson distribution (Mean="+nf6.format(poisson.getMean())+")\n\n";
				break;
				
			case DistributionDefinitions.GEOMETRIC:
				report+="Fitting data to Geometric distribution (P="+nf6.format(geometric.getP())+")\n\n";
				break;
	
		}

		report+="Q Statistic: "+nf6.format(Q)+"\n\n";
		
		report+="P-Value : "+nf6.format(pValue)+"\n";

		return report;
		
	}//end-method
	
}//end-class
