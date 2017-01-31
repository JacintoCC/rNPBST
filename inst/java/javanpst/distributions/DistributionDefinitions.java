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

package javanpst.distributions;

/**
 * Some common definitions for distributions
 * 
 * @author Joaquin
 * @version 1.0
 */
public class DistributionDefinitions{

	//Discrete distributions
	public static final int UNIFORM = 0;
	public static final int BINOMIAL = 1;
	public static final int POISSON = 2;
	public static final int GEOMETRIC = 3;
	
	//Continuous distributions
	public static final int NORMAL = 4;
	public static final int UNIFORMC = 5;
	public static final int CHI_SQUARE = 6;
	public static final int EXPONENTIAL = 7;
	public static final int GAMMA = 8;
	public static final int LAPLACE = 9;
	public static final int LOGISTIC = 10;
	public static final int WEIBULL = 11;
	
	//Data values
	public static final double UNDEFINED = -1.0;
	public static final double ALL = 1.0;

}//end-class

