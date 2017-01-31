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

package javanpst.tests;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * An abstract class representing basic functionality
 * for statistical tests.
 * 
 * @author Joaquin
 * @version 1.0
 */
public abstract class StatisticalTest{
	
	/**
	 * Flag for data ready to apply the test
	 */
	protected boolean dataReady;
	
	/**
	 * Flag to set when the set have been performed
	 */
	protected boolean performed;
	
	/**
	 * Formatter for real values
	 */
	protected DecimalFormat nf6;
	
	/**
	 * Tests is the data is ready to carry out the test
	 * 
	 * @return true if the data is ready. False, otherwise
	 */
	public boolean isDataReady(){
		
		return dataReady;
		
	}//end-method
	
	/**
	 * Tests is the test have been already carried out
	 * 
	 * @return true if the test have been already performed. False, otherwise
	 */
	public boolean isPerformed(){
		
		return performed;
		
	}//end-method
	
	/**
	 */
	protected void setReportFormat(){
		
		nf6 = (DecimalFormat) DecimalFormat.getInstance();
		nf6.setMaximumFractionDigits(6);
		nf6.setMinimumFractionDigits(0);

		DecimalFormatSymbols dfs = nf6.getDecimalFormatSymbols();
		
		dfs.setDecimalSeparator('.');
		nf6.setDecimalFormatSymbols(dfs); 
		
	}//end-method
	
	/**
	 * Prints a report with the results of the test
	 * 
	 * @return Output report
	 */
	public abstract String printReport();
	
	/**
	 * Prints the data stored in the test
	 * 
	 * @return Data stored
	 */
	public abstract String printData();
	
	/**
	 * Perform the test
	 */
	public abstract void doTest(); 
	
	/**
	 * Clears the data stored in the test
	 */
	public abstract void clearData(); 
	
}//end-class
