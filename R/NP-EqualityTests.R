#' @title Chakraborti and Desu Exact Probability
#'
#' @description This function compute the exact probability for Chakraborti and Desu test
#' @param W W statistic
#' @param N Number of elements
#' @param populations Number of populations
#' @param length Number values per sample
#' @return p-value computed
computeCDExactProbability <- function(W, N, populations, length){
  pvalue <- length / N * choose(N - length, W) * choose(length - 1, populations - 1)
  pvalue <- pvalue / choose(N - 1, W + populations - 1)
  return(pvalue)
}

#' @title Chakraborti and Desu Asymtotic Probability
#'
#' @description This function compute the asymtotic probability for Chakraborti and Desu test
#' @param W W statistic
#' @param N Number of elements
#' @param populations Number of populations
#' @param length Number values per sample
#' @return p-value computed
computeCDAsymtoticProbability <- function(W, N, populations, length){
  stMean <- (N - length) * (populations / (length + 1))

  stDev <- populations * (length - populations + 1) * (N + 1) * (N - length)
  stDev <- sqrt(stDev / ((length + 1) * (length + 1) * (length + 2)))
  Z <- (W - stMean + 0.5) / stDev

  return(stats::pnorm(Z))
}


#' @title Chakraborti and Desu test for equality
#'
#' @export
#' @description This function performs the Chakraborti and Desu test
#' @param matrix Matrix of data
#' @examples
#' cd.test(results)
#' @return A htest object with pvalues and statistics
cd.test <- function(matrix){
  if(ncol(matrix) < 3)
    stop("Extended median test only can be employed with more than two samples")

  if(anyNA(matrix))
    stop("No null values allowed in this test.")

  n <- nrow(matrix)
  control <- matrix[ ,1]
  median <- median(control)

  # Compute W statistic
  W <- sum(matrix[ ,-1] < median)

  statistic <- c(median = median, W = W)
  pvalues <- c("Exact p-value" = computeCDExactProbability(W, nrow(matrix)*ncol(matrix), ncol(matrix), nrow(matrix)),
               "Asymtotic p-value" = computeCDAsymtoticProbability(W, nrow(matrix)*ncol(matrix), ncol(matrix), nrow(matrix)))

  htest <- list(data.name = deparse(substitute(matrix)),
                statistic = statistic, p.value = pvalues,
                method = "Chakraborti and Desu")
  return(htest)
}
