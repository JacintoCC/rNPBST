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
import javanpst.data.distributions.incompleteTable.Incomplete3KeyTable;
import javanpst.data.readers.distributionReaders.TotalNumberOfRunsReaderFormat;

/**
 * An implementation of the Total number of runs test distribution.
 * 
 * Uses a Singleton pattern to ensure only an instance of the class is allowed.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class TotalNumberOfRunsDistribution extends TestDistribution{

	/**
	 * Inner Normal distribution for approximated p-value
	 */
	private NormalDistribution normal;
	
	/**
	 * Left tail table of the distribution
	 */
	private static Incomplete3KeyTable tableLeft;
	
	/**
	 * Right tail table of the distribution
	 */
	private static Incomplete3KeyTable tableRight;
	
	/**
	 * Private default builder.
	 * 
	 * Initializes distribution tables
	 */
	private TotalNumberOfRunsDistribution(){
		
		initialize();
		
	}//end-method
	
	/**
	 * Singleton pattern access to the distribution
	 * 
	 * @return the instance of the distribution
	 */
	public static TotalNumberOfRunsDistribution getInstance(){
		
		return SingletonHolder.distribution;
		
	}//end-method
	
	/**
	 * Singleton holder for the only instance of the distribution
	 * 
	 * SingletonHolder is loaded on the first execution of getInstance() 
	 * or the first access to SingletonHolder.distribution, not before.
	 */
	private static class SingletonHolder { 
		   
		public static final TotalNumberOfRunsDistribution distribution = new TotalNumberOfRunsDistribution();
		
	}//end-method
	
	/**
	 * Generate distribution tables
	 */
	protected void generateTables(){
		
		tableLeft = new Incomplete3KeyTable(13,19,13);	
		tableRight = new Incomplete3KeyTable(13,18,25);
		
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
            sp.parse(this.getClass().getResourceAsStream("/tables/TotalNumberOfRuns/TotalNumberOfRuns-Left.xml"), new TotalNumberOfRunsReaderFormat(TotalNumberOfRunsReaderFormat.LEFT));	            
        }catch(ParserConfigurationException e){                  
        	System.err.println("Parser error");             
        }catch(SAXException e2){                                 
        	System.err.println("SAX error: " + e2.getStackTrace());
        } catch (IOException e3) {
        	System.err.println("Input/Output error: " + e3.getMessage() );
        }
        
        try{
            SAXParserFactory spf=SAXParserFactory.newInstance(); 
            SAXParser sp = spf.newSAXParser();                   
            sp.parse(this.getClass().getResourceAsStream("/tables/TotalNumberOfRuns/TotalNumberOfRuns-Right.xml"), new TotalNumberOfRunsReaderFormat(TotalNumberOfRunsReaderFormat.RIGHT));	            
        }catch(ParserConfigurationException e){                  
        	System.err.println("Parser error");             
        }catch(SAXException e2){                                 
        	System.err.println("SAX error: " + e2.getStackTrace());
        } catch (IOException e3) {
        	System.err.println("Input/Output error: " + e3.getMessage() );
        }
        
	}//end-method
	
	/**
	 * Gets the left tail table of the distribution
	 * 
	 * @return Reference to the table
	 */
	public static Incomplete3KeyTable getTableLeft(){
		
		return tableLeft;
		
	}//end-method
	
	/**
	 * Gets the right tail table of the distribution
	 * 
	 * @return Reference to the table
	 */
	public static Incomplete3KeyTable getTableRight(){
		
		return tableRight;
		
	}//end-method
	
	/**
	 * Computes left tail p-value of the Total number of runs distribution
	 * 
	 * @param a number of elements of the first type
	 * @param b number of elements of the second type
	 * @param R total number of runs
	 * @return p-value computed
	 */
	public double computeLeftTailProbability(int a, int b, int R){
		
		int n1=Math.min(a, b);
		int n2=Math.max(a, b);
		
		double leftTail,rightTail;
		
		if((n1>12)||((n1<9)&&(n1+n2>20))||((n1>8)&&(n2>12))){
			return DistributionDefinitions.UNDEFINED;
		}

		leftTail=tableLeft.get(n1, n2, R);
		rightTail=tableRight.get(n1, n2, R+1);
		
		if((leftTail==DistributionDefinitions.UNDEFINED)&&(rightTail==DistributionDefinitions.UNDEFINED)){
			return DistributionDefinitions.UNDEFINED;
		}
		
		if(leftTail==DistributionDefinitions.UNDEFINED){
			leftTail=1.0-rightTail;
		}
		
		return leftTail;
		
	}//end-method
	
	/**
	 * Computes right tail p-value of the Total number of runs distribution
	 * 
	 * @param a number of elements of the first type
	 * @param b number of elements of the second type
	 * @param R total number of runs
	 * @return p-value computed
	 */
	public double computeRightTailProbability(int a, int b, int R){
				
		int n1=Math.min(a, b);
		int n2=Math.max(a, b);

		double leftTail,rightTail;
		
		if((n1>12)||((n1<10)&&(n1+n2>20))||((n1>9)&&(n2>12))){
			return DistributionDefinitions.UNDEFINED;
		}

		leftTail=tableLeft.get(n1, n2, R-1);
		rightTail=tableRight.get(n1, n2, R);
		
		if((leftTail==DistributionDefinitions.UNDEFINED)&&(rightTail==DistributionDefinitions.UNDEFINED)){
			return DistributionDefinitions.UNDEFINED;
		}
		
		if(rightTail==DistributionDefinitions.UNDEFINED){
			rightTail=1.0-leftTail;
		}
		
		return rightTail;
		
	}//end-method
	
	/**
	 * Computes asymptotic left tail p-value of the Total number of runs distribution
	 * 
	 * @param a number of elements of the first type
	 * @param b number of elements of the second type
	 * @param R total number of runs
	 * @return p-value computed
	 */
	public double computeAsymptoticLeftTailProbability(int a, int b, int R){

		double n1= a;
		double n2 = b;
		double n = n1+n2;
		
		double numerator,denominator,z;
		
		denominator=Math.sqrt(2*n1*n2*(2*n1*n2-n)/(n*n*(n-1)));
		
		numerator=R+0.5-1-2*n1*n2/n;
		
		z=numerator/denominator;

		return normal.getTipifiedProbability(z, false);
		
	}//end-method
	
	/**
	 * Computes asymptotic left tail p-value of the Total number of runs distribution
	 * 
	 * @param a number of elements of the first type
	 * @param b number of elements of the second type
	 * @param R total number of runs
	 * @return p-value computed
	 */
	public double computeAsymptoticRightTailProbability(int a, int b, int R){

		double n1= a;
		double n2 = b;
		double n = n1+n2;
		
		double numerator,denominator,z;
		
		denominator=Math.sqrt(2*n1*n2*(2*n1*n2-n)/(n*n*(n-1)));
		
		numerator=R-0.5-1-2*n1*n2/n;
		
		z=numerator/denominator;

		return normal.getTipifiedProbability(z, true);
		
	}//end-method
	
	/**
	 * Computes asymptotic double tail p-value of the Total number of runs distribution
	 * 
	 * @param a number of elements of the first type
	 * @param b number of elements of the second type
	 * @param R total number of runs
	 * @return p-value computed
	 */
	public double computeAsymptoticDoubleTailProbability(int a, int b, int R){

		double n1= a;
		double n2 = b;
		double n = n1+n2;
		
		double numerator,denominator,z;
		
		double leftPValue,rightPValue;
		
		denominator=Math.sqrt(2*n1*n2*(2*n1*n2-n)/(n*n*(n-1)));
		
		numerator=R-0.5-1-2*n1*n2/n;
		
		z=numerator/denominator;

		rightPValue=normal.getTipifiedProbability(z, true);
		
		numerator=R+0.5-1-2*n1*n2/n;
		
		z=numerator/denominator;

		normal.getTipifiedProbability(z, false);
		
		leftPValue=normal.getTipifiedProbability(z, false);
		
		return Math.min(Math.min(leftPValue,rightPValue)*2.0, 1.0);
		
	}//end-method

}//end-class
