#' @title Daniel Trend test for bivariated samples
#'
#' @export
#' @description This function performs the Daniel Trend test
#' @param matrix Matrix of data
#' @examples
#' x <- 1:10
#' m <- matrix(c(x, 2*x+rnorm(length(x))), ncol = 2)
#' danielTrend.test(m)
#' @return A list with pvalues for alternative hypothesis, statistics,
#'     method and data name
danielTrend.test <- function(matrix){
   # Check Conditions
   checkBivariateConditions(matrix)
   
   n <- nrow(matrix)
   
   # Ascending rank for both columns
   rank.first <- rank(matrix[ ,1])
   rank.second <- rank(matrix[ ,2])
   
   # Compute sum D statistic
   sumD <- sum( (rank.first - rank.second)^2 )
   
   # Compute R statistic
   R <- 1 - (6 * sumD) / (n * (n*n - 1))
   
   # Compute P-Values
   if(n <= 10){
      pvalue <- computeExactProbability(SpearmanExactTable, n, R)
   }
   else if(n <= 30){
      pvalue <- computeAproximatedProbability(SpearmanQuantileTable, n, R)
   }
   
   # Compute asymptotic p-value
   Z <- R * sqrt(n-1)
   
   positive.dependence.pvalue <- 1 - stats::pnorm(Z)
   negative.dependence.pvalue <- stats::pnorm(Z)
   no.dependence.pvalue <- 2 * min(positive.dependence.pvalue,
                                   negative.dependence.pvalue)
   
   statistic <- c(D = sumD, R = R, Z = Z)
   if(n <= 30){
      pvalues <- c("pvalue" = pvalue,
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
                 method = "Daniel Trend")
   return(htest)
}
