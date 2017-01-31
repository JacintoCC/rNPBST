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
import javanpst.data.distributions.criticalTable.Critical2KeyTable;
import javanpst.data.readers.distributionReaders.PageReaderFormat;


/**
 * An implementation of the Page distribution.
 * 
 * Uses a Singleton pattern to ensure only an instance of the class is allowed.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class PageDistribution extends TestDistribution{
	
	private static NormalDistribution normal;
	
	/**
	 * Table of critical values of the distribution
	 */
	private static Critical2KeyTable table;
	
	/**
	 * Private default builder.
	 * 
	 * Initializes distribution tables
	 */
	private PageDistribution(){
		
		initialize();
		
	}//end-method
	
	/**
	 * Singleton pattern access to the distribution
	 * 
	 * @return the instance of the distribution
	 */
	public static PageDistribution getInstance(){
		
		return SingletonHolder.distribution;
		
	}//end-method
	
	/**
	 * Singleton holder for the only instance of the distribution
	 * 
	 * SingletonHolder is loaded on the first execution of getInstance() 
	 * or the first access to SingletonHolder.distribution, not before.
	 */
	private static class SingletonHolder { 
		   
		public static final PageDistribution distribution = new PageDistribution();
		
	}//end-method
	
	/**
	 * Generate distribution tables
	 */
	protected void generateTables(){
		
		double [] tags={0.001,0.01,0.05};
		
		table=new Critical2KeyTable(13,9,tags);
		
		loadFiles();
		
		normal=new NormalDistribution();

	}//end-method
	
	/**
	 * Load data files to fill distribution tables
	 */
	protected void loadFiles(){
		
		try{
            SAXParserFactory spf=SAXParserFactory.newInstance(); 
            SAXParser sp = spf.newSAXParser();       
            sp.parse(this.getClass().getResourceAsStream("/tables/Page/PageTest.xml"), new PageReaderFormat());	            
        }catch(ParserConfigurationException e){                  
        	System.err.println("Parser error");             
        }catch(SAXException e2){                                 
        	System.err.println("SAX error: " + e2.getStackTrace());
        } catch (IOException e3) {
        	System.err.println("Input/Output error: " + e3.getMessage() );
        }
        
	}//end-method
	
	/**
	 * Gets the table of critical values of the distribution
	 * 
	 * @return Reference to the table
	 */
	public static Critical2KeyTable getTable(){
		
		return table;
		
	}//end-method
	
	/**
	 * Computes Exact p-value of the Page distribution.
	 * 
	 * @param n number of columns
	 * @param k number of rows
	 * @param L Page statistic
	 * @return p-value computed
	 */
	public double computeExactProbability(int n,int k,double L){
		
		if((n<=8)&&(n>=3)&&(k<=12)&&(k>=2)){
			return table.estimate(k, n, L);
		}
		
		return DistributionDefinitions.UNDEFINED;

	}//end-method
	
	/**
	 * Computes asymptotic p-value of the Page distribution.
	 * 
	 * @param n number of columns
	 * @param k number of rows
	 * @param L Page statistic
	 * @return p-value computed
	 */
	public double computeAsymptoticProbability(int n,int k,double L){
		
		double Z,numerator, denominator; 
		
		numerator=12.0*(L-0.5)-(3.0*k*n*(n+1.0)*(n+1.0));
		denominator=n*(n+1)*Math.sqrt(k*(n-1.0));
		
		Z=numerator/denominator;

		return normal.getTipifiedProbability(Z, true);

	}//end-method
	
}//end-class
