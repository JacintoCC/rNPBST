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

package javanpst.distributions.tests;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import javanpst.distributions.DistributionDefinitions;
import javanpst.data.distributions.incompleteTable.Incomplete3KeyTable;
import javanpst.data.readers.distributionReaders.WilcoxonRankSumReaderFormat;


/**
 * An implementation of the Wilcoxon Ranks-Sum distribution.
 * 
 * Uses a Singleton pattern to ensure only an instance of the class is allowed.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class WilcoxonRankSumDistribution extends TestDistribution{
	
	/**
	 * Table of the distribution
	 */
	private static Incomplete3KeyTable table;
	
	/**
	 * Array with maximum values of R for each n and m pair
	 */
	private int maximum[][]={{1,2,2,3,3,4,4,5,5,6},
							{0,5,6,7,8,9,10,11,12,13},
							{0,0,10,12,13,15,16,18,19,21},
							{0,0,0,18,20,22,24,26,28,30},
							{0,0,0,0,27,30,32,35,37,40},
							{0,0,0,0,0,39,42,45,48,51},
							{0,0,0,0,0,0,52,56,59,63},
							{0,0,0,0,0,0,0,68,72,76},
							{0,0,0,0,0,0,0,0,85,90},
							{0,0,0,0,0,0,0,0,0,105},};	
	
	/**
	 * Private default builder.
	 * 
	 * Initializes distribution tables
	 */
	private WilcoxonRankSumDistribution(){
		
		initialize();
		
	}//end-method
	
	/**
	 * Singleton pattern access to the distribution
	 * 
	 * @return the instance of the distribution
	 */
	public static WilcoxonRankSumDistribution getInstance(){
		
		return SingletonHolder.distribution;
		
	}//end-method
	
	/**
	 * Singleton holder for the only instance of the distribution
	 * 
	 * SingletonHolder is loaded on the first execution of getInstance() 
	 * or the first access to SingletonHolder.distribution, not before.
	 */
	private static class SingletonHolder { 
		   
		public static final WilcoxonRankSumDistribution distribution = new WilcoxonRankSumDistribution();
		
	}//end-method
	
	/**
	 * Generate distribution tables
	 */
	protected void generateTables(){
		
		table = new Incomplete3KeyTable(11,11,106);	
		
		loadFiles();

	}//end-method
	
	/**
	 * Load data files to fill distribution tables
	 */
	protected void loadFiles(){
		
		try{
            SAXParserFactory spf=SAXParserFactory.newInstance(); 
            SAXParser sp = spf.newSAXParser();                   
            sp.parse(this.getClass().getResourceAsStream("/tables/Wilcoxon/WilcoxonRanksSumTable.xml"), new WilcoxonRankSumReaderFormat());	            
        }catch(ParserConfigurationException e){                  
        	System.err.println("Parser error");             
        }catch(SAXException e2){                                 
        	System.err.println("SAX error: " + e2.getStackTrace());
        } catch (IOException e3) {
        	System.err.println("Input/Output error: " + e3.getMessage() );
        }
        
	}//end-method
	
	/**
	 * Gets the table of the distribution
	 * 
	 * @return Reference to the table
	 */
	public static Incomplete3KeyTable getTable(){
		
		return table;
		
	}//end-method
	
	/**
	 * Computes left tail probability of the Wilcoxon Ranks-Sum distribution.
	 * 
	 * @param n n parameter
	 * @param m m parameter
	 * @param R rank associated
	 * @return p-value associated
	 */
	public double computeLeftProbability(int n, int m, int R){
		
		if((n>10)||(m>10)||(R>105)){
			return DistributionDefinitions.UNDEFINED;
		}

		return table.get(n, m, R);
		
	}//end-method
	
	/**
	 * Computes right tail probability of the Wilcoxon Ranks-Sum distribution.
	 * 
	 * @param n n parameter
	 * @param m m parameter
	 * @param R rank associated
	 * @return p-value associated
	 */
	public double computeRightProbability(int n, int m, int R){
		
		int trueR;
		
		initialize();
		
		if((n>10)||(m>10)||(R>105)){
			return DistributionDefinitions.UNDEFINED;
		}
		
	
		int max=maximum[n-1][m-1];
		
		if(R<=max){
			return DistributionDefinitions.UNDEFINED;
		}
		
		if(max%2==0){
			trueR=(2*max)-R;
		}
		else{
			trueR=(2*max)-(R-1);
		}
		
		return table.get(n, m, trueR);
		
	}//end-method
	
	/**
	 * Finds the maximum rank whose p-value associated is equal o less than alpha/2
	 * 
	 * This method is used for computing confidence interval boundaries
	 * 
	 * @param n n parameter
	 * @param m m parameter
	 * @param alpha alpha confidence required
	 * @return rank found
	 */
	public int findCriticalValue(int n, int m, double alpha){
		
		int minimun=0;
		double value=0.0;
		int rank=-1;
		
		alpha/=2.0;
		for(int i=1;i<=m;i++){
			minimun+=i;
		}
		
		while(value<alpha){
			value=table.get(m, n, minimun);
			minimun++;
			rank++;
		}
		
		return rank;
		
	}//end-method
	
	/**
	 * Finds the p-value associated to a rank, provided that it exists 
	 * 
	 * This method is used for computing exact confidence for confidence intervals 
	 * 
	 * @param n n parameter
	 * @param m m parameter
	 * @param u rank selected
	 * @return confidence associated
	 */
	public double inverseFindCriticalValue(int n, int m, int u){

		int minimun=0;
		double value=0.0;
	
		for(int i=1;i<=m;i++){
			minimun+=i;
		}
	
		value=table.get(m, n, minimun+u-1);
		value*=2.0;
		
		return value;
		
	}//end-method
	
}//end-class
