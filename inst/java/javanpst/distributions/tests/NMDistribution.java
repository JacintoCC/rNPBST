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
import javanpst.data.distributions.incompleteTable.Incomplete2KeyTable;
import javanpst.data.readers.distributionReaders.NMDistributionReaderFormat;

/**
 * An implementation of the NM distribution.
 * 
 * Uses a Singleton pattern to ensure only an instance of the class is allowed.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class NMDistribution extends TestDistribution{
	
	/**
	 * Table of the left tail of the distribution
	 */
	private static Incomplete2KeyTable leftTable;
	
	/**
	 * Table of the right tail of the distribution
	 */
	private static Incomplete2KeyTable rightTable;
	
	/**
	 * Flag showing if the last estimation was approximate
	 */
	private static boolean approximate;
	
	/**
	 * Private default builder.
	 * 
	 * Initializes distribution tables
	 */
	private NMDistribution(){
		
		initialize();
		
	}//end-method
	
	/**
	 * Singleton pattern access to the distribution
	 * 
	 * @return the instance of the distribution
	 */
	public static NMDistribution getInstance(){
		
		return SingletonHolder.distribution;
		
	}//end-method
	
	/**
	 * Singleton holder for the only instance of the distribution
	 * 
	 * SingletonHolder is loaded on the first execution of getInstance() 
	 * or the first access to SingletonHolder.distribution, not before.
	 */
	private static class SingletonHolder { 
		   
		public static final NMDistribution distribution = new NMDistribution();
		
	}//end-method
	
	/**
	 * Generate distribution tables
	 */
	protected void generateTables(){
		
		leftTable = new Incomplete2KeyTable(11,285);	
		rightTable = new Incomplete2KeyTable(11,285);
		
		loadFiles();

	}//end-method
	
	/**
	 * Load data files to fill distribution tables
	 */
	protected void loadFiles(){
		
		try{
            SAXParserFactory spf=SAXParserFactory.newInstance(); 
            SAXParser sp = spf.newSAXParser();                   
            sp.parse(this.getClass().getResourceAsStream("/tables/RanksVonNeumann/NMRanksLeft.xml"), new NMDistributionReaderFormat(NMDistributionReaderFormat.LEFT));	            
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
            sp.parse(this.getClass().getResourceAsStream("/tables/RanksVonNeumann/NMRanksRight.xml"), new NMDistributionReaderFormat(NMDistributionReaderFormat.RIGHT));	            
        }catch(ParserConfigurationException e){                  
        	System.err.println("Parser error");             
        }catch(SAXException e2){                                 
        	System.err.println("SAX error: " + e2.getStackTrace());
        } catch (IOException e3) {
        	System.err.println("Input/Output error: " + e3.getMessage() );
        }
        
	}//end-method
	
	/**
	 * Gets the table of the left tail of the distribution
	 * 
	 * @return Reference to the table
	 */
	public static Incomplete2KeyTable getTableLeft(){
		
		return leftTable;
		
	}//end-method

	/**
	 * Gets the table of the right tail of the distribution
	 * 
	 * @return Reference to the table
	 */
	public static Incomplete2KeyTable getTableRight(){
		
		return rightTable;
		
	}//end-method
	
	/**
	 * Computes left tail p-value of the NM distribution.
	 * 
	 * @param n length of the sequence
	 * @param realNM NM statistic
	 * @return p-value computed
	 */
	public double computeLeftProbability(int n,double realNM){
		
		int NM;
		double prob;

		NM=(int)Math.ceil(realNM);
		
		prob=leftTable.get(n, NM);
		
		if(prob!=DistributionDefinitions.UNDEFINED){
			approximate=false;
			return prob;
		}
		
		for(int i=NM+1;i<285 && prob==DistributionDefinitions.UNDEFINED;i++){
			prob=leftTable.get(n, i);			
		}
		
		if(prob==DistributionDefinitions.UNDEFINED){
			approximate=true;
			return 1.0;
		}
		else{
			approximate=true;
			return prob;
		}

	}//end-method
	
	/**
	 * Computes right tail p-value of the NM distribution.
	 * 
	 * @param n length of the sequence
	 * @param realNM NM statistic
	 * @return p-value computed
	 */
	public double computeRightProbability(int n,double realNM){
		
		int NM;
		double prob;
	
		NM=(int)Math.floor(realNM);
		
		prob=rightTable.get(n, NM);
		
		if(prob!=DistributionDefinitions.UNDEFINED){
			approximate=false;
			return prob;
		}
		
		for(int i=NM-1;i >0 && prob==DistributionDefinitions.UNDEFINED;i--){
			prob=rightTable.get(n, i);			
		}
		
		if(prob==DistributionDefinitions.UNDEFINED){
			approximate=true;
			return DistributionDefinitions.ALL;
		}
		else{
			approximate=true;
			return prob;
		}

	}//end-method
		
	/**
	 * Tests if the last estimation was approximate
	 * 
	 * @return true if the last estimation was approximate. False, otherwise
	 */
	public boolean isApproximate(){
		
		return approximate;
		
	}//end-method
	
}//end-class

