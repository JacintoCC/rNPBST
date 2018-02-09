#' @title Iman-Davenport test
#'
#' @export
#' @description This function performs the Iman-Davenport test
#' @param matrix Matrix of data
#' @examples
#' imanDavenport.test(results)
#' @return A list with pvalues for alternative hypothesis, statistics, method and data name
imanDavenport.test <- function(matrix){
   n <- nrow(matrix)
   k <- ncol(matrix)
   
   if(k < 3)
      stop("Extended median test only can be employed with more than two samples")
   
   if(anyNA(matrix))
      stop("No null values allowed in this test.")
   
   aligned.obs <- matrix - matrix(rep(rowMeans(matrix), k), nrow = n)
   ranked.obs <- matrix(rank(aligned.obs, ties.method = "average"), nrow = n)
   
   T <- (k-1) * (sum(colSums(ranked.obs)^2) - k*n^2*(k*n+1)^2/4) /
      (k*n*(k*n+1)*(2*k*n+1)/6 - sum(rowSums(ranked.obs)^2) / k )
   
   F.id <- (n-1) * T / (n * (k-1) - T)
   statistic <- c(F.id = F.id)
   pvalues <- doubleTailProbability(stats::pf(F.id, k-1, (k-1)*(n-1)),
                                    1-stats::pf(F.id, k-1, (k-1)*(n-1)))
   
   htest <- list(data.name = deparse(substitute(matrix)),
                 statistic = statistic, p.value = pvalues,
                 method = "Iman-Davenport")
   return(htest)
}
