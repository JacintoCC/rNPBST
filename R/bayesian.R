#' @title Bayesian Sign test
#'
#' @export
#' @description This function performs the Bayesian Sign test
#' @param x First vector of observations
#' @param y Second vector of observations
#' @param z_0 Prior pseudo-observation
#' @param s Prior pseudo-observation probabilitie
#' @param rope.min Inferior limit of the rope considered
#' @param rope.max Superior limit of the rope considered
#' @param weights A priori weights
#' @param n.samples Number of samples of the distributio
#' @examples
#' bs <- bayesianSign.test(results$random.forest, results$KNN)
#' bs.stronger.prior <- bayesianSign.test(results$random.forest, results$KNN, s=3, z_0 = 0.5)
#' @return List with probabilities for each region and a sample of
#'     posterior distribution.
bayesianSign.test <- function(x, y = NULL, s = 1, z_0 = 0,
                             rope.min = -0.01, rope.max = 0.01,
                             weights = c(s/2, rep(1, length(x))),
                             n.samples = 100000){

  if(rope.min > rope.max)
    stop("rope.min should be smaller than rope.min")

  diff <- getDiff(x, y)
  n.diff <- length(diff)

  if(rope.min != rope.max){
    # Belonging to an interval
    belongs.left <- diff < rope.min
    belongs.rope <- diff > rope.min & diff < rope.max
    belongs.right <- diff > rope.max

    # Compute counts
    n.left  <- sum(belongs.left)
    n.rope  <- sum(belongs.rope)
    n.right <- sum(belongs.right)

    # Generate a random sample from Dirichlet distribution
    weights <- c(n.left+s/3, n.rope+s/3, n.right+s/3)
  }
  else{
    # Compute number of elements in each region
    n.left  <- sum(diff < rope.min)
    n.right  <- n.diff - n.left

    # Adjust weights
    if(n.left > n.right)
      weights <- c(n.left+s, n.right)
    else if(n.left < n.right)
      weights <- c(n.left, n.right + s)
    else
      weights <- c(n.left + s/2, n.right + s/2)
  }

  # Take a sample from Dirichlet distribution
  sample <- MCMCpack::rdirichlet(n.samples, weights)
  posterior.prob <- colMeans(sample)
 
  return(list(probabilities = c(left = posterior.prob[1],
                                rope = posterior.prob[2],
                                right = posterior.prob[3]),
              sample = sample))
}

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
#' correlatedBayesianT.test(results.rf[1, ], results.knn[1, ])
#' correlatedBayesianT.test(results.rf[5, ], results.knn[5, ], rope.min=-0.05, rope.max = 0.05)
#' @return List with probabilities for each region and a sample of
#'     posterior distribution.
correlatedBayesianT.test <- function(x, y = NULL, rho = 1/length(x),
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

  x <- seq(min(delta-3*stdX, -0.02), max(delta+3*stdX, 0.02), by = 0.001)
  y <- sapply(x, function(t) stats::dt((t - delta)/sp, df))

  results <- list('probabilities' = c('left' = p.left, 'rope' = p.rope, 'right'= 1 - p.left-p.rope),
                  'rope' = c(rope.min, rope.max),
                  'dist' = data.frame(x = x, y = y))
  return (results)
}
