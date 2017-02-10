#' @title Checks Conditions
#'
#' @description Checks conditions for bivariate non parametric tests
#' @param matrix Matrix of data
checkBivariateConditions <- function(matrix){
   # Checks
   if(ncol(matrix) != 2)
      stop("This test only can be employed with two samples")

   if(nrow(matrix) < 3)
      stop("This test need samples of size 3 or more")

   if(anyNA(matrix))
      stop("No null values allowed in this test.")
}


#' @title Daniel Trend test for bivariated samples
#'
#' @export
#' @description This function performs the Daniel Trend test
#' @param matrix Matrix of data
#' @return A htest object with pvalues and statistics
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
      SpearmanExactTable <- getData("SpearmanExactTable")
      pvalue <- computeExactProbability(SpearmanExactTable, n, R)
   }
   else if(n <= 30){
      SpearmanQuantileTable <-  getData("SpearmanQuantileTable")
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


#' @title Kendall test for bivariated samples
#'
#' @export
#' @description This function performs the Kendall test
#' @param matrix Matrix of data
#' @return A htest object with pvalues and statistics
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
   T <- 2 * (C - Q) / (n * (n-1))

   # Compute P-Values
   if(n <= 10){
      KendallExactTable <- getData("KendallExactTable")
      pvalue <- computeExactProbability(KendallExactTable, n, T)
      print(pvalue)
   }
   else if(n <= 30){
      KendallQuantileTable <- getData("KendallQuantileTable")
      pvalue <- computeAproximatedProbability(KendallQuantileTable, n, T)
   }

   # Compute asymptotic p-value
   numerator <- 3 * T * sqrt(n * (n-1))
   stdDev <- sqrt(2 * (2*n + 5))
   Z <- numerator / stdDev

   positive.dependence.pvalue <- 1 - stats::pnorm(Z)
   negative.dependence.pvalue <- stats::pnorm(Z)
   no.dependence.pvalue <- 2 * min(positive.dependence.pvalue,
                                   negative.dependence.pvalue)

   statistic <- c(T = T, C = C, Q = Q, Z = Z)
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
