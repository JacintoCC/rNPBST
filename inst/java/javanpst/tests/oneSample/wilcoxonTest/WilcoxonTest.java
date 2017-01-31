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

package javanpst.tests.oneSample.wilcoxonTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javanpst.data.structures.dataTable.DataTable;
import javanpst.distributions.DistributionDefinitions;
import javanpst.distributions.tests.WilcoxonDistribution;
import javanpst.tests.StatisticalTest;

/**
 * The Wilcoxon Signed-Ranks test
 * 
 * This test is proposed for use with paired-sample data 
 * in making inferences concerning the value of the median 
 * of the population of differences.
 * 
 * Zero differences are shared between both samples. If there 
 * is a odd number of them, one is discarded. Ties are handled
 * by the midrank method.
 * 
 * An exact p-value (for p-values equal or lower than 0.2, and
 * sample size equal or lower than 50) is provided. An
 * asymptotic approximation to the normal distribution is 
 * also provided.
 * 
 * Furthermore, this version of the test also provides two
 * confidence intervals (at 0.90 and 0.95 levels) for the
 * location of the median of the differences, through the use of
 * Walsh averages.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class WilcoxonTest extends StatisticalTest{

	/**
	 * Distribution of the test
	 */
	private WilcoxonDistribution distribution;
	
	/**
	 * Data to analyze
	 */
	private DataTable data;
	
	/**
	 * Wilcoxon ranks
	 */
	private double rPlus, rMinus;

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
	 * Upper limit of confidence intervals
	 */
	double upper90, upper95;
	
	/**
	 * Upper limit of confidence intervals
	 */
	double lower90, lower95;
	
	/**
	 * Exact confidence
	 */
	double exactConfidence90,exactConfidence95;
	
	/**
	 * Default builder
	 */
	public WilcoxonTest(){
		
		distribution=WilcoxonDistribution.getInstance();
		setReportFormat();
		clearData();
		
	}//end-method
	
	/**
	 * Clears the data stored in the test
	 */
	public void clearData(){
		
		rPlus=0.0;
		rMinus=0.0;
		
		upper90=0.0;
		upper95=0.0;
		lower90=0.0;
		lower95=0.0;
		
		exactLeftTail=-1.0;
		exactRightTail=-1.0;
		exactDoubleTail=-1.0;
		asymptoticLeftTail=-1.0;
		asymptoticRightTail=-1.0;
		asymptoticDoubleTail=-1.0;
		
		data = new DataTable();
		
		dataReady=false;
		performed=false;
		
	}//end-method
	
	/**
	 * Builder. Load data to test.
	 * 
	 * @param newData data to test.
	 */
	public WilcoxonTest(DataTable newData){
		
		distribution=WilcoxonDistribution.getInstance();	
		setReportFormat();
		
		data=DataTable.newInstance(newData);
		
		if(data.getColumns()!=2){
			System.out.println("Wilcoxon test only can be employed with two samples");
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
		
		dataReady=true;
		performed=false;
		
	}//end-method
	
	/**
	 * Load data to test.
	 * 
	 * @param newData data to test.
	 */
	public void setData(DataTable newData){
		
		data=DataTable.newInstance(newData);
		
		if(data.getColumns()!=2){
			System.out.println("Wilcoxon test only can be employed with two samples");
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
		
		dataReady=true;
		performed=false;
		
	}//end-method
	
	/**
	 * Performs the test
	 */
	public void doTest(){
		
		double AOld [],A [];
		double BOld [],B [];
		double diffOld [], diff [];
		int zeroDifferences,N,pointer;
		boolean sign[];
		double ranks[];
		ArrayList<Double> walsh;
		double tiesWeight;
		int criticalN;
		
		if(!dataReady){		
			System.out.println("Data is not ready");
			return;	
		}
		
		AOld=new double[data.getRows()];
		BOld=new double[data.getRows()];
		diffOld=new double[data.getRows()];
		
		zeroDifferences=0;
		tiesWeight=0.0;

		//compute population of differences
		for(int i=0;i<data.getRows();i++){
			
			AOld[i]=data.get(i, 0);
			BOld[i]=data.get(i, 1);
			
			diffOld[i]=Math.abs(AOld[i]-BOld[i]);

			if(diffOld[i]==0.0){
				zeroDifferences++;
			}
		}

		//new number of values (ties are ignored)
		N=data.getRows()-zeroDifferences;
		
		A=new double[N];
		B=new double[N];
		diff=new double[N];
		sign=new boolean[N];
		ranks=new double[N];
		
		pointer=0;

		for(int i=0;i<data.getRows();i++){
		
			if(diffOld[i]!=0.0){
				A[pointer]=AOld[i];
				B[pointer]=BOld[i];
				diff[pointer]=Math.abs(A[pointer]-B[pointer]);
				if((A[pointer]-B[pointer])>0.0){
					sign[pointer]=true;
				}
				else{
					sign[pointer]=false;
				}
				pointer++;
			}
		
		}

		//compute ranks
		double min;
		double points;
		int tied;
		
		Arrays.fill(ranks, -1.0);

		for(int rank=1;rank<=N;){
			
			min=Double.MAX_VALUE;
			tied=1;
			
			for(int i=0;i<N;i++){
				if((ranks[i]==-1.0)&&diff[i]==min){
					tied++;
				}
				if((ranks[i]==-1.0)&&diff[i]<min){
					min=diff[i];
					tied=1;
				}
				
			}
			
			//min has the lower unassigned value
			
			//compute new ranks to assign (points)
			if(tied==1){
				points=rank;
			}
			else{
				points=0.0;
				for(int k=0;k<tied;k++){
					points+=(rank+k);
				}
				points/=tied;
				
				tiesWeight+=tied*((tied*tied)-1.0);
			}
			
			//assign new ranks
			for(int i=0;i<N;i++){
				if(diff[i]==min){
					ranks[i]=points;
				}
			}
			
			rank+=tied;
			
		}//end of assignation of ranks
		
		//compute sum of ranks for each sample
		
		rPlus=0.0; 
		rMinus=0.0;
		
		for(int i=0;i<ranks.length;i++){
			if(sign[i]){
				rPlus+=ranks[i];
			}
			else{
				rMinus+=ranks[i];
			}
		}

		//Treatment of zero differences
		double increment;
		double sum0;
		
		if(zeroDifferences>1){
			//If there is a odd number of them, one is discarded.
			if(zeroDifferences%2==1){
				increment=zeroDifferences-1.0;
			}
			else{
				increment=zeroDifferences;
			}
			
			//Addition of ranks due to zero differences
			//(e.g if 2 zero differences were added, the sum of these ranks is:
			//((2+1)*(2))/2 = 3, thus 1.5 points are added to each statistic)
			sum0=(((double)zeroDifferences+1.0)*(double)zeroDifferences)/2.0;
			sum0/=2.0;
			
			rPlus+=sum0;
			rMinus+=sum0;
			
			//Scaling the rest of ranks 
			//(e.g if 2 zero differences were added, each rank must be increased by 2)
			for(int i=0;i<ranks.length;i++){
				if(sign[i]){
					rPlus+=increment;
				}
				else{
					rMinus+=increment;
				}
			}
			
		}//end of compute ranks

		//compute exact p-values (distribution is symmetric)
		
		double aux1,aux2;
		
		aux1=distribution.computeExactProbability(N, rPlus);
		aux2=distribution.computeExactProbability(N, rMinus);
		
		if((aux1==1.0)&&(aux2==1.0)){
			exactLeftTail=1.0;
			exactRightTail=1.0;
			exactDoubleTail=1.0;
		}
		else{
			if((aux1!=1.0)){
				exactDoubleTail=aux1;
				exactLeftTail=exactDoubleTail/2.0;
				exactRightTail=1.0-exactLeftTail;
			}
			else{
				exactDoubleTail=aux2;
				exactRightTail=exactDoubleTail/2.0;
				exactLeftTail=1.0-exactRightTail;
			}
		}
		
		//compute asymptotic p-values
		asymptoticLeftTail=distribution.computeAsymptoticLeftTailProbability(N, rPlus, tiesWeight);
		asymptoticRightTail=distribution.computeAsymptoticRightTailProbability(N, rPlus, tiesWeight);
		asymptoticDoubleTail=distribution.computeAsymptoticDoubleTailProbability(N, rPlus, tiesWeight);
		
		//compute confidence intervals
		walsh=new ArrayList<Double>();
		
		double aux;
		for(int i=0;i<diffOld.length-1;i++){
			aux=AOld[i]-BOld[i];
			walsh.add(aux);
			for(int j=i+1;j<diffOld.length;j++){
				aux2=AOld[j]-BOld[j];
				walsh.add((aux+aux2)/2.0);
			}
		}
		
		Collections.sort(walsh);
		
		//Find critical levels
		
		criticalN=findCriticalValue(diffOld.length,0.05);
		criticalN=Math.max(criticalN,0);
		
		//Build confidence intervals

		lower95=walsh.get(criticalN);
		upper95=walsh.get(walsh.size()-(criticalN+1));

		exactConfidence95=1.0-(distribution.computeExactProbability(diffOld.length,criticalN));

		criticalN=findCriticalValue(diffOld.length,0.1);
		
		lower90=walsh.get(criticalN);
		upper90=walsh.get(walsh.size()-(criticalN+1));
		
		exactConfidence90=1.0-(distribution.computeExactProbability(diffOld.length,criticalN));
		
		performed=true;

	}//end-method
	
	/**
	 * Find greatest rank whose p-value is lower than alpha
	 * 
	 * @param N number of elements
	 * @param alpha alpha value
	 * @return greatest rank
	 */
	private int findCriticalValue(int N, double alpha){
		
		double limit=alpha;
		int critical=-1;
		
		do{
			critical++;
		}while(distribution.computeExactProbability(N, critical)<limit);
		
		critical--;
		
		return critical;
		
	}//end-method

	/**
	 * Get R+ statistic of the first sample
	 * 
	 * @return R+ statistic 
	 */
	public double getRPlus(){
		
		return rPlus;
		
	}//end-method
	
	/**
	 * Get R- statistic of the second sample
	 * 
	 * @return R- statistic 
	 */
	public double getRMinus(){
		
		return rMinus;
		
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
		
		report+="\n************\n";
		report+="Wilcoxon test\n";
		report+="************\n\n";

		report+="R+ statistic (sample 1): "+nf6.format(rPlus)+"\n";
		report+="R- statistic (sample 2): "+nf6.format(rMinus)+"\n\n";
		
		if(exactDoubleTail!=DistributionDefinitions.UNDEFINED){
			report+="Exact P-Value (Left tail, Y > X): "+nf6.format(exactLeftTail)+"\n";
			report+="Exact P-Value (Right tail, Y < X): "+nf6.format(exactRightTail)+"\n";
			report+="Exact P-Value (Double tail, Y != X): "+nf6.format(exactDoubleTail)+"\n\n";
		}
		
		report+="Asymptotic P-Value (Left tail, Y > X): "+nf6.format(asymptoticLeftTail)+"\n";
		report+="Asymptotic P-Value (Right tail, Y > X): "+nf6.format(asymptoticRightTail)+"\n";
		report+="Asymptotic P-Value (Double tail, Y != X): "+nf6.format(asymptoticDoubleTail)+"\n\n";

		report+="Confidence intervals:\n\n";
		report+="90% confidence interval: ["+nf6.format(lower90)+","+nf6.format(upper90)+"], exact confidence: "+nf6.format(exactConfidence90)+"\n";
		report+="95% confidence interval: ["+nf6.format(lower95)+","+nf6.format(upper95)+"], exact confidence: "+nf6.format(exactConfidence95)+"\n";
		
		return report;
			
	}//end-method
		
}//end-class
