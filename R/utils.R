#' @title Heaviside step function
#'
#' @export
#' @description This function implements Heaviside step function
#' @param x Object to be evaluated
#' @param a Frontier value
heaviside <- function(x, a=0){
  return( (sign(x-a) + 1) / 2 )
}

#' @title Location of the maximum(s) in a vector
#'
#' @export
#' @description Returns a vector with an 1 in the maximum position. If
#'     there are more than one then 1/num. maximums is situated in
#'     each position
#' @param x Vector where find the maximum(s)
locate.max <- function(x){
  vec <- sapply(x, function(y){y == max(x)})
  return(vec/sum(vec))
}


#' @title Get data from data folderFunction to get a data file and
#'     assign it to a variable
#'
#' @description Function to get a data file and assign it to a
#'     variable
#' @param ... Parameters to data function
#' @return Data to assign it to a variable
getData <- function(...)
{
   e <- new.env()
   name <- utils::data(..., envir = e)[1]
   e[[name]]
}

#' @title Auxiliar function to get the difference between observations
#         and make checkings
#'
#' @description This function returns the difference between two
#'     vectors if they have the same length. If the second vector is
#'     missing, the first one is returned
#' @param x First vector
#' @param y Second vector
getDiff <- function(x, y){
   # Check if the data corresponds with a pair of
   # observations or the difference between observations
   if(is.null(y))
      diff <- x
   else if(length(x) != length(y))
      stop("X and Y must be of the same length")
   else
      diff <- x - y
   
   if(length(dim(x)) == 2){
      if(ncol(x) == 2)
         diff <- x[,1]-x[,2]
   }

   return(diff)
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
htest2Tex <- function(test){
   tex.string <- paste("\\begin{table}[] \n\\centering \\caption{",
                       test$method,
                       " test} \n\\begin{tabular}{lll} \n\\hline\n",
                       sep = "")

   tex.string <- paste(tex.string,
                       "\\multicolumn{3}{c}{",
                       test$method, " test} \\\\ \\hline\n",
                       sep = "")

   names.items <- names(test)
   tex.items <- lapply(1:(length(test)-1),
                       function(i){
                          item <- test[[i]]
                          tex.item <- paste("\\multirow{",
                                            length(item),
                                            "}{*}{",
                                            names.items[i],
                                            "} \t & \t",
                                            sep = "")
                          names.subitems <- names(item)
                          tex.item.subitems <- rbind(names.subitems,
                                                     rep("\t & \t", length(item)),
                                                     format(item, digits = 4,
                                                            nsmall = 2),
                                                     c(rep("\t\\\\\n \t & \t",
                                                           length(item)-1),
                                                       "\t \\\\ \\hline"))
                          return(paste(tex.item,
                                       paste(tex.item.subitems,
                                             collapse = ""),
                                       sep = ""))
                       })

   cat(paste(tex.string,
             paste(tex.items, collapse = "\n"),
             "\\end{tabular}",
             "\\end{table}",
             sep="\n"))
}

#' @title Checks Conditions
#'
#' @description Checks conditions for bivariate non parametric tests
#' @param matrix Matrix of data
checkBivariateConditions <- function(matrix){
   # Checks
   if(ncol(matrix) != 2)
      stop("This test only can be employed with two samples")

   if(nrow(matrix) < 3)
      stop("This test need samples of size 3 or more")

   if(anyNA(matrix))
      stop("No null values allowed in this test.")
}


#' @title Daniel Trend test for bivariated samples
#'
#' @export
#' @description This function performs the Daniel Trend test
#' @param matrix Matrix of data
#' @examples
#' x <- 1:10
#' m <- matrix(c(x, 2*x+rnorm(length(x))), ncol = 2)
#' danielTrend.test(m)
#' @return A list with pvalues for alternative hypothesis, statistics, method and data name
danielTrend.test <- function(matrix){
   # Check Conditions
   checkBivariateConditions(matrix)

   n <- nrow(matrix)

   # Ascending rank for both columns
   rank.first <- rank(matrix[ ,1])
   rank.second <- rank(matrix[ ,2])

   # Compute sum D statistic
   sumD <- sum( (rank.first - rank.second)^2 )

   # Compute R statistic
   R <- 1 - (6 * sumD) / (n * (n*n - 1))

   # Compute P-Values
   if(n <= 10){
      SpearmanExactTable <- getData("SpearmanExactTable")
      pvalue <- computeExactProbability(SpearmanExactTable, n, R)
   }
   else if(n <= 30){
      SpearmanQuantileTable <-  getData("SpearmanQuantileTable")
      pvalue <- computeAproximatedProbability(SpearmanQuantileTable, n, R)
   }

   # Compute asymptotic p-value
   Z <- R * sqrt(n-1)

   positive.dependence.pvalue <- 1 - stats::pnorm(Z)
   negative.dependence.pvalue <- stats::pnorm(Z)
   no.dependence.pvalue <- 2 * min(positive.dependence.pvalue,
                                   negative.dependence.pvalue)

   statistic <- c(D = sumD, R = R, Z = Z)
   if(n <= 30){
      pvalues <- c("pvalue" = pvalue,
                   "Positive Dependence pvalue" = positive.dependence.pvalue,
                   "Negative Dependence pvalue" = negative.dependence.pvalue,
                   "No Dependence pvalue" = no.dependence.pvalue)
   }
   else{
      pvalues <- c("Positive Dependence pvalue" = positive.dependence.pvalue,
                   "Negative Dependence pvalue" = negative.dependence.pvalue,
                   "No Dependence pvalue" = no.dependence.pvalue)
   }

   htest <- list(data.name = deparse(substitute(matrix)),
                 statistic = statistic, p.value = pvalues,
                 method = "Daniel Trend")
   return(htest)
}


#' @title Kendall test for bivariated samples
#'
#' @export
#' @description This function performs the Kendall test
#' @param matrix Matrix of data
#' @examples
#' x <- 1:10
#' m <- matrix(c(x, 2*x+rnorm(length(x))), ncol = 2)
#' kendall.test(m)
#' @return A list with pvalues for alternative hypothesis, statistics,
#'     method and data name
kendall.test <- function(matrix){
   checkBivariateConditions(matrix)

   n <- nrow(matrix)

   # Ascending rank for both columns
   rank.first <- rank(matrix[ ,1])
   rank.second <- rank(matrix[ ,2])

   # Sort pairs of ranks according to first sample
   rank.matrix <- matrix(c(rank.first, rank.second), ncol = 2)
   rank.matrix <- rank.matrix[order(rank.first), ]

   # Print ranks
   print("Ranks computed")
   print(rank.matrix)

   # Compute C and Q statistic
   C <- vector("numeric", length = n)
   Q <- vector("numeric", length = n)

   for(i in n:2){
      value <- rank.matrix[i, 2]
      C[(i-1):1] <- C[(i-1):1] + (rank.matrix[(i-1):1,2] < value)
      Q[(i-1):1] <- Q[(i-1):1] + (rank.matrix[(i-1):1,2] > value)
   }

   C <- sum(C)
   Q <- sum(Q)

   # Compute T statistic
   T <- 2 * (C - Q) / (n * (n-1))

   # Compute P-Values
   if(n <= 10){
      KendallExactTable <- getData("KendallExactTable")
      pvalue <- computeExactProbability(KendallExactTable, n, T)
      print(pvalue)
   }
   else if(n <= 30){
      KendallQuantileTable <- getData("KendallQuantileTable")
      pvalue <- computeAproximatedProbability(KendallQuantileTable, n, T)
   }

   # Compute asymptotic p-value
   numerator <- 3 * T * sqrt(n * (n-1))
   stdDev <- sqrt(2 * (2*n + 5))
   Z <- numerator / stdDev

   positive.dependence.pvalue <- 1 - stats::pnorm(Z)
   negative.dependence.pvalue <- stats::pnorm(Z)
   no.dependence.pvalue <- 2 * min(positive.dependence.pvalue,
                                   negative.dependence.pvalue)

   statistic <- c(T = T, C = C, Q = Q, Z = Z)
   if(n <= 30){
      pvalues <- c("Exact pvalue" = pvalue,
                   "Positive Dependence pvalue" = positive.dependence.pvalue,
                   "Negative Dependence pvalue" = negative.dependence.pvalue,
                   "No Dependence pvalue" = no.dependence.pvalue)
   }
   else{
      pvalues <- c("Positive Dependence pvalue" = positive.dependence.pvalue,
                   "Negative Dependence pvalue" = negative.dependence.pvalue,
                   "No Dependence pvalue" = no.dependence.pvalue)
   }

   htest <- list(data.name = deparse(substitute(matrix)),
                 statistic = statistic, p.value = pvalues,
                 method = "Kendall")
   return(htest)
}

#' @title Occurences Dominance Configuration
#'
#' @description Count the occurences for each possible dominance
#'     configuration
#' @param dominance.matrix Dominance Matrix
#' @return Occurence count vector
occurencesDominanceConfiguration <- function(dominance.matrix){
    count.vector <- sapply(0:(2**n.measures - 1),
                           function(i){
                               binary.vector <- rev(intToBits(i)[1:n.measures])
                               coincidence.vector <- apply(dominance.matrix, 1,
                                                           function(r) all(r == binary.vector))
                               return(sum(coincidence.vector))
                           })
    return(count.vector)
}
