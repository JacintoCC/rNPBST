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
import javanpst.data.distributions.aproximateTable.Aproximate1KeyTable;
import javanpst.data.distributions.criticalTable.Critical1KeyTable;
import javanpst.data.readers.distributionReaders.SpearmanExactReaderFormat;
import javanpst.data.readers.distributionReaders.SpearmanQuantileReaderFormat;

/**
 * An implementation of the Spearman distribution.
 * 
 * Uses a Singleton pattern to ensure only an instance of the class is allowed.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class SpearmanDistribution extends TestDistribution{
	
	/**
	 * Table of the exact distribution
	 */
	private static Aproximate1KeyTable exactTable;
	
	/**
	 * Table of critical values
	 */
	private static Critical1KeyTable table;
	
	/**
	 * Normal distribution for asymptotic approximation
	 */
	private static NormalDistribution normal;
	
	/**
	 * Private default builder.
	 * 
	 * Initializes distribution tables
	 */
	private SpearmanDistribution(){
		
		initialize();
		
	}//end-method
	
	/**
	 * Singleton pattern access to the distribution
	 * 
	 * @return the instance of the distribution
	 */
	public static SpearmanDistribution getInstance(){
		
		return SingletonHolder.distribution;
		
	}//end-method
	
	/**
	 * Singleton holder for the only instance of the distribution
	 * 
	 * SingletonHolder is loaded on the first execution of getInstance() 
	 * or the first access to SingletonHolder.distribution, not before.
	 */
	private static class SingletonHolder { 
		   
		public static final SpearmanDistribution distribution = new SpearmanDistribution();
		
	}//end-method
	
	/**
	 * Generate distribution tables
	 */
	protected void generateTables(){
		
		double [] tags={0.100,0.050,0.025,0.010,0.005,0.001};
		
		table=new Critical1KeyTable(31,tags);
		exactTable= new Aproximate1KeyTable(11,85);
		
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
            sp.parse(this.getClass().getResourceAsStream("/tables/Spearman/QuantileTable.xml"), new SpearmanQuantileReaderFormat());	            
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
            sp.parse(this.getClass().getResourceAsStream("/tables/Spearman/ExactTable.xml"), new SpearmanExactReaderFormat());	            
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
	public static Critical1KeyTable getCriticalTable(){
		
		return table;
		
	}//end-method
	
	/**
	 * Gets the table of the distribution
	 * 
	 * @return Reference to the table
	 */
	public static Aproximate1KeyTable getExactTable(){
		
		return exactTable;
		
	}//end-method
	
	/**
	 * Computes exact probability of the Spearman distribution
	 * 
	 * @param n number of pairs
	 * @param T Spearman statistic
	 * @return p-value computed
	 */
	public double computeExactProbability(int n,double T){
		
		T=Math.abs(T);
		
		return exactTable.get(n, T);

	}//end-method
	
	/**
	 * Computes approximated probability of the exact Spearman distribution
	 * 
	 * @param n number of pairs
	 * @param T Spearman statistic
	 * @return p-value computed
	 */
	public double computeAproximatedProbability(int n,double T){

		T=Math.abs(T);
		
		if((n>10)&&(n<31)){
			for(int i=5;i>-1;i--){
				if(T>=table.get(n, i)){
					return table.getHeader(i);
				}
			}	
		}

		return DistributionDefinitions.ALL;
		
	}//end-method
	
	/**
	 * Computes asymptotic probability of the Spearman distribution
	 * 
	 * @param Z normal approximation
	 * @param dependence true if testing positive dependence. False, if negative
	 * @return p-value computed
	 */
	public double computeAsymptoticProbability(double Z, boolean dependence){
		
		return normal.getTipifiedProbability(Z, dependence);
		
	}//end-method
	
}//end-class

