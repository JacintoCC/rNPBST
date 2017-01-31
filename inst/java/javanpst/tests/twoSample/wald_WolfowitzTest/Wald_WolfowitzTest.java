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

package javanpst.tests.twoSample.wald_WolfowitzTest;

import java.util.Arrays;

import javanpst.data.DataDefinitions;
import javanpst.data.structures.dataTable.DataTable;
import javanpst.data.structures.sequence.StringSequence;
import javanpst.distributions.DistributionDefinitions;
import javanpst.distributions.tests.TotalNumberOfRunsDistribution;
import javanpst.tests.StatisticalTest;

/**
 * The Wald-Wolfowitz test.
 * 
 * This tests maps the values of the ordered combined sample
 * into a string sequence (e.g. XXYXYY, where X are the values 
 * of the first sample and Y are the values of the second sample).
 * 
 * Then a Total Number of Runs test is carried out, where the
 * alternative hypothesis (distinct distributions) is accepted
 * when there are too few runs.
 * 
 * The exact and the asymptotic p-values of the Total Number 
 * of Runs test is provided.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class Wald_WolfowitzTest extends StatisticalTest{

	private TotalNumberOfRunsDistribution distribution;
	
	/**
	 * Data to analyze
	 */
	private DataTable data;
	
	/**
	 * First sample
	 */
	private double sample1 [];
	
	/**
	 * Second sample
	 */
	private double sample2 [];
	
	/**
	 * Sequence of combined values
	 */
	private StringSequence sequence;
	
	/**
	 * Test statistic (number of runs)
	 */
	private int R;
	
	/**
	 * Exact p-value
	 */
	private double exactPValue;
	
	/**
	 * Asymptotic p-value
	 */
	private double asymptoticPValue;

	/**
	 * Default builder
	 */
	public Wald_WolfowitzTest(){
		
		distribution = TotalNumberOfRunsDistribution.getInstance();
		setReportFormat();
		clearData();
		
	}//end-method
	
	/**
	 * Clears the data stored in the test
	 */
	public void clearData(){
		
		data=new DataTable();
		sequence= new StringSequence();
		
		sample1=null;
		sample2=null;
		
		performed=false;
		dataReady=false;
		
		R=0;
		
		exactPValue=-1.0;
		asymptoticPValue=-1.0;

	}//end-method
	
	/**
	 * Builder. Load data to test.
	 * 
	 * @param newData data to test.
	 */
	public Wald_WolfowitzTest(DataTable newData){

		int nulls;
		int counter1, counter2;
		double value;
		int pointer1,pointer2;
		double value1,value2;
		boolean last=false;
		
		distribution = TotalNumberOfRunsDistribution.getInstance();
		setReportFormat();
		
		data=DataTable.newInstance(newData);
		
		if(data.getColumns()!=2){
			System.out.println("Wald-Wolfowitz test only can be employed with two samples");
			clearData();
			return;
		}
		
		nulls=data.getColumnNulls(0);
		sample1=new double [data.getRows()-nulls];
		nulls=data.getColumnNulls(1);
		sample2=new double [data.getRows()-nulls];
		
		counter1=0;
		counter2=0;
		
		for(int i=0;i<data.getRows();i++){
			
			value=data.get(i, 0);
			if(value!=DataDefinitions.NULL_VALUE){
				sample1[counter1]=value;
				counter1++;
			}
			
			value=data.get(i, 1);
			if(value!=DataDefinitions.NULL_VALUE){
				sample2[counter2]=value;
				counter2++;
			}
			
		}
		
		pointer1=0;
		pointer2=0;
		
		Arrays.sort(sample1);
		Arrays.sort(sample2);
		
		sequence = new StringSequence();
		
		while((pointer1<data.getRows())&&(pointer2<data.getRows())){
			value1=data.get(pointer1, 0);
			value2=data.get(pointer2, 1);

			if(value1<value2){
				sequence.append("X");
				pointer1++;
				last=true;
			}
			if(value1>value2){
				sequence.append("Y");
				pointer2++;
				last=false;
			}
			
			if(value1==value2){
				if(last){
					sequence.append("Y");
					pointer2++;
					last=false;
				}else{
					sequence.append("X");
					pointer1++;
					last=true;
				}

			}
		}
		
		while(pointer1<data.getRows()){
			sequence.append("X");
			pointer1++;
		}
		
		while(pointer2<data.getRows()){
			sequence.append("Y");
			pointer2++;
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

		int nulls;
		int counter1, counter2;
		double value;
		int pointer1,pointer2;
		double value1,value2;
		boolean last=false;
		
		data=DataTable.newInstance(newData);
		
		if(data.getColumns()!=2){
			System.out.println("Wald-Wolfowitz test only can be employed with two samples");
			clearData();
			return;
		}
		
		nulls=data.getColumnNulls(0);
		sample1=new double [data.getRows()-nulls];
		nulls=data.getColumnNulls(1);
		sample2=new double [data.getRows()-nulls];
		
		counter1=0;
		counter2=0;
		
		for(int i=0;i<data.getRows();i++){
			
			value=data.get(i, 0);
			if(value!=DataDefinitions.NULL_VALUE){
				sample1[counter1]=value;
				counter1++;
			}
			
			value=data.get(i, 1);
			if(value!=DataDefinitions.NULL_VALUE){
				sample2[counter2]=value;
				counter2++;
			}
			
		}
		
		pointer1=0;
		pointer2=0;
		
		Arrays.sort(sample1);
		Arrays.sort(sample2);
		
		sequence = new StringSequence();
		
		while((pointer1<data.getRows())&&(pointer2<data.getRows())){
			value1=data.get(pointer1, 0);
			value2=data.get(pointer2, 1);

			if(value1<value2){
				sequence.append("X");
				pointer1++;
				last=true;
			}
			if(value1>value2){
				sequence.append("Y");
				pointer2++;
				last=false;
			}
			
			if(value1==value2){
				if(last){
					sequence.append("Y");
					pointer2++;
					last=false;
				}else{
					sequence.append("X");
					pointer1++;
					last=true;
				}

			}
		}
		
		while(pointer1<data.getRows()){
			sequence.append("X");
			pointer1++;
		}
		
		while(pointer2<data.getRows()){
			sequence.append("Y");
			pointer2++;
		}
		
		dataReady=true;
		performed=false;
		
	}//end-method

	/**
	 * Performs the test
	 */
	public void doTest(){
	
		String c;
		int i;
		
		if(!dataReady){		
			System.out.println("Data is not ready");
			return;	
		}
		
		//compute the number of runs
		R=1;
		
		c=sequence.get(0);
		
		i=1;

		while(i<sequence.size()){
			if(!sequence.get(i).equals(c)){
				R++;
				c=sequence.get(i);
			}
			i++;
		}

		exactPValue=distribution.computeLeftTailProbability(sample1.length, sample2.length, R);
		asymptoticPValue=distribution.computeAsymptoticLeftTailProbability(sample1.length, sample2.length, R);
		
		performed=true;
		
	}//end-method
	
	/**
	 * Get R statistic (number of runs)
	 * 
	 * @return R statistic 
	 */
	public double getR(){
		
		return R;
		
	}//end-method


	/**
	 * Get exact p-value 
	 * 
	 * @return p-value computed
	 */
	public double getExactPValue(){
		
		return exactPValue;
		
	}//end-method
	
	/**
	 * Get asymptotic p-value 
	 * 
	 * @return p-value computed
	 */
	public double getAsymptoticPValue(){
		
		return asymptoticPValue;
		
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
		report+="Wald-Wolfowitz test\n";
		report+="***************************************\n\n";

		report+="R statistic: "+nf6.format(R)+"\n\n";
		
		if(exactPValue!=DistributionDefinitions.UNDEFINED){
			
			report+="Exact P-Value (Left tail, Y > X): "+nf6.format(exactPValue)+"\n";
			
		}
		
		report+="Asymptotic P-Value: "+nf6.format(asymptoticPValue)+"\n";
	
		return report;
			
	}//end-method
		
}//end-class
