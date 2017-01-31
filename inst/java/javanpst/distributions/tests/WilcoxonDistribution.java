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

import javanpst.data.distributions.incompleteTable.Incomplete2KeyTable;
import javanpst.data.readers.distributionReaders.WilcoxonReaderFormat;
import javanpst.distributions.DistributionDefinitions;
import javanpst.distributions.common.continuous.NormalDistribution;
import javanpst.utils.Formats;

/**
 * An implementation of the Wilcoxon Signed-ranks distribution.
 * 
 * Uses a Singleton pattern to ensure only an instance of the class is allowed.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public class WilcoxonDistribution extends TestDistribution{
	
	/**
	 * Table of the distribution
	 */
	private static Incomplete2KeyTable table;

	/**
	 * Normal distribution for adjusting 
	 */
	private static NormalDistribution normal;
	
	/**
	 * Private default builder.
	 * 
	 * Initializes distribution tables
	 */
	private WilcoxonDistribution(){
		
		initialize();
		
	}//end-method
	
	/**
	 * Singleton pattern access to the distribution
	 * 
	 * @return the instance of the distribution
	 */
	public static WilcoxonDistribution getInstance(){
		
		return SingletonHolder.distribution;
		
	}//end-method
	
	/**
	 * Singleton holder for the only instance of the distribution
	 * 
	 * SingletonHolder is loaded on the first execution of getInstance() 
	 * or the first access to SingletonHolder.distribution, not before.
	 */
	private static class SingletonHolder { 
		   
		public static final WilcoxonDistribution distribution = new WilcoxonDistribution();
		
	}//end-method
	
	/**
	 * Generate distribution tables
	 */
	protected void generateTables(){
		
		table=new Incomplete2KeyTable(505,51);
		
		loadFiles();

	}//end-method
	
	/**
	 * Load data files to fill distribution tables
	 */
	protected void loadFiles(){
		
		try{
            SAXParserFactory spf=SAXParserFactory.newInstance(); 
            SAXParser sp = spf.newSAXParser();                   
            sp.parse(this.getClass().getResourceAsStream("/tables/Wilcoxon/WilcoxonTable.xml"), new WilcoxonReaderFormat());	            
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
	 * Computes exact p-value of the Wilcoxon distribution
	 * 
	 * @param N n parameter
	 * @param R rank associated
	 * @return p-value computed
	 */
	public double computeExactProbability(int N,double R){
		
		double pValue,value1,value2;

		if(N>50){
			return DistributionDefinitions.UNDEFINED;
		}
		
		try{
		if(Formats.isInt(R)){		
			pValue=table.get((int)R, N);
			if(pValue==DistributionDefinitions.UNDEFINED){
				pValue=DistributionDefinitions.ALL;
			}
		}else{
			value1=table.get((int)Math.ceil(R), N);
			if(value1==DistributionDefinitions.UNDEFINED){
				value1=DistributionDefinitions.ALL;
			}
			value2=table.get((int)Math.floor(R), N);
			if(value2==DistributionDefinitions.UNDEFINED){
				value2=DistributionDefinitions.ALL;
			}
			
			pValue=(value1+value2)/2.0;
		}
		}
		catch(ArrayIndexOutOfBoundsException e){
			return DistributionDefinitions.ALL;
			
		}
		
		return pValue;
		
	}//end-method
	
	/**
	 * Computes asymptotic double tailed p-value of the Wilcoxon distribution
	 * 
	 * A ties correction of variance is incorporated. The array ties
	 * must be in the form {2,3,2,3} where each integer denotes the occurrence of a tie
	 * and the number of elements involved.
	 * 
	 * if no ties, a void array shoud be used (i.e. ties=new int [0];)
	 * 
	 * @param N n parameter
	 * @param R rank associated
	 * @param ties weight of ties
	 * @return p-value computed
	 */
	public double computeAsymptoticDoubleTailProbability(int N,double R,double ties){
		
		double numerator1,numerator2,denominator;
		double z;
		double leftTail, rightTail, doubleTail;
		
		normal=new NormalDistribution();
		
		numerator1=R-0.5-(N*(N+1.0)/4.0);
		numerator2=R+0.5-(N*(N+1.0)/4.0);
		
		denominator=Math.sqrt(N*(N+1.0)*((2*N)+1.0)/24.0);
		
		denominator-=ties/48.0;
		
		z=numerator1/denominator;
		
		rightTail=1.0-normal.getTipifiedProbability(z, false);

		z=numerator2/denominator;

		leftTail=normal.getTipifiedProbability(z, false);
		
		doubleTail=Math.min(leftTail, rightTail)*2.0;
		doubleTail=Math.min(doubleTail,1.0);
		
		return doubleTail;
		
	}//end-method
	
	/**
	 * Computes asymptotic right tailed p-value of the Wilcoxon distribution
	 * 
	 * A ties correction of variance is incorporated. The array ties
	 * must be in the form {2,3,2,3} where each integer denotes the occurrence of a tie
	 * and the number of elements involved.
	 * 
	 * if no ties, a void array shoud be used (i.e. ties=new int [0];)
	 * 
	 * @param N n parameter
	 * @param R rank associated
	 * @param ties weight of ties
	 * @return p-value computed
	 */
	public double computeAsymptoticRightTailProbability(int N,double R,double ties){
		
		double numerator2,denominator;
		double z;
		double rightTail;
		
		normal=new NormalDistribution();

		numerator2=R-0.5-(N*(N+1.0)/4.0);
		
		denominator=Math.sqrt(N*(N+1.0)*((2*N)+1.0)/24.0);
		
		denominator-=ties/48.0;
		
		z=numerator2/denominator;

		rightTail=1.0-normal.getTipifiedProbability(z, false);
		
		return rightTail;
		
	}//end-method
	
	/**
	 * Computes asymptotic double tailed p-value of the Wilcoxon distribution
	 * 
	 * A ties correction of variance is incorporated. The array ties
	 * must be in the form {2,3,2,3} where each integer denotes the occurrence of a tie
	 * and the number of elements involved.
	 * 
	 * if no ties, a void array shoud be used (i.e. ties=new int [0];)
	 * 
	 * @param N n parameter
	 * @param R rank associated
	 * @param ties weight of ties
	 * @return p-value computed
	 */
	public double computeAsymptoticLeftTailProbability(int N,double R,double ties){
		
		double numerator1,denominator;
		double z;
		double leftTail;
		
		normal=new NormalDistribution();
		
		numerator1=R+0.5-(N*(N+1.0)/4.0);
		
		denominator=Math.sqrt(N*(N+1.0)*((2*N)+1.0)/24.0);
		
		denominator-=ties/48.0;
		
		z=numerator1/denominator;
		
		leftTail=normal.getTipifiedProbability(z, false);
		
		return leftTail;
		
	}//end-method

}//end-class

