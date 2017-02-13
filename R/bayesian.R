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
#' @return List with probabilities for each region and a sample of posterior distribution.
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
  # Compute probabilities based on data
  probabilities <- c("left" = n.left,"rope" =  n.diff - n.left - n.right, "right" = n.right)/n.diff

  return(list(probabilities = probabilities,
              sample = sample))
}

#' @title Bayesian Signed-Rank test
#'
#' @export
#' @description This function performs the Bayesian Signed-Rank test
#' @param x First vector of observations
#' @param y Second vector of observations
#' @param z_0 Prior pseudo-observation
#' @param s Prior pseudo-observation probabilitie
#' @param rope.min Inferior limit of the rope considered
#' @param rope.max Superior limit of the rope considered
#' @param weights A priori weights
#' @param samples Number of samples of the distribution
#' @examples
#' bsr <- bayesianSignedRank.test(results$random.forest, results$KNN)
#' bsr.stronger.prior <- bayesianSignedRank.test(results$random.forest, results$KNN, s=3, z_0 = 0.5)
#' @return List with probabilities for each region and a sample of posterior distribution.
bayesianSignedRank.test <- function(x, y = NULL, s = 0.5, z_0 = 0,
                                    rope.min = -0.01, rope.max = 0.01,
                                    weights = c(s, rep(1, length(x))),
                                    samples = 30000){
  if(rope.min > rope.max)
    stop("rope.min should be smaller than rope.min")

  diff <- getDiff(x, y)

  # Creation of the vector with the pseudo-observation
  diff <- c(z_0, diff)
  num.elements <- length(diff)

  # Generate the sampled weights
  sampled.weights <- MCMCpack::rdirichlet(samples, weights)

  # Belonging of an interval
  belongs.left <- sapply(diff, FUN = function(x) x+diff < 2 * rope.min)
  belongs.rope <- sapply(diff, FUN = function(x) (x+diff > 2 * rope.min) &
                                                 (x+diff < 2 * rope.max))
  belongs.right <- sapply(diff, FUN = function(x) x+diff > 2 * rope.max)

  sample <- t(apply(sampled.weights, 1,
                   function(x){
                     # Getting the coefficients
                     matrix.prod <- sapply(x, FUN = function(y) x*y)
                     c(sum(matrix.prod * belongs.left),
                       sum(matrix.prod * belongs.rope),
                       sum(matrix.prod * belongs.right))
                    }
                  ))

  # Selects the max according to sampled weights
  #   In case of a tie, the score is divided
  winners <- apply(sample, 1, locate.max)

  # Compute of the probabilities
  probabilities <- apply(winners, 1, mean)
  names(probabilities) <- c("left", "rope", "right")

  return(list(probabilities = probabilities,
              sample = sample))
}

#' @title Bayesian signed rank test
#'
#' @export
#' @description This function performs the bayesian signed rank test
#' @param x First vector of observations
#' @param y Second vector of observations
#' @param rho Expectated correlation.
#' @param rope.min Lower limit of the rope considered
#' @param rope.max Upper limit of the rope considered
#' @examples
#' correlatedBayesianT.test(results.rf[1, ], results.knn[1, ])
#' correlatedBayesianT.test(results.rf[5, ], results.knn[5, ], rope.min=-0.05, rope.max = 0.05)
#' @return List with probabilities for each region and a sample of posterior distribution.
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
