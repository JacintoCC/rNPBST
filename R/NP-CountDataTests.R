#' @title Checks Conditions
#'
#' @description Checks conditions for count data non parametric tests
#' @param matrix Matrix of data
checkCountDataConditions <- function(matrix){
  if(ncol(matrix) != 2 || nrow(matrix) != 2)
    stop("Data must be represented in two rows and two columns")

  if(anyNA(matrix))
    stop("No null values allowed in this test.")
}

#' @title Compute Fisher Left Exact Probability
#'
#' @description Computes exact left tail of Fisher distribution
#' @param N Number of samples
#' @param n1 Sum of the samples of the first row
#' @param n2 sum of the samples of the second row
#' @param Y sum of the samples of the first column
#' @param n00 samples in first column, first row
#' @return p-value computed
computeFisherLeftExactProbability <- function(N, n1, n2, Y, n00){
  numerator <- 0
  for(i in n00:n1)
    numerator <- numerator + choose(n1, i) * choose(n2, Y-i)

  return(numerator / choose(N, Y))
}

#' @title Compute Fisher Right Exact Probability
#'
#' @description Computes exact left tail of Fisher distribution
#' @param N Number of samples
#' @param n1 Sum of the samples of the first row
#' @param n2 sum of the samples of the second row
#' @param Y sum of the samples of the first column
#' @param n00 samples in first column, first row
#' @return p-value computed
computeFisherRightExactProbability <- function(N, n1, n2, Y, n00){
  numerator <- 0
  for(i in n00:0)
    numerator <- numerator + choose(n1, i) * choose(n2, Y-i)

  return(numerator / choose(N, Y))
}

#' @title Fisher test for counts of data
#'
#' @export
#' @description This function performs the Fisher test
#' @param matrix Matrix of data
#' @return A htest object with pvalues and statistics
fisher.test <- function(matrix){

  checkCountDataConditions(matrix)

  N <- sum(matrix)
  n1 <- sum(matrix[1, ])
  n2 <- sum(matrix[2, ])
  Y <- sum(matrix[ ,1])
  n00 <- matrix[1,1]

  # Compute exact P-values
  exact.left.pvalue <- computeFisherLeftExactProbability(N, n1, n2, Y, n00)
  exact.right.pvalue <- computeFisherRightExactProbability(N, n1, n2, Y, n00)

  # Asymtotic P-Values
  Q <- 0

  for(i in 1:2){
    for(j in 1:2){
      term <- N * matrix[i,j] - sum(matrix[i, ])*sum(matrix[ ,j])
      term <- term * term / (N * sum(matrix[i, ])*sum(matrix[ ,j]))
      Q <- Q + term
    }
  }

  asymptotic.pvalue <- stats::pchisq(Q, 1)
  pvalues <- c("Exact Left p-value" = exact.left.pvalue,
               "Exact Right p-value" = exact.right.pvalue,
               "Asymtotic p-value" = asymptotic.pvalue)

  htest <- list(data.name = deparse(substitute(matrix)),
                statistic = c("Q" = Q), p.value = pvalues,
                method = "Fisher")
  return(htest)
}

#' @title McNemar test for counts of data
#'
#' @export
#' @description This function performs the McNemar test
#' @param matrix Matrix of data
#' @return A htest object with pvalues and statistics
mcNemar.test <- function(matrix){
  checkCountDataConditions(matrix)

  # Compute statistics
  S <- matrix[1,2] + matrix[2,1]
  Z <- (matrix[1,2] - matrix[2,1] + 0.5) / sqrt(matrix[1,2] + matrix[2,1])
  T <- (matrix[1,2] - matrix[2,1]) * (matrix[1,2] - matrix[2,1]) / (matrix[1,2] + matrix[2,1])

  exact.pvalue <- stats::pbinom(matrix[1,2], S,  0.5)
  asymptotic.normal.pvalue <- stats::pnorm(Z)
  asymptotic.chi.pvalue <- 1 - stats::pchisq(T, 1)

  pvalues <- c("Exact p-value" = exact.pvalue,
               "Asymtotic Normal p-value" = asymptotic.normal.pvalue,
               "Asymtotic Chi p-value" = asymptotic.chi.pvalue)

  htest <- list(data.name = deparse(substitute(matrix)),
                statistic = c("S" = S, "Z" = Z, "T"=T), p.value = pvalues,
                method = "McNemar")
  return(htest)
}
