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

#' @title Occurences Dominance Configuration
#'
#' @description Count the occurences for each possible dominance
#'     configuration
#' @param dominance.matrix Dominance Matrix
#' @return Occurence count vector
occurencesDominanceConfiguration <- function(dominance.matrix){
   n.measures <- ncol(dominance.matrix)
    count.vector <- sapply(0:(2**n.measures - 1),
                           function(i){
                               binary.vector <- rev(intToBits(i)[1:n.measures])
                               coincidence.vector <- apply(dominance.matrix, 1,
                                                           function(r) all(r == binary.vector))
                               return(sum(coincidence.vector))
                           })
    return(count.vector)
}
