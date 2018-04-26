#' @title Compute exact probability of Page distribution
#'
#' @description Function to get the exact probability of Page distribution
#' @param N number of columns
#' @param k number of rows
#' @param L Page statistic
#' @return Exact p-value computed
computePageExactProbability <- function(N, k, L){
  if(N <= 8 & N >= 3 & k <= 12 & k >= 2){
    pvalues <- PageTable$p[PageTable$N == N &
                           PageTable$k == k &
                           PageTable$L < L]

    pvalue <- ifelse(length(pvalues) > 0, pvalues[1], 1)
    return(pvalue)
  }

  return(-1)
}

#' @title Compute asymptotic probability of distribution
#'
#' @description Function to get the asymptotic probability given a distribution table
#' @param N number of columns
#' @param k number of rows
#' @param L Page statistic
#' @return Exact p-value computed
computePageAsymptoticProbability <- function(N, k, L){

  numerator <- 12 * (L - 0.5) - 3 * k * N * (N + 1) * (N + 1)
  denominator <- N * (N + 1) * sqrt(k * (N - 1))
  Z <- numerator / denominator

  return(1 - stats::pnorm(Z))
}

#' @title Page test for multiple comparisons
#'
#' @export
#' @description This function performs the Page test
#' @param matrix Matrix of data
#' @return A list with pvalues for alternative hypothesis, statistics, method and data name
page.test <- function(matrix){
  if(ncol(matrix) < 3)
    stop("Extended median test only can be employed with more than two samples")

  if(anyNA(matrix))
    stop("No null values allowed in this test.")

  ranks <- t(apply(matrix, 1, rank))
  sumRanks <- apply(ranks, 2, sum)
  L <- sum(sumRanks * 1:ncol(matrix))

  exact.pvalue <- computePageExactProbability(ncol(matrix), nrow(matrix), L)
  asymptotic.pvalue <- computePageAsymptoticProbability(ncol(matrix), nrow(matrix), L)
  pvalues <- c("Exact pvalue <=" = exact.pvalue,
               "Asymtotic pvalue" = asymptotic.pvalue)

  htest <- list(data.name = deparse(substitute(matrix)),
                statistic = c("L" = L), p.value = pvalues,
                method = "Page")
  return(htest)
}

#' @title Partial Correlation test for multiple comparisons
#'
#' @export
#' @description This function performs the Partial correlation test
#' @param matrix Matrix of data
#' @return A list with pvalues for alternative hypothesis, statistics, method and data name
partialcorrelation.test <- function(matrix){
  if(nrow(matrix) != 3)
    stop("Partial correlation test only can be employed with three variables: X, Y and Z")

  if(anyNA(matrix))
    stop("No null values allowed in this test.")

  ranks <- t(apply(matrix, 1, rank))
  n <- ncol(matrix)

  # T partial statistics

  # Sorted by Z
  ranks <- ranks[ , order(ranks[3, ])]

  TxzQ <- sum(sapply(1:n, function(i) sum(i < 1:n & ranks[1,i] > ranks[1, ])))
  TxzC <- sum(sapply(1:n, function(i) sum(i < 1:n & ranks[1,i] <= ranks[1, ])))

  TyzQ <- sum(sapply(1:n, function(i) sum(i < 1:n & ranks[2,i] > ranks[2, ])))
  TyzC <- sum(sapply(1:n, function(i) sum(i < 1:n & ranks[2,i] <= ranks[2, ])))

  # Sorted by Y
  ranks <- ranks[ , order(ranks[2, ])]

  TxyQ <- sum(sapply(1:n, function(i) sum(i < 1:n & ranks[1,i] > ranks[1, ])))
  TxyC <- sum(sapply(1:n, function(i) sum(i < 1:n & ranks[1,i] <= ranks[1, ])))

  # T statistics
  Txy <- 2 * (TxyC - TxyQ) / (n * (n-1))
  Txz <- 2 * (TxzC - TxzQ) / (n * (n-1))
  Tyz <- 2 * (TyzC - TyzQ) / (n * (n-1))

  tau <- (Txy - Txz * Tyz) / sqrt((1 - Txz * Txz) * (1 - Tyz * Tyz))

  # Pvalue
  pvalue <- as.numeric(computeAproximatedProbability(PartialCorrelationTable, n, tau))

  htest <- list(data.name = deparse(substitute(matrix)),
                statistic = c("tau" = tau), p.value = ("pvalue <=" = pvalue),
                method = "Partial correlation")
  return(htest)
}
