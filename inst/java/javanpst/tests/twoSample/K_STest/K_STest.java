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

package javanpst.tests.twoSample.K_STest;

import java.util.Arrays;

import javanpst.data.structures.dataTable.DataTable;
import javanpst.distributions.tests.KolmogorovTwoSampleDistribution;
import javanpst.tests.StatisticalTest;

/**
 * The Kolmogorov-Smirnov test for two samples 
 * 
 * This test can be applied to test the null hypothesis of equal distributions
 * against the one side and two-side alternatives.
 * 
 * The exact p-value is computed is the number of values per sample is equal or less
 * than 40. In addition, an approximation to the normal distribution is computed.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class K_STest extends StatisticalTest{

	/**
	 * Test distribution
	 */
	private KolmogorovTwoSampleDistribution distribution;
	
	/**
	 * Data to analyze
	 */
	private DataTable data;
	
	/**
	 * Quantiles of sample 1
	 */
	private double Sn [];
	
	/**
	 * Quantiles of sample 2
	 */
	private double Sm [];
	
	/**
	 * Values of the first sample
	 */
	private double Fn [];
	
	/**
	 * Values of the second sample
	 */
	private double Fm [];
	
	/**
	 * Test statistics
	 */
	private double Dn,DnPos,DnNeg;
	
	/**
	 * Array with all the values of the combined sample
	 */
	private double combined[];
	
	/**
	 * Left tail asymptotic p-value
	 */
	private double asymptoticLeftTail;
	
	/**
	 * Right tail asymptotic p-value
	 */
	private double asymptoticRightTail;
	
	/**
	 * Double tail asymptotic p-value
	 */
	private double asymptoticDoubleTail;
	
	/**
	 * Left tail p-value
	 */
	private double exactLeftTail;
	
	/**
	 * Right tail p-value
	 */
	private double exactRightTail;
	
	/**
	 * Double tail p-value
	 */
	private double exactDoubleTail;
	
	/**
	 * Default builder
	 */
	public K_STest(){
		
		distribution = KolmogorovTwoSampleDistribution.getInstance();
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
		
		Dn=0;

		Sn=null;
		Sm=null;
		Fn=null;
		Fm=null;
		
		exactLeftTail=-1.0;
		exactRightTail=-1.0;
		exactDoubleTail=-1.0;
		asymptoticLeftTail=-1.0;
		asymptoticRightTail=-1.0;
		asymptoticDoubleTail=-1.0;

	}//end-method
	
	/**
	 * Builder. Load data to test.
	 * 
	 * @param newData data to test.
	 */
	public K_STest(DataTable newData){

		distribution = KolmogorovTwoSampleDistribution.getInstance();
		setReportFormat();
		
		data=DataTable.newInstance(newData);
		
		if(data.getColumns()!=2){
			System.out.println("Kolmogorov-Smirnov test only can be employed with two samples");
			clearData();
			return;
		}
		
		Fn=new double [data.getRows()];
		Fm=new double [data.getRows()];
		combined= new double [data.getRows()+data.getRows()];
		
		for(int i=0;i<data.getRows();i++){
			
			Fm[i]=data.get(i, 0);
			combined[i]=data.get(i, 0);

		}
		
		for(int i=0;i<data.getRows();i++){
			
			Fn[i]=data.get(i, 1);
			combined[i+data.getRows()]=data.get(i, 1);

		}
		
		Sn=new double [data.getRows()+data.getRows()];
		Sm=new double [data.getRows()+data.getRows()];
		
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
		
		if(data.getColumns()!=2){
			System.out.println("Kolmogorov-Smirnov test only can be employed with two samples");
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
		
		Fn=new double [data.getRows()];
		Fm=new double [data.getRows()];
		combined= new double [data.getRows()+data.getRows()];
		
		for(int i=0;i<data.getRows();i++){
			
			Fm[i]=data.get(i, 0);
			combined[i]=data.get(i, 0);

		}
		
		for(int i=0;i<data.getRows();i++){
			
			Fn[i]=data.get(i, 1);
			combined[i+data.getRows()]=data.get(i, 1);

		}
		
		Sn=new double [data.getRows()+data.getRows()];
		Sm=new double [data.getRows()+data.getRows()];
		
		dataReady=true;
		performed=false;
		
	}//end-method
	
	/**
	 * Performs the test
	 */
	public void doTest(){

		double distinct;
		double increment;
		int index, cdfIndex;
		double value;
		double combinedCdf [];
		
		if(!dataReady){		
			System.out.println("Data is not ready");
			return;	
		}
		
		Arrays.sort(Fn);
		Arrays.sort(Fm);
		Arrays.sort(combined);
		
		distinct=1;
		
		//count the number of distinct values
		for(int i=1;i<combined.length;i++){
			
			if(combined[i]!=combined[i-1]){
				distinct++;
			}
			
		}
		
		increment=1.0/distinct;
		combinedCdf=new double [combined.length];
		
		combinedCdf[0]=0.0;
		
		for(int i=1;i<combined.length;i++){
			
			if(combined[i]==combined[i-1]){
				combinedCdf[i]=combinedCdf[i-1];
			}
			else{
				combinedCdf[i]=combinedCdf[i-1]+increment;
			}
			
		}
		
		//fill Sn and Sm arrays
		index=0;
		cdfIndex=0;
		
		while(index<Fn.length){
			
			value=Fn[index];
			
			while(combined[cdfIndex]!=value){
				cdfIndex++;
			}
			
			Sn[index]=combinedCdf[cdfIndex];
			index++;
			
		}

		index=0;
		cdfIndex=0;
		
		while(index<Fm.length){
			
			value=Fm[index];
			
			while(combined[cdfIndex]!=value){
				cdfIndex++;
			}
			
			Sm[index]=combinedCdf[cdfIndex];
			index++;
			
		}
		
		//Compute Dn
		Dn = 0.0;
		DnPos = 0.0;
		DnNeg = 0.0;
		
		for(int i=0;i<Sn.length;i++){
			
			//Sn - Sm			
			value=Sm[i]-Sn[i];
			
			DnPos=Math.max(DnPos,value);
			DnNeg=Math.min(DnNeg,value);
			Dn=Math.max(Dn,Math.abs(value));
			
		}
		
		exactLeftTail=distribution.computeExactProbability(data.getRows(), Math.abs(DnNeg));
		exactRightTail=distribution.computeExactProbability(data.getRows(), Math.abs(DnPos));
		exactDoubleTail=distribution.computeExactProbability(data.getRows(), Math.abs(Dn));
		asymptoticLeftTail=distribution.computeAsymptoticProbability(data.getRows(), Math.abs(DnNeg));
		asymptoticRightTail=distribution.computeAsymptoticProbability(data.getRows(), Math.abs(DnPos));
		asymptoticDoubleTail=distribution.computeAsymptoticProbability(data.getRows(), Math.abs(Dn));
		
		performed=true;

	}//end-method
	
	/**
	 * Get Kolmogorov double tailed statistic 
	 * 
	 * @return Kolmogorov statistic 
	 */
	public double getDn(){
		
		return Dn;
		
	}//end-method
	
	/**
	 * Get Kolmogorov left tail (Y > X) statistic 
	 * 
	 * @return Kolmogorov statistic 
	 */
	public double getDnNeg(){
		
		return DnNeg;
		
	}//end-method
	
	/**
	 * Get Kolmogorov righ tail statistic 
	 * 
	 * @return Kolmogorov statistic 
	 */
	public double getDnPos(){
		
		return DnPos;
		
	}//end-method
	
	/**
	 * Get left tail exact p-value 
	 * 
	 * @return left tail p-value computed
	 */
	public double getExactLeftPValue(){
		
		return exactLeftTail;
		
	}//end-method
	
	/**
	 * Get right tail exact p-value 
	 * 
	 * @return right tail p-value computed
	 */
	public double getExactRightPValue(){
		
		return exactRightTail;
		
	}//end-method
	
	/**
	 * Get double tail exact p-value 
	 * 
	 * @return double tail p-value computed
	 */
	public double getExactDoublePValue(){
		
		return exactDoubleTail;
		
	}//end-method
	
	/**
	 * Get left tail p-value 
	 * 
	 * @return left tail p-value computed
	 */
	public double getLeftPValue(){
		
		return asymptoticLeftTail;
		
	}//end-method
	
	/**
	 * Get right tail p-value 
	 * 
	 * @return right tail p-value computed
	 */
	public double getRightPValue(){
		
		return asymptoticRightTail;
		
	}//end-method
	
	/**
	 * Get double tail p-value 
	 * 
	 * @return double tail p-value computed
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
			
		report+="\n***************************************\n";
		report+="Kolmogorov Two Sample test\n";
		report+="***************************************\n\n";

		report+="Dn+ statistic: "+nf6.format(DnPos)+"\n";
		report+="Dn- statistic: "+nf6.format(DnNeg)+"\n";
		report+="Dn statistic: "+nf6.format(Dn)+"\n\n";
					
		if(Fn.length<9){
			report+="Exact P-Value (Left tail, Y > X): "+nf6.format(exactLeftTail)+"\n";
			report+="Exact P-Value (Right tail, Y < X): "+nf6.format(exactRightTail)+"\n";
			report+="Exact P-Value (Double tail, Y != X): "+nf6.format(exactDoubleTail)+"\n\n";
		}
		else{
			report+="Asymptotic P-Value (Left tail, Y > X): "+nf6.format(asymptoticLeftTail)+"\n";
			report+="Asymptotic P-Value (Right tail, Y > X): "+nf6.format(asymptoticRightTail)+"\n";
			report+="Asymptotic P-Value (Double tail, Y != X): "+nf6.format(asymptoticDoubleTail)+"\n\n";
		}
	
		return report;
			
	}//end-method
		
}//end-class
