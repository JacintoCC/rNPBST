#' @title Kendall test for bivariated samples
#'
#' @export
#' @description This function performs the Kendall test
#' @param matrix Matrix of data
#' @examples
#' x <- 1:10
#' m <- matrix(c(x, 2*x+rnorm(length(x))), ncol = 2)
#' kendall.test(m)
#' @return A list with pvalues for alternative hypothesis, statistics,
#'     method and data name
kendall.test <- function(matrix){
   checkBivariateConditions(matrix)
   
   n <- nrow(matrix)
   
   # Ascending rank for both columns
   rank.first <- rank(matrix[ ,1])
   rank.second <- rank(matrix[ ,2])
   
   # Sort pairs of ranks according to first sample
   rank.matrix <- matrix(c(rank.first, rank.second), ncol = 2)
   rank.matrix <- rank.matrix[order(rank.first), ]
   
   # Print ranks
   print("Ranks computed")
   print(rank.matrix)
   
   # Compute C and Q statistic
   C <- vector("numeric", length = n)
   Q <- vector("numeric", length = n)
   
   for(i in n:2){
      value <- rank.matrix[i, 2]
      C[(i-1):1] <- C[(i-1):1] + (rank.matrix[(i-1):1,2] < value)
      Q[(i-1):1] <- Q[(i-1):1] + (rank.matrix[(i-1):1,2] > value)
   }
   
   C <- sum(C)
   Q <- sum(Q)
   
   # Compute T statistic
   t <- 2 * (C - Q) / (n * (n-1))
   
   # Compute P-Values
   if(n <= 10){
      pvalue <- computeExactProbability(KendallExactTable, n, t)
   }
   else if(n <= 30){
      pvalue <- computeAproximatedProbability(KendallQuantileTable, n, t)
   }
   
   # Compute asymptotic p-value
   numerator <- 3 * t * sqrt(n * (n-1))
   stdDev <- sqrt(2 * (2*n + 5))
   Z <- numerator / stdDev
   
   positive.dependence.pvalue <- 1 - stats::pnorm(Z)
   negative.dependence.pvalue <- stats::pnorm(Z)
   no.dependence.pvalue <- 2 * min(positive.dependence.pvalue,
                                   negative.dependence.pvalue)
   
   statistic <- c(t = t, C = C, Q = Q, Z = Z)
   if(n <= 30){
      pvalues <- c("Exact pvalue" = pvalue,
                   "Positive Dependence pvalue" = positive.dependence.pvalue,
                   "Negative Dependence pvalue" = negative.dependence.pvalue,
                   "No Dependence pvalue" = no.dependence.pvalue)
   }
   else{
      pvalues <- c("Positive Dependence pvalue" = positive.dependence.pvalue,
                   "Negative Dependence pvalue" = negative.dependence.pvalue,
                   "No Dependence pvalue" = no.dependence.pvalue)
   }
   
   htest <- list(data.name = deparse(substitute(matrix)),
                 statistic = statistic, p.value = pvalues,
                 method = "Kendall")
   return(htest)
}
