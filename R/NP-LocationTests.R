#' @title Wilcoxon exact left p-value
#'
#' @description Compute left p-value for Wilcoxon rank sum test
#' @param n n parameter
#' @param m m parameter
#' @param R rank associated
#' @return pvalue associated
computeWilcoxonRankLeftProbability <- function(n, m, R){
   WilcoxonRanksSumTable <- getdata("WilcoxonRanksSumTable.rda")

   if(n > 10 | m > 10 | R > 105)
      return(NULL)
   else
      return(WilcoxonRanksSumTable$distribution[WilcoxonRanksSumTable$x == n &
                                                   WilcoxonRanksSumTable$y == m &
                                                   WilcoxonRanksSumTable$z == R])
}

#' @title Wilcoxon exact right p-value
#'
#' @description Compute right p-value for Wilcoxon rank sum test
#' @param n n parameter
#' @param m m parameter
#' @param R rank associated
#' @return pvalue associated
computeWilcoxonRankRightProbability <- function(n, m, R){
   WilcoxonRanksSumTable <- getdata("WilcoxonRanksSumTable.rda")

   if(n > 10 | m > 10 | R > 105)
      return(NULL)

   maximum <- matrix( c(1,2,2,3,3,4,4,5,5,6,
                        0,5,6,7,8,9,10,11,12,13,
                        0,0,10,12,13,15,16,18,19,21,
                        0,0,0,18,20,22,24,26,28,30,
                        0,0,0,0,27,30,32,35,37,40,
                        0,0,0,0,0,39,42,45,48,51,
                        0,0,0,0,0,0,52,56,59,63,
                        0,0,0,0,0,0,0,68,72,76,
                        0,0,0,0,0,0,0,0,85,90,
                        0,0,0,0,0,0,0,0,0,105),
                      ncol = 10, nrow = 10, byrow = T)

   max <- maximum[n,m]

   if(R <= max)
      return(NULL)

   trueR <- ifelse(max%%2 == 0, 2 * max - R, 2 * max - (R-1))
   getdata("WilcoxonRanksSumTable.rda")

   return(WilcoxonRanksSumTable$distribution[WilcoxonRanksSumTable$x == n &
                                                WilcoxonRanksSumTable$y == m &
                                                WilcoxonRanksSumTable$z == trueR])

}

#' @title Wilcoxon Rank p-values
#'
#' @description Compute p-values for Wilcoxon
#' @param combined Vector of combined samples
#' @param n1 Size of first sample
#' @param n2 Size of second sample
#' @param WRank Statistic
#' @return pvalues
computeWilcoxonRankPValues <- function(combined, n1, n2, WRank){
   # Create ties vector
   n <- n1 + n2
   ties <- combined == c(combined[1], combined[-n])
   pvalues <- c()

   if(n1 <= 10 & n2 <= 10){
      rank <- WRank
      exact.left.tail <- computeWilcoxonRankLeftProbability(n2, n1, rank)
      exact.right.tail <- computeWilcoxonRankRightProbability(n2, n1, rank)

      if(is.null(exact.right.tail) || exact.right.tail == -1)
         exact.right.tail <- 1 - exact.left.tail
      if(is.null(exact.left.tail) || exact.left.tail == -1)
         exact.right.tail <- 1 - exact.right.tail

      exact.double.tail <- doubleTailProbability(exact.left.tail, exact.right.tail)
      pvalues <- c("Exact Left Tail" = exact.left.tail,
                   "Exact Right Tail" = exact.right.tail,
                   "Exact Double Tail" = exact.double.tail)
   }

   pointer <- 0
   sum.ties <- 0
   actual.tie <- 0

   for(i in ties){
      if(i)
         actual.tie <- actual.tie + 1
      else{
         if(actual.tie > 0){
            sum.ties <- sum.ties + actual.tie * (actual.tie * actual.tie - 1)
            actual.tie <- 0
         }
      }
   }

   if(ties[n])
      sum.ties <- sum.ties + actual.tie * (actual.tie * actual.tie - 1)

   # Compute variance
   denominator <- sqrt(n1 * n2 * (n1 + n2 + 1) / 12 - (n1 * n2 *sum.ties) / (12 * (n1 + n2) * (n1 + n2 - 1)))
   numerator <- WRank - 0.5 - n1 * (n1 + n2 + 1) / 2

   z <- numerator / denominator

   asymptotic.right.tail <- 1 - stats::pnorm(z)

   numerator <- WRank + 0.5 - n1 * (n1 + n2 + 1) / 2
   z <- numerator / denominator

   asymptotic.left.tail <- stats::pnorm(z)
   asymptotic.double.tail <- doubleTailProbability(asymptotic.left.tail,asymptotic.right.tail)

   pvalues <- c(pvalues,
                "Asymptotic Left Tail" = asymptotic.left.tail,
                "Asymptotic Right Tail" = asymptotic.right.tail,
                "Asymptotic Double Tail" = asymptotic.double.tail)
   return(pvalues)
}


#' @title Wilcoxon Rank Sum test for location
#'
#' @export
#' @description This function performs the Wilcoxon Rank Sum test
#' @param matrix Matrix of data
#' @return A htest object with pvalues and statistics
wilcoxonRankSum.test <- function(matrix){

   if(ncol(matrix) != 2)
      stop("Wilcoxon Ranks-Sum test only can be employed with two samples")

   # Remove NA's or NULL values
   sample1 <- matrix[ ,1]
   sample2 <- matrix[ ,2]

   sample1 <- sample1[!is.na(sample1) & !is.null(sample1)]
   sample2 <- sample2[!is.na(sample2) & !is.null(sample2)]

   n1 <- length(sample1)
   n2 <- length(sample2)

   # Sort samples
   sample1 <- sample1[order(sample1)]
   sample2 <- sample2[order(sample2)]

   # Create combined sample
   combined <- c(sample1, sample2)
   combined <- combined[order(combined)]

   # Create rank vector
   rank.combined <- rank(combined)

   pointer1 <- 1
   pointer2 <- 1
   WRank <- 0

   if(n1 <= n2)
      shorter.sample <- sample1
   else
      shorter.sample <- sample2

   shorter.n <- min(n1, n2)
   larger.n <- max(n1, n2)

   while(pointer1 <= shorter.n){
      while(combined[pointer2] != shorter.sample[pointer1] && pointer2 <= larger.n)
         pointer2 <- pointer2 + 1

      WRank <- WRank + rank.combined[pointer2]
      pointer1 <- pointer1 + 1
   }

   pvalues <- computeWilcoxonRankPValues(combined, n1, n2, WRank)

   htest <- list(data.name = deparse(substitute(matrix)),
                 statistic = WRank, p.value = pvalues,
                 method = "Wilcoxon Rank Sum")
   return(htest)
}
