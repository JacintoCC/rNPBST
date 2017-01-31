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
import javanpst.data.distributions.incompleteTable.Incomplete3KeyTable;
import javanpst.data.readers.distributionReaders.KolmogorovTwoSampleAsymptoticalReaderFormat;
import javanpst.data.readers.distributionReaders.KolmogorovTwoSampleReaderFormat;

/**
 * An implementation of the Kolmogorov distribution for two samples.
 * 
 * Uses a Singleton pattern to ensure only an instance of the class is allowed.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class KolmogorovTwoSampleDistribution extends TestDistribution{
	
	/**
	 * Table of the Kolmogorov distribution
	 */
	private static Incomplete3KeyTable exactTable;
	
	/**
	 * Table of critical values the Kolmogorov distribution
	 */
	private static Critical1KeyTable table;
	
	/**
	 * Private default builder.
	 * 
	 * Initializes distribution tables
	 */
	private KolmogorovTwoSampleDistribution(){
		
		initialize();
		
	}//end-method
	
	/**
	 * Singleton pattern access to the distribution
	 * 
	 * @return the instance of the distribution
	 */
	public static KolmogorovTwoSampleDistribution getInstance(){
		
		return SingletonHolder.distribution;
		
	}//end-method
	
	/**
	 * Singleton holder for the only instance of the distribution
	 * 
	 * SingletonHolder is loaded on the first execution of getInstance() 
	 * or the first access to SingletonHolder.distribution, not before.
	 */
	private static class SingletonHolder { 
		   
		public static final KolmogorovTwoSampleDistribution distribution = new KolmogorovTwoSampleDistribution();
		
	}//end-method
	
	/**
	 * Generate distribution tables
	 */
	protected void generateTables(){
		
		double [] tags={0.200,0.100,0.050,0.020,0.010};
		
		table=new Critical1KeyTable(21,tags);
		
		exactTable = new Incomplete3KeyTable(9,9,65);
		
		loadFiles();

	}//end-method
	
	/**
	 * Load data files to fill distribution tables
	 */
	protected void loadFiles(){
		
		try{
            SAXParserFactory spf=SAXParserFactory.newInstance(); 
            SAXParser sp = spf.newSAXParser();                   
            sp.parse(this.getClass().getResourceAsStream("/tables/Kolmogorov/TwoSampleAsymptotical.xml"), new KolmogorovTwoSampleAsymptoticalReaderFormat());	            
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
            sp.parse(this.getClass().getResourceAsStream("/tables/Kolmogorov/TwoSample.xml"), new KolmogorovTwoSampleReaderFormat());
        }catch(ParserConfigurationException e){                  
        	System.err.println("Parser error");             
        }catch(SAXException e2){                                 
        	System.err.println("SAX error: " + e2.getStackTrace());
        } catch (IOException e3) {
        	System.err.println("Input/Output error: " + e3.getMessage() );
        }
        
	}//end-method
	
	/**
	 * Gets the table of the critical values distribution
	 * 
	 * @return Reference to the table
	 */
	public static Critical1KeyTable getTable(){
		
		return table;
		
	}//end-method

	/**
	 * Gets the table of the distribution
	 * 
	 * @return Reference to the table
	 */
	public static Incomplete3KeyTable getExactTable(){
		
		return exactTable;
		
	}//end-method

	/**
	 * Computes p-value of the Kolmogorov distribution.
	 * 
	 * @param n size of the population
	 * @param Dn Kolmogorov statistic
	 * @return p-value computed
	 */
	public double computeExactProbability(int n,double Dn){
		
		double nmDn;
		int nmDnInteger;
		double pValue;
	
		if(n<9){
			//exact table
			nmDn=n*n*Dn;
			nmDnInteger=(int)nmDn;
			
			pValue=exactTable.get(n, n, nmDnInteger);
			
			if(pValue==-1){
				return DistributionDefinitions.ALL;
			}
			
			return pValue;
			
		}
		
		return DistributionDefinitions.ALL;
		
	}//end-method
	
	/**
	 * Computes p-value of the Kolmogorov distribution.
	 * 
	 * @param n size of the population
	 * @param Dn Kolmogorov statistic
	 * @return p-value computed
	 */
	public double computeAsymptoticProbability(int n,double Dn){
		
		double asymptoticValues []={1.07,1.22,1.36,1.52,1.63};
		double nmDn;

		if(n<21){
			nmDn=n*n*Dn;
			//asymptotic table
			if(table.get(n, 0)==DistributionDefinitions.UNDEFINED){
				return DistributionDefinitions.UNDEFINED;
			}
				
			for(int i=4;i>-1;i--){
				if(nmDn>=table.get(n, i)){
					return table.getHeader(i);
				}
			}
		}
		else{
			//asymptotic distribution
			double size=Math.sqrt((n+n)/(n*n));
			for(int i=4;i>-1;i--){
				if(Dn>=(asymptoticValues[i]/size)){
					return table.getHeader(i);
				}
			}

		}
		
		return DistributionDefinitions.ALL;
		
	}//end-method
	
}//end-class

