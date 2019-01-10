#' Approximate Non-Parametric Confidence Interval
#'
#' Approximate and Non-Parametric approach to the computation of the Confidence Interval for the comparison of two algorihms
#' @param x First algorithm's results
#' @param y Second algorithm's results
#' @param alpha 100(1-alpha)\% level of significance
#' @param paired Boolean that indicates if the samples are paired
#' @return NP confidence interval
#' @export
NP.ApproximateConfidenceInterval <- function(x,y,alpha = 0.05, paired = FALSE){
  
  if(paired){
    n <- length(x)
    differences <- x - y
    mean.differences <- sapply(differences, 
                          function(i) sapply(differences, 
                                             function(j) mean(c(i,j))))
    
    mean.differences <- mean.differences[upper.tri(mean.differences, diag = T)]
    mean.differences <- sort(mean.differences)
    
    K.star <- n * (n+1) / 4 - stats::qnorm(1-alpha/2) * sqrt(n*(n+1)*(2*n+1) / 24)
    return(c(lb = mean.differences[ceiling(K.star)], 
             ub = mean.differences[floor(length(mean.differences)-K.star)]))
  }
  else{
    # Get number of samples of each algorithm
    n <- length(x)
    m <- length(y)
    
    # Compute and order n x m differences
    differences <- sapply(x, function(item) item - y)
    differences <- sort(as.vector(differences))
    
    # Compute number of samples away from median sample
    K  <- n * m / 2 - stats::qnorm(1-alpha/2) * sqrt(n*m*(n+m+1)/12)
    
    return(c(lb = differences[ceiling(K)], ub = differences[floor(n*m - K)]))
  }
}
