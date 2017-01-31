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

package javanpst.tests.oneSample.signTest;

import javanpst.data.structures.dataTable.DataTable;
import javanpst.data.structures.sequence.NumericSequence;
import javanpst.distributions.common.continuous.NormalDistribution;
import javanpst.distributions.common.discrete.BinomialDistribution;
import javanpst.tests.StatisticalTest;

/**
 * The sign test
 * 
 * This is a special case of the Population Quantile test
 * where the quantile is 0.5 (the median) and the hypothesis
 * value is 0.
 * 
 * If a sequence is tested, the null hypothesis will be median=0
 * If a two-column table is analyzed, then the population of 
 * differences will be tested, thus testing which population is greater
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class SignTest extends StatisticalTest{

	/**
	 * Sequence to analyze
	 */
	private NumericSequence sequence;
	
	/**
	 * Binomial distribution
	 */
	private BinomialDistribution binomial;
	
	/**
	 * Normal distribution
	 */
	private NormalDistribution normal;
	
	/**
	 * K Statistic
	 */
	private double K,K2;
	
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
	 * Default builder
	 */
	public SignTest(){
		
		setReportFormat();
		clearData();
		
	}//end-method
	
	/**
	 * Clears the data stored in the test
	 */
	public void clearData(){
		
		K=0;
		K2=0;
		
		exactLeftTail=-1.0;
		exactRightTail=-1.0;
		exactDoubleTail=-1.0;
		asymptoticLeftTail=-1.0;
		asymptoticRightTail=-1.0;
		asymptoticDoubleTail=-1.0;
		
		sequence = new NumericSequence();
		
		dataReady=false;
		performed=false;
		
	}//end-method
	
	/**
	 * Builder. Load data to test.
	 * 
	 * @param newSequence data to test.
	 */
	public SignTest(NumericSequence newSequence){
		
		setReportFormat();
		
		sequence = new NumericSequence(newSequence);
		
		binomial = new BinomialDistribution();
		normal = new NormalDistribution();
		
		binomial.setP(0.5);
		
		dataReady=true;
		performed=false;
		
	}//end-method
	
	/**
	 * Sets data to test
	 * 
	 * @param newSequence data to test.
	 */
	public void setData(NumericSequence newSequence){
		
		sequence = new NumericSequence(newSequence);
		
		binomial = new BinomialDistribution();
		normal = new NormalDistribution();
		
		binomial.setP(0.5);
		
		dataReady=true;
		performed=false;
		
	}//end-method
	
	/**
	 * Builder. Load data from two populations to test.
	 * 
	 * @param newData data to test.
	 */
	public SignTest(DataTable newData){
		
		setReportFormat();
		
		if(newData.getColumns()!=2){
			System.out.println("Sign test only can be employed with two samples");
			clearData();
			return;
		}
		
		for(int i=0;i<newData.getColumns();i++){
			if(newData.getColumnNulls(i)>0){
				System.out.println("No null values allowed in this test.");
				clearData();
				return;
			}
		}
		
		double differences [];
		
		differences = new double [newData.getRows()];
		
		for(int i=0;i<differences.length;i++){
			differences[i]=newData.get(i, 0)-newData.get(i, 1);
		}
		
		sequence = new NumericSequence(differences);
		
		binomial = new BinomialDistribution();
		normal = new NormalDistribution();
		
		binomial.setP(0.5);
		
		dataReady=true;
		performed=false;
		
	}//end-method
	
	/**
	 * Sets data from two populations to test
	 * 
	 * @param newData data to test.
	 */
	public void setData(DataTable newData){
		
		if(newData.getColumns()!=2){
			System.out.println("Sign test only can be employed with two samples");
			clearData();
			return;
		}
		
		for(int i=0;i<newData.getColumns();i++){
			if(newData.getColumnNulls(i)>0){
				System.out.println("No null values allowed in this test.");
				clearData();
				return;
			}
		}
		
		double differences [];
		
		differences = new double [newData.getRows()];
		
		for(int i=0;i<differences.length;i++){
			differences[i]=newData.get(i, 0)-newData.get(i, 1);
		}
		
		sequence = new NumericSequence(differences);
		
		binomial = new BinomialDistribution();
		normal = new NormalDistribution();
		
		binomial.setP(0.5);
		
		dataReady=true;
		performed=false;
		
	}//end-method
	
	/**
	 * Performs the test
	 */
	public void doTest(){

		int draws;
		int effectiveSamples;
		
		if(!dataReady){		
			System.out.println("Data is not ready");
			return;	
		}
		
		//compute K statistics
				
		K=0;
		K2=0;
		draws=0;
		
		for(int i=0;i<sequence.size();i++){
			if(sequence.get(i)>0){
				K++;
			}
			if(sequence.get(i)<0){
				K2++;
			}
			if(sequence.get(i)==0){
				draws++;
			}
		}
				
		//compute p-values
		
		effectiveSamples=sequence.size()-draws;
		binomial.setN(effectiveSamples);
		
		exactLeftTail=binomial.computeCumulativeProbability(effectiveSamples-K2);
		exactRightTail=binomial.computeCumulativeProbability(effectiveSamples-K);
		exactDoubleTail=Math.min(Math.min(exactLeftTail,exactRightTail)*2.0,1.0);
							
		//asymptotic p-values
		double numerator, denominator;
		
		denominator=0.5*Math.sqrt(effectiveSamples);
		
		numerator=((double)K-(0.5*effectiveSamples)+0.5);

		asymptoticLeftTail=normal.getTipifiedProbability(numerator/denominator, false);

		numerator=((double)K-(0.5*effectiveSamples)-0.5);
		asymptoticRightTail=1.0-normal.getTipifiedProbability(numerator/denominator, false);
		
		asymptoticDoubleTail=Math.min(Math.min(asymptoticLeftTail,asymptoticRightTail)*2.0,1.0);
		
		performed=true;

	}//end-method
	
	/**
	 * Get K statistic of the first sample
	 * 
	 * @return K statistic 
	 */
	public double getK(){
		
		return K;
		
	}//end-method
	
	/**
	 * Get K statistic of the second sample
	 * 
	 * @return K statistic 
	 */
	public double getK2(){
		
		return K2;
		
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
			
		text+="\n"+sequence;
			
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
		
		report+="\n*******\n";
		report+="Sign test\n";
		report+="*********\n\n";

		report+="K statistic (sample 1): "+nf6.format(K)+"\n";
		report+="K statistic (sample 2): "+nf6.format(K2)+"\n\n";
					
		report+="Exact P-Value (Left tail, Y > X): "+nf6.format(exactLeftTail)+"\n";
		report+="Exact P-Value (Right tail, Y < X): "+nf6.format(exactRightTail)+"\n";
		report+="Exact P-Value (Double tail, Y != X): "+nf6.format(exactDoubleTail)+"\n\n";
		report+="Asymptotic P-Value (Left tail, Y > X): "+nf6.format(asymptoticLeftTail)+"\n";
		report+="Asymptotic P-Value (Right tail, Y > X): "+nf6.format(asymptoticRightTail)+"\n";
		report+="Asymptotic P-Value (Double tail, Y != X): "+nf6.format(asymptoticDoubleTail)+"\n\n";

		return report;
			
	}//end-method
		
}//end-class
