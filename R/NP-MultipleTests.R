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
