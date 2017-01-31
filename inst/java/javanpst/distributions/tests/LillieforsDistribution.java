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
import javanpst.data.readers.distributionReaders.LillieforsNormalReaderFormat;
import javanpst.data.readers.distributionReaders.LillieforsExponentialReaderFormat;

/**
 * An implementation of the Lilliefors distribution.
 * 
 * Uses a Singleton pattern to ensure only an instance of the class is allowed.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class LillieforsDistribution extends TestDistribution{
	
	/**
	 * Table of the distribution for Normal adjustment
	 */
	private static Critical1KeyTable tableNormal;
	
	/**
	 * Table of the distribution for Exponential adjustment
	 */
	private static Critical1KeyTable tableExponential;

	/**
	 * Flag showing if the last estimation was approximate
	 */
	private static boolean aproximated;
	
	/**
	 * Private default builder.
	 * 
	 * Initializes distribution tables
	 */
	private LillieforsDistribution(){
		
		initialize();
		
	}//end-method
	
	/**
	 * Singleton pattern access to the distribution
	 * 
	 * @return the instance of the distribution
	 */
	public static LillieforsDistribution getInstance(){
		
		return SingletonHolder.distribution;
		
	}//end-method
	
	/**
	 * Singleton holder for the only instance of the distribution
	 * 
	 * SingletonHolder is loaded on the first execution of getInstance() 
	 * or the first access to SingletonHolder.distribution, not before.
	 */
	private static class SingletonHolder { 
		   
		public static final LillieforsDistribution distribution = new LillieforsDistribution();
		
	}//end-method
	
	/**
	 * Generate distribution tables
	 */
	protected void generateTables(){
		
		double [] tags={0.100,0.050,0.010,0.001};
		
		tableNormal=new Critical1KeyTable(101,tags);
		tableExponential=new Critical1KeyTable(101,tags);
		
		loadFiles();

	}//end-method
	
	/**
	 * Load data files to fill distribution tables
	 */
	protected void loadFiles(){
		
		try{
            SAXParserFactory spf=SAXParserFactory.newInstance(); 
            SAXParser sp = spf.newSAXParser();                   
            sp.parse(this.getClass().getResourceAsStream("/tables/Lilliefors/LillieforsNormal.xml"), new LillieforsNormalReaderFormat());	            
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
            sp.parse(this.getClass().getResourceAsStream("/tables/Lilliefors/LillieforsExponential.xml"), new LillieforsExponentialReaderFormat());	            
        }catch(ParserConfigurationException e){                  
        	System.err.println("Parser error");             
        }catch(SAXException e2){                                 
        	System.err.println("SAX error: " + e2.getStackTrace());
        } catch (IOException e3) {
        	System.err.println("Input/Output error: " + e3.getMessage() );
        }
        
	}//end-method
	
	/**
	 * Gets the table of the distribution (normal)
	 * 
	 * @return Reference to the table
	 */
	public static Critical1KeyTable getNormalTable(){
		
		return tableNormal;
		
	}//end-method
	
	/**
	 * Gets the table of the distribution (exponential)
	 * 
	 * @return Reference to the table
	 */
	public static Critical1KeyTable getExponentialTable(){
		
		return tableExponential;
		
	}//end-method

	/**
	 * Computes p-value of the Lilliefors distribution (adjust to a normal distribution).
	 * 
	 * @param n size of the population
	 * @param Dn Lilliefors statistic
	 * @return p-value computed
	 */
	public double computeProbabilityNormal(int n,double Dn){
		
		double asymptoticValuesNormal []={0.816,0.888,1.038,1.212};
		
		if(n>3){
			if(tableNormal.get(n, 0)==DistributionDefinitions.UNDEFINED){
				aproximated=false;
				for(int i=3;i>-1;i--){
					if(Dn>=tableNormal.get(n, i)){
						return tableNormal.getHeader(i);
					}
				}	
			}
			else{
				aproximated=true;
				double size=Math.sqrt(n);

				//adjust to a normal distribution
				
				for(int i=3;i>-1;i--){
					if(Dn>=(asymptoticValuesNormal[i]/size)){
						return tableNormal.getHeader(i);
					}
				}
			}
		}
		else{
			aproximated=true;
			return 1.0;
		}

		return DistributionDefinitions.ALL;
		
	}//end-method
	
	/**
	 * Computes p-value of the Lilliefors distribution (adjust to a normal distribution).
	 * 
	 * @param n size of the population
	 * @param Dn Lilliefors statistic
	 * @return p-value computed
	 */
	public double computeProbabilityExponential(int n,double Dn){
		
		double asymptoticValuesExponential []={0.980,1.077,1.274,1.501};
		
		if(n>3){
			if(tableExponential.get(n, 0)==DistributionDefinitions.UNDEFINED){
				aproximated=false;
				for(int i=3;i>-1;i--){
					if(Dn>=tableExponential.get(n, i)){
						return tableExponential.getHeader(i);
					}
				}	
			}
			else{
				aproximated=true;
				double size=Math.sqrt(n);

				for(int i=3;i>-1;i--){
					if(Dn>=(asymptoticValuesExponential[i]/size)){
						return tableExponential.getHeader(i);
					}
				}
			}
		}
		else{
			aproximated=true;
			return 1.0;
		}

		return DistributionDefinitions.ALL;
		
	}//end-method
	
	/**
	 * Tests if the last estimation was approximate
	 * 
	 * @return true if the last estimation was approximate. False, otherwise
	 */
	public boolean isApproximate(){
		
		return aproximated;
		
	}//end-method
	
}//end-class

