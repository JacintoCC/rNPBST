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

package javanpst.distributions.common;

/**
 * An interface with some requirements common distributions.
 * 
 * @author Joaquín Derrac Rus (jderrac@decsai.ugr.es)
 * @version 1.0
 */
public interface Distribution {
	
	/**
	 * Computes mass probability at a given point of the distribution
	 * @param value Point selected
	 * @return mass probability at 'value'
	 */
	public abstract double computeProbability(double value);
	
	/**
	 * Computes cumulative probability at a given point of the distribution
	 * @param value Point selected
	 * @return cumulative probability at 'value'
	 */
	public abstract double computeCumulativeProbability(double value);
	
}//end-interface