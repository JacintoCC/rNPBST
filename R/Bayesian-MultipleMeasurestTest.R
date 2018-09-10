#' @title Bayesian Test for Multiple Performance Measures Comparison
#'
#' @export
#' @description Performs the GLRT for the joint analysis of multiple
#'     performance measures.
#' @param x Performance matrix of first algorithm
#' @param y Performance matrix of second algorithm
#' @param n.samples Size of Monte Carlo sampling
#' @param prior Prior distribution parameters
#' @return List with the probabilites of each possible dominance
#'     configuration and posterior sample
bayesianMultipleConditions.test <- function(x, y,
                                        n.samples = 10000,
                                        prior = rep(2^-ncol(x), 2^ncol(x))){
    #checkMultipleMeasuresConditions(x, y)
    
    # Get the number of measures
    n.measures <- ncol(x)

    # Get the number of occurences of each dominance configuration
    count.vector <- rNPBST:::occurencesDominanceConfiguration(x, y)
    count.proportion <-  count.vector / n.measures

    # Posterior distribution
    posterior.distribution <- count.vector + prior

    # Sampling from Dirichlet distribution
    mc.sampling <- MCMCpack::rdirichlet(n.samples,
                                       posterior.distribution)

    # Compute posterior probabilities
    posterior.probabilities <- sapply(1:(2^n.measures),
           function(i){
              mean(mc.sampling[ ,i] > apply(mc.sampling[ ,-i], 1, max))
           })

    return(list(probabilities = posterior.probabilities,
                sample = mc.sampling))
}