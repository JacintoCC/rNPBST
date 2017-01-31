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
import javanpst.data.distributions.criticalTable.Critical1KeyTable;
import javanpst.data.readers.distributionReaders.RVNDistributionReaderFormat;

/**
 * An implementation of the Ranks Von Newmann distribution.
 * 
 * Uses a Singleton pattern to ensure only an instance of the class is allowed.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class RVNDistribution extends TestDistribution{
	
	/**
	 * Table of critical values
	 */
	private static Critical1KeyTable table;
	
	/**
	 * Private default builder.
	 * 
	 * Initializes distribution tables
	 */
	private RVNDistribution(){
		
		initialize();
		
	}//end-method
	
	/**
	 * Singleton pattern access to the distribution
	 * 
	 * @return the instance of the distribution
	 */
	public static RVNDistribution getInstance(){
		
		return SingletonHolder.distribution;
		
	}//end-method
	
	/**
	 * Singleton holder for the only instance of the distribution
	 * 
	 * SingletonHolder is loaded on the first execution of getInstance() 
	 * or the first access to SingletonHolder.distribution, not before.
	 */
	private static class SingletonHolder { 
		   
		public static final RVNDistribution distribution = new RVNDistribution();
		
	}//end-method
	
	/**
	 * Generate distribution tables
	 */
	protected void generateTables(){
		
		double [] tags={0.005,0.010,0.025,0.050,0.100};
		
		table=new Critical1KeyTable(101,tags);
		loadFiles();

	}//end-method
	
	/**
	 * Load data files to fill distribution tables
	 */
	protected void loadFiles(){
		
		try{
            SAXParserFactory spf=SAXParserFactory.newInstance(); 
            SAXParser sp = spf.newSAXParser();                   
            sp.parse(this.getClass().getResourceAsStream("/tables/RanksVonNeumann/RanksVonNeumann.xml"), new RVNDistributionReaderFormat());	            
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
	public static Critical1KeyTable getTable(){
		
		return table;
		
	}//end-method

	/**
	 * Computes left tail p-value of the Ranks Von Newmann distribution.
	 * 
	 * @param n size of the sequence
	 * @param RVN Ranks Von Newmann statistic
	 * @return p-value computed
	 */
	public double computeLeftProbability(int n,double RVN){

		if(table.get(n, 0)==DistributionDefinitions.UNDEFINED){
			return DistributionDefinitions.UNDEFINED;
		}
		
		for(int i=0;i<5;i++){
			if(RVN<=table.get(n, i)){
				return table.getHeader(i);
			}
		}
		
		return DistributionDefinitions.ALL;
		
	}//end-method
	
	/**
	 * Computes right tail p-value of the Ranks Von Newmann distribution.
	 * 
	 * @param n size of the sequence
	 * @param RVN Ranks Von Newmann statistic
	 * @return p-value computed
	 */
	public double computeRightProbability(int n,double RVN){

		if(table.get(n, 0)==DistributionDefinitions.UNDEFINED){
			return DistributionDefinitions.UNDEFINED;
		}
		
		for(int i=0;i<5;i++){
			if(RVN>=(2.0-table.get(n, i)+2.0)){
				return table.getHeader(i);
			}
		}
		
		return DistributionDefinitions.ALL;
		
	}//end-method
	
}//end-class

