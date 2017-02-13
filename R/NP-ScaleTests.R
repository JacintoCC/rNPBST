#' @title Siegel-Tukey test for scale
#'
#' @export
#' @description This function performs the Siegel Tukey test
#' @param matrix Matrix of data
#' @examples
#' x <- cbind(rnorm(10, sd=1), rnorm(10, sd=3))
#' siegelTukey.test(x)
#' @return A htest object with pvalues and statistics
siegelTukey.test <- function(matrix){
  if(ncol(matrix) != 2)
    stop("Siegel-Tukey test only can be employed with two samples")

  sample1 <- matrix[!is.na(matrix[ ,1]),1]
  sample2 <- matrix[!is.na(matrix[ ,2]),2]
  n1 <- length(sample1)
  n2 <- length(sample2)
  n <- n1 + n2


  combined <- c(sample1, sample2)
  combined <- combined[order(combined)]

  even <- n%%2 == 0

  if(even){
    weights <- sapply(1:n, function(i) ifelse(i <= n/2,
                                              ifelse(i %% 2 == 0, 2 * i, 2 * i -1),
                                              ifelse(i %% 2 == 0, 2 * (n-i) + 2, 2 * (n-i) + 1)))

  }
  else{
    weights <- sapply(1:n, function(i)
      ifelse(i <= n/2,
             ifelse(i %% 2 == 0, 2 * i, 2 * i -1),
             ifelse(i == n/2 + 1, 0,
                    ifelse(i %% 2 == 0, 2 * (n-i) + 2, 2 * (n-i) + 1))))
  }

  sumWeights <- sum(weights)

  ST1 <- ifelse(n1 <= n2,
                sum(weights[combined %in% sample1]),
                sum(weights[combined %in% sample2]))
  ST2 <- sumWeights - ST1

  if(n1 <= 10 & n2 <= 10){
    exact.left.tail <- computeWilcoxonRankLeftProbability(n2, n1, ST1)
    exact.right.tail <- computeWilcoxonRankRightProbability(n2, n1, ST1)
    exact.double.tail <- doubleTailProbability(exact.left.tail, exact.right.tail)
    pvalues <- c("Exact Left Tail" = exact.left.tail,
                 "Exact Right Tail" = exact.right.tail,
                 "Exact Double Tail" = exact.double.tail)
  }

  denominator <- sqrt(n1 * n2 * (n1 + n2 + 1) / 12)

  numerator <- ST1 - 0.5 - n1 * (n2 + n1 + 1) / 2
  z <- numerator / denominator
  asymptotic.right <- 1 - stats::pnorm(z)

  numerator <- ST1 + 0.5 - n1 * (n2 + n1 + 1) / 2
  z <- numerator / denominator
  asymptotic.left <- stats::pnorm(z)

  asymptotic.values <- c("Asymptotic Left Tail" = asymptotic.left,
                         "Asymptotic Right Tail" = asymptotic.right,
                         "Asymptotic Double Tail" = doubleTailProbability(asymptotic.left, asymptotic.right))

  pvalues <- c(pvalues, asymptotic.values)

  htest <- list(data.name = deparse(substitute(matrix)),
                statistic = c("ST1" = ST1, "ST2" = ST2),
                p.value = pvalues,
                method = "Siegel Tukey")
  return(htest)
}
