#' @title Compute exact probability of Page distribution
#'
#' @description Function to get the exact probability of Page distribution
#' @param N number of columns
#' @param k number of rows
#' @param L Page statistic
#' @return Exact p-value computed
computePageExactProbability <- function(N, k, L){
   if(N <= 8 & N >= 3 & k <= 12 & k >= 2){
      pvalues <- PageTable$p[PageTable$N == N &
                                PageTable$k == k &
                                PageTable$L < L]
      
      pvalue <- ifelse(length(pvalues) > 0, pvalues[1], 1)
      return(pvalue)
   }
   
   return(-1)
}

#' @title Compute asymptotic probability of distribution
#'
#' @description Function to get the asymptotic probability given a distribution table
#' @param N number of columns
#' @param k number of rows
#' @param L Page statistic
#' @return Exact p-value computed
computePageAsymptoticProbability <- function(N, k, L){
   
   numerator <- 12 * (L - 0.5) - 3 * k * N * (N + 1) * (N + 1)
   denominator <- N * (N + 1) * sqrt(k * (N - 1))
   Z <- numerator / denominator
   
   return(1 - stats::pnorm(Z))
}

#' @title Page test for multiple comparisons
#'
#' @export
#' @description This function performs the Page test
#' @param x First matrix of data
#' @param y Second matrix of data
#' @return A list with pvalues for alternative hypothesis, statistics, method and data name
page.test <- function(x, y = NULL){
    if(is.null(y)){
       matrix <- x
    }
    else{
       matrix <- x - y
    }
   
    if(ncol(matrix) < 3)
        stop("Extended median test only can be employed with more than two samples")
    
    if(anyNA(matrix))
        stop("Null values are not allowed in this test.")
   
    ranks <- t(apply(matrix, 1, rank))

    adjust.ranks.page <- function(x, y, rank){
        x.0 <- x == 0
        y.0 <- y == 0
        if(sum(x.0) == sum(y.0)){
            return(rank)
        } else if (sum(x.0) > sum(y.0)) {
            return(c(rank(rank[!x.0]), (1:length(x))[x.0]))
        } else{
            return(c(((sum(y.0)+1):length(x))[rank(rank[!y.0])], sum(y.0):1))
        }
    }
    
    # Modification of ranks
    if(!is.null(y)){
        adjusted.ranks <- mapply(FUN = adjust.ranks.page,
                                x = as.data.frame(t(x)), 
                                y = as.data.frame(t(y)), 
                                rank = as.data.frame(t(ranks)))
        ranks <- t(adjusted.ranks)
    }
    
    sumRanks <- apply(ranks, 2, sum)
    L <- sum(sumRanks * 1:ncol(matrix))
    
    exact.pvalue <- computePageExactProbability(ncol(matrix), nrow(matrix), L)
    asymptotic.pvalue <- computePageAsymptoticProbability(ncol(matrix), nrow(matrix), L)
    
    if(exact.pvalue == -1){
       pvalues <- c("Asymptotic pvalue" = asymptotic.pvalue)
    }
    else{
       pvalues <- c("Exact pvalue <=", exact.pvalue,
                    "Asymptotic pvalue" = asymptotic.pvalue)
    }
    htest <- list(data.name = paste(deparse(substitute(x)), 
                                    ifelse(is.null(y), "", deparse(substitute(y))), sep = "-"),
                 statistic = c("L" = L), p.value = pvalues,
                 method = "Page")
    return(htest)
}