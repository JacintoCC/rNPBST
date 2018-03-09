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
#' @param mc.samples Number of samples of the distribution
#' @examples
#' bsr <- bayesianSignedRank.test(results$random.forest, results$KNN)
#' bsr.stronger.prior <- bayesianSignedRank.test(results$random.forest, results$KNN, s=3, z_0 = 0.5)
#' @return List with probabilities for each region and a sample of
#'     posterior distribution.
bayesianSignedRank.test <- function(x, y = NULL, s = 0.5, z_0 = 0,
                                    rope.min = -0.01, rope.max = 0.01,
                                    weights = NULL,
                                    mc.samples = 10000){
   if(rope.min > rope.max)
      stop("rope.min should be smaller than rope.min")
   
   diff <- getDiff(x, y)
   
   # Creation of the vector with the pseudo-observation
   diff <- c(z_0, diff)
   num.elements <- length(diff)
   if(is.null(weights))
      weights <- c(s, rep(1, num.elements))
   w <- MCMCpack::rdirichlet(mc.samples, weights)
   
   # Belonging of an interval
   belongs.left <- sapply(diff, FUN = function(x) x+diff < 2 * rope.min)
   belongs.rope <- sapply(diff, FUN = function(x) (x+diff > 2 * rope.min) &
                             (x+diff < 2 * rope.max))
   belongs.right <- sapply(diff, FUN = function(x) x+diff > 2 * rope.max)
   
   get.posterior <- function(w.x.s, w.y.s, left, rope, right){
      product <- matrix(w.x.s[-1], ncol = 1) %*% matrix(w.y.s[-1], nrow = 1)
      posterior.left <- sum(left * product)
      posterior.rope <- sum(rope * product)
      posterior.right <- sum(right * product)
      return(cbind(left = posterior.left,
                   rope = posterior.rope,
                   right = posterior.right))
   }
   
   posterior.distribution <- mapply(
      get.posterior,
      w.x = as.data.frame(t(w)),
      w.y = as.data.frame(t(w)),
      MoreArgs = list(left = belongs.left, rope = belongs.rope, right = belongs.right)
   ) %>% unname() %>% as.matrix() %>% t()
   
   return(list("sample" = posterior.distribution,
               "probabilities" = colMeans(posterior.distribution)))
   
}