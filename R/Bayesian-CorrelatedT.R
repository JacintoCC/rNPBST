#' @title Bayesian Correlayed t-test
#'
#' @export
#' @description This function performs the Bayesian correlated t-test
#' @param x First vector of observations
#' @param y Second vector of observations
#' @param rho Expectated correlation.
#' @param rope.min Lower limit of the rope considered
#' @param rope.max Upper limit of the rope considered
#' @examples
#' bayesianCorrelatedT.test(results.rf[1, ], results.knn[1, ])
#' bayesianCorrelatedT.test(results.rf[5, ], results.knn[5, ], rope.min=-0.05, rope.max = 0.05)
#' @return List with probabilities for each region and a sample of
#'     posterior distribution.
bayesianCorrelatedT.test <- function(x, y = NULL, rho = 1/length(x),
                                    rope.min = -0.01, rope.max = 0.01){
  if(rope.min > rope.max)
    stop("rope.min should be smaller than rope.min")

  diff <- getDiff(x, y)

  delta <- mean(diff)
  n <- length(diff)
  df <- n-1
  stdX <- stats::sd(diff)
  sp <- stdX * sqrt(1/n + rho/(1-rho))
  p.left <- stats::pt((rope.min - delta)/sp, df)
  p.rope <- stats::pt((rope.max - delta)/sp, df) - p.left

  x <- seq(min(delta-3*stdX, -0.02), max(delta+3*stdX, 0.02), length.out = 100)
  y <- sapply(x, function(t) stats::dt((t - delta)/sp, df))

  results <- list('probabilities' = c('left' = p.left, 'rope' = p.rope, 'right'= 1 - p.left-p.rope),
                  'rope' = c(rope.min, rope.max),
                  'dist' = data.frame(x = x, y = y), 
                  'method' = "Bayesian Correlated t-test")
  class(results) <- c("PosteriorT")
  return (results)
}

#' @title Test object to table in LaTeX format
#'
#' @export
#' @description Transform a test object to table in LaTeX format
#' @param test Test object with pvalue(s), test name and statistic(s)
#' @examples
#' htest2Tex(cd.test(results))
#' @return This method prints the necessary code for include a table
#'     with the information provided by the test.
htest2Tex.PosteriorT <- function(test){
  tex.string <- paste("\\begin{table}[] \n",
                      "\\centering\n",
                      "\\caption{Bayesian Correlated t-test}\n",
                      "\\begin{tabular}{lll} \n",
                      "\\hline\n",
                      "\\multirow{3}{*}{Probabilities}\n",
                      "& 	left	 &", test$probabilities[1],"\\\n",
                      "& 	rope	 &", test$probabilities[2],"\\\n",
                      "& 	right	 &", test$probabilities[3],"\\ \\hline\n",
                      "\\multirow{2}{*}{rope}\n",
                      "& 	Lower Bound	 &", test$rope[1],"\\\n",
                      "& 	Upper Bound	 &", test$rope[2],"\\ \\hline\n",
                      "\\end{tabular}",
                      "\\end{table}",
                      sep = "")
  
  return(tex.string)
}
