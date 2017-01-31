#' @title Compute exact probability of Wilcoxon distribution
#'
#' @description Function to get the exact probability of Wilcoxon distribution
#' @param n Size of distribution
#' @param R statistic
#' @return Exact p-value computed
computeWilcoxonExactProbability <- function(n, R){
  if(n > 50)
    return(-1)
  else{
    WilcoxonTable <- getdata("WilcoxonTable.rda")
    if(R - floor(R) == 0){
      pvalue <- unname(WilcoxonTable[R,n])
      pvalue <- ifelse(pvalue == -1, 1, pvalue)
    }
    else{
      v1 <- unname(WilcoxonTable[floor(R),n])
      v1 <- ifelse(v1 == -1, 1, v1)

      v2 <- unname(WilcoxonTable[ceiling(R),n])
      v2 <- ifelse(v2 == -1, 1, v2)

      pvalue <- mean(v1, v2)
    }

    return(pvalue)
  }
}

#' @title Compute asymptotic probability of Wilcoxon distribution
#'
#' @description Function to get asymptotic probability of Wilcoxon distribution
#' @param n Size of distribution
#' @param R statistic
#' @param ties Ties
#' @return Exact p-value computed
computeWilcoxonAsymptoticProbability <- function(n, R, ties){
  numerator1 <- R - 0.5 - n * (n + 1) / 4
  numerator2 <- R + 0.5 - n * (n + 1) / 4

  denominator <- sqrt(n * (n+1) * (2*n + 1) / 24) - ties / 48

  right.tail <- 1 - stats::pnorm(numerator1 / denominator)
  left.tail <- stats::pnorm(numerator2 / denominator)
  double.tail <- doubleTailProbability(left.tail, right.tail)

  return(c("Asymptotic Left Tail" = left.tail,
           "Asymptotic Right Tail" = right.tail,
           "Asymptotic Double Tail" = double.tail))
}


#' @title Wilcoxon Test for one sample
#'
#' @export
#' @description This function performs the Wilcoxon test
#' @param matrix matrix of data
#' @return A htest object with pvalues and statistics
wilcoxon.test <- function(matrix){
  if(ncol(matrix) != 2)
    stop("Wilcoxon test only can be employed with two samples")

  if(anyNA(matrix))
    stop("No null values allowed in this test.")

  n <- nrow(matrix)
  differences <- abs(matrix[ ,1] - matrix[ ,2])
  zero.diff <- sum(differences == 0)

  # Ignoring ties

  sample.wo.ties.1 <- matrix[differences != 0, 1]
  sample.wo.ties.2 <- matrix[differences != 0, 2]
  differences.wo.ties <- sample.wo.ties.1 - sample.wo.ties.2
  sign <- differences.wo.ties > 0
  differences.wo.ties <- abs(differences.wo.ties)
  n.wo.ties <- length(differences.wo.ties)

  # Compute ranks and sum of ranks
  ranks <- rank(differences.wo.ties)

  rPlus <- ranks %*% sign
  rMinus <- ranks %*% !sign

  if(zero.diff > 1){
    increment <- zero.diff - zero.diff %% 2

    sum0 <- zero.diff * (zero.diff + 1) / 4
    rPlus <- rPlus + sum0 + sum(increment * sign)
    rMinus <- rMinus + sum0 + sum(increment * !sign)
  }

  aux.pvalue1 <-computeWilcoxonExactProbability(n.wo.ties, rPlus)
  aux.pvalue2 <-computeWilcoxonExactProbability(n.wo.ties, rMinus)

  if(aux.pvalue1 == 1 & aux.pvalue2 == 1){
    exact.left.tail <- 1
    exact.right.tail <- 1
    exact.double.tail <- 1
  }
  else if(aux.pvalue1 != 1){
    exact.double.tail <- aux.pvalue1
    exact.left.tail <- exact.double.tail / 2
    exact.right.tail <- 1 - exact.left.tail
  }
  else{
    exact.double.tail <- aux.pvalue2
    exact.right.tail <- exact.double.tail / 2
    exact.left.tail <- 1 - exact.right.tail
  }

  uniq <- unique(ranks)
  tied <- sapply(uniq, function(x) sum(x == ranks))
  tied <- tied[ tied > 1]
  tiesWeight <- sum(tied * (tied * tied - 1))

  pvalues <- c("Exact Left pvalue" = exact.left.tail,
               "Exact Right pvalue" = exact.right.tail,
               "Exact Double pvalue" = exact.double.tail,
               computeWilcoxonAsymptoticProbability(n.wo.ties, rPlus, tiesWeight))

  htest <- list(data.name = deparse(substitute(matrix)),
                statistic = c("R+" = rPlus, "R-" = rMinus),
                p.value = pvalues,
                method = "Wilcoxon")
  return(htest)
}
