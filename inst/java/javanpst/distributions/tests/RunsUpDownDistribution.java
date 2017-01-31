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
import javanpst.distributions.common.continuous.NormalDistribution;
import javanpst.data.distributions.incompleteTable.Incomplete2KeyTable;
import javanpst.data.readers.distributionReaders.RunsUpDownReaderFormat;

/**
 * An implementation of the Runs up down distribution.
 * 
 * Uses a Singleton pattern to ensure only an instance of the class is allowed.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class RunsUpDownDistribution extends TestDistribution{

	/**
	 * Inner Normal distribution for approximated p-value
	 */
	private NormalDistribution normal;
	
	/**
	 * Table of the distribution
	 */
	private static Incomplete2KeyTable table;
	
	/**
	 * Critical values for the left tail
	 */
	private static double leftLimits [] = {0,0,0,1,1,2,3,3,4,5,5,6,7,7,8,9,9,10,11,11,12,13,13,14,15,15};
	
	/**
	 * Private default builder.
	 * 
	 * Initializes distribution tables
	 */
	private RunsUpDownDistribution(){
		
		initialize();
		
	}//end-method
	
	/**
	 * Singleton pattern access to the distribution
	 * 
	 * @return the instance of the distribution
	 */
	public static RunsUpDownDistribution getInstance(){
		
		return SingletonHolder.distribution;
		
	}//end-method
	
	/**
	 * Singleton holder for the only instance of the distribution
	 * 
	 * SingletonHolder is loaded on the first execution of getInstance() 
	 * or the first access to SingletonHolder.distribution, not before.
	 */
	private static class SingletonHolder { 
		   
		public static final RunsUpDownDistribution distribution = new RunsUpDownDistribution();
		
	}//end-method
	
	/**
	 * Generate distribution tables
	 */
	protected void generateTables(){
		
		table = new Incomplete2KeyTable(26,25);	
		loadFiles();
		
		normal= new NormalDistribution();

	}//end-method
	
	/**
	 * Load data files to fill distribution tables
	 */
	protected void loadFiles(){
		
		try{
            SAXParserFactory spf=SAXParserFactory.newInstance(); 
            SAXParser sp = spf.newSAXParser();                   
            sp.parse(this.getClass().getResourceAsStream("/tables/RunsUpDown/RunsUpDown.xml"), new RunsUpDownReaderFormat());	            
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
	public static Incomplete2KeyTable getTable(){
		
		return table;
		
	}//end-method
	
	/**
	 * Computes exact p-value of the Runs up down distribution
	 * 
	 * @param n number of elements
	 * @param R Runs up down statistic
	 * @param leftTail true if computing p-value of the left tail, false if right tail
	 * @return p-value computed 
	 */
	public double computeExactProbability(int n,int R, boolean leftTail){
		
		double value;
		
		if((n<3)||(n>25)||(R>=n)||(R<1)){
			return DistributionDefinitions.UNDEFINED;
		}
		
		if(leftTail){
			if(R>leftLimits[n]){
				return DistributionDefinitions.ALL;
			}
		}
		else{
			if(R<=leftLimits[n]){
				return DistributionDefinitions.ALL;
			}
		}

		value=table.get(n, R);

		return value;
		
	}//end-method
	
	/**
	 * Computes asymptotic left tail p-value of the Runs Up Down distribution
	 * 
	 * @param n number of elements in the sequence
	 * @param R total number of runs
	 * @return p-value computed
	 */
	public double computeAsymptoticLeftTailProbability(int n, int R){
	
		double numerator,denominator,z;
		
		denominator=Math.sqrt(((16.0*n)-29.0)/90.0);
		
		numerator=R+0.5-(((2.0*n) - 1.0)/3.0);
		
		z=numerator/denominator;

		return normal.getTipifiedProbability(z, false);
		
	}//end-method
	
	/**
	 * Computes asymptotic left tail p-value of the Runs Up Down distribution
	 * 
	 * @param n number of elements in the sequence
	 * @param R total number of runs
	 * @return p-value computed
	 */
	public double computeAsymptoticRightTailProbability(int n, int R){
		
		double numerator,denominator,z;
		
		denominator=Math.sqrt(((16.0*n)-29.0)/90.0);
		
		numerator=R-0.5-(((2.0*n) - 1.0)/3.0);
		
		z=numerator/denominator;

		return normal.getTipifiedProbability(z, false);
		
	}//end-method
	
	/**
	 * Computes asymptotic double tail p-value of the Runs Up Down distribution
	 * 
	 * @param n number of elements in the sequence
	 * @param R total number of runs
	 * @return p-value computed
	 */
	public double computeAsymptoticDoubleTailProbability(int n, int R){
		
		double numerator,denominator,z;
		
		double leftPValue,rightPValue;
		
		denominator=Math.sqrt(((16.0*n)-29.0)/90.0);
		
		numerator=R-0.5-(((2.0*n) - 1.0)/3.0);
		
		z=numerator/denominator;

		rightPValue=normal.getTipifiedProbability(z, false);
		
		numerator=R+0.5-(((2.0*n) - 1.0)/3.0);
		
		z=numerator/denominator;
	
		normal.getTipifiedProbability(z, false);
		
		leftPValue=normal.getTipifiedProbability(z, false);
		
		return Math.min(Math.min(leftPValue,rightPValue)*2.0, 1.0);
		
	}//end-method
	
}//end-class

