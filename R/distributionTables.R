##################################
# READ XML DISTRIBUTION METHODS
##################################

#' @title Parse XML attributes to a data frame
#'
#' @description Parse XML Attributes of a row
#' @param x Row
#' @return Row with values and attribute names
attParse <- function(x){
  xattrs <- XML::xmlAttrs(x)
  c(sapply(XML::xmlChildren(x), XML::xmlValue), xattrs)
}

#' @title Parse XML distribution to a data frame
#'
#' @description Parse XML distribution to a data frame
#' @param xml.file XML file with the distribution
#' @param name.distribution Name of the distribution
#' @return Distribution data frame
xmlDistributionToDataFrame <-function(xml.file, name.distribution){
  parsed.file <- XML::xmlParse(xml.file)
  dist.data.frame <- as.data.frame(t(XML::xpathSApply(parsed.file, "//*/element", attParse)),
                                   stringsAsFactors = FALSE)

  if(name.distribution == "Kendall" | name.distribution == "Spearman" | name.distribution == "Kolmogorov"){
    colnames(dist.data.frame)[which(colnames(dist.data.frame) == "text")] <- "distribution"
    dist.data.frame <- transform(dist.data.frame,
                                 distribution = as.numeric(dist.data.frame$distribution),
                                 n = as.integer(dist.data.frame$n),
                                 T = as.numeric(dist.data.frame$T))
  }
  if(name.distribution == "RunsUpDown" |name.distribution == "NMRanksRight" |name.distribution == "NMRanksLeft"){
    colnames(dist.data.frame)[which(colnames(dist.data.frame) == "text")] <- "distribution"
    dist.data.frame <- transform(dist.data.frame,
                                 distribution = as.numeric(dist.data.frame$distribution),
                                 x = as.integer(dist.data.frame$x),
                                 y = as.numeric(dist.data.frame$y))
  }
  else if(name.distribution == "WilcoxonRanksSum" | name.distribution == "TotalNumberOfRuns-Right" |
          name.distribution == "TotalNumberOfRuns-Left" | name.distribution == "TwoSample"){
    colnames(dist.data.frame)[which(colnames(dist.data.frame) == "text")] <- "distribution"
    dist.data.frame <- transform(dist.data.frame,
                                 distribution = as.numeric(dist.data.frame$distribution),
                                 x = as.integer(dist.data.frame$x),
                                 y = as.integer(dist.data.frame$y),
                                 z = as.integer(dist.data.frame$z))
  }
  else if(name.distribution == "Page"){
    colnames(dist.data.frame)[which(colnames(dist.data.frame) == "text")] <- "L"
    dist.data.frame <- transform(dist.data.frame,
                                 L = as.integer(dist.data.frame$L),
                                 k = as.integer(dist.data.frame$k),
                                 p = as.numeric(dist.data.frame$p),
                                 N = as.integer(dist.data.frame$N))

  }

  return(dist.data.frame)
}

#' @title Parse XML quantile distribution to a data frame
#'
#' @description Parse XML distribution to a data frame
#' @param xml.file XML file with the distribution
#' @param pvalues p-values of the distribution of the statistic
#' @return Distribution data frame
xmlQuantileDistributionToMatrix <-function(xml.file, pvalues){
  parsed.file <- XML::xmlParse(xml.file)
  matrix <- utils::type.convert(t(XML::xpathSApply(parsed.file, "//*/row", attParse)))
  rownames(matrix) <- matrix[ ,"n"]
  matrix <- matrix[ ,-which(colnames(matrix) == "n")]
  colnames(matrix) <- pvalues
  return(matrix)
}

##################################
# GET VALUE OF STATISTIC DISTRIBUTION
##################################

#' @title Get value from distribution table
#'
#' @export
#' @description Function to get the exact probability given a distribution table
#' @param table Distribution table
#' @param n Size of the exact distribution
#' @param T Value of the statistic
#' @param epsilon Threshold for compare the statistic
#' @return p-value computed
getFromDistributionTable <- function(table, n, T, epsilon = 0.002){
  value <- table$distribution[table$n == n & abs(table$T - T) < epsilon]
  return(value)
}

#' @title Compute exact probability of distribution
#'
#' @export
#' @description Function to get the exact probability given a distribution table
#' @param table Distribution table
#' @param n Size of the exact distribution
#' @param T Value of the statistic
#' @return Exact p-value computed
computeExactProbability <- function(table, n, T){
  value <- getFromDistributionTable(table, n, abs(T))
  return(value)
}

#' @title Compute exact probability of distribution
#'
#' @description Function to get the exact probability given a distribution table
#' @param table Distribution table
#' @param n Size of the exact distribution
#' @param T Value of the statistic
#' @return p-value computed
computeAproximatedProbability <- function(table, n, T){

  for(i in 1:ncol(table)){
    if(T >= table[as.character(n),i])
      return(colnames(table)[i])
  }

  return(1.0)
}

#' @title Kolmogorov probability
#'
#' @export
#' @description Computes p-value of the Kolmogorov distribution
#' @param n Size of the population
#' @param Dn Kolmogorov statistic
#' @return p-value computed
pkolmogorov <- function(n, Dn){
   asymptoticValues <- c(1.07,1.22,1.36,1.52,1.63)

   if(n <= 40){
      for(i in ncol(KolmogorovTable):1){
         if(Dn >= KolmogorovTable[as.character(n),i])
            return(colnames(KolmogorovTable)[i])
      }
   }
   else{
      size <- sqrt(n)
      for(i in ncol(KolmogorovTable):1){
         if(Dn >= asymptoticValues[i]/size)
            return(colnames(KolmogorovTable)[i])
      }
   }

   return(1.0)
}

#' @title Get cumulative probability function
#'
#' @description  Get cumulative probability function according to distribution
#' @param distribution Distribution name
#' @param ... Parameters for the distribution
#' @return Cumulative probability function
getCumulativeProbabilityFunction <- function(distribution, ...){
   switch(distribution,
          "NORMAL" = function(x) stats::pnorm(q = x, ...),
          "UNIFORM" = function(x) stats::punif(q = x, ...),
          "CHI_SQUARE" =function(x) stats::pchisq(q = x, ...) ,
          "EXPONENTIAL" = function(x) stats::pexp(q = x, ...),
          "GAMMA" = function(x) stats::pgamma(q = x, ...),
          "LAPLACE" = function(x) rmutil::plaplace(q = x, ...),
          "LOGISTIC" = function(x) stats::plogis(q = x, ...),
          "WEIBULL" = function(x) stats::pweibull(q = x, ...)
   )
}

#' @title Double tail probability
#'
#' @export
#' @description  Get double tail probability
#' @param l Left tail probability
#' @param r Right tail probability
#' @return Double tail probability
doubleTailProbability <- function(l, r){
   return(min(min(l,r)*2, 1))
}
