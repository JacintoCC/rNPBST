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
#' @description Returns a vector with an 1 in the maximum position. If there are more than one then 1/num. maximums is situated in each position
#' @param x Vector where find the maximum(s)
locate.max <- function(x){
  vec <- sapply(x, function(y){y == max(x)})
  return(vec/sum(vec))
}


#' @title Get data from data folderFunction to get a data file and assign it to a variable
#'
#' @description  Function to get a data file and assign it to a variable
#' @param ... Parameters to data function
#' @return Data to assign it to a variable
getdata <- function(...)
{
   e <- new.env()
   name <- utils::data(..., envir = e)[1]
   e[[name]]
}

#' @title Auxiliar function to get the difference between observations and
#         make checkings
#'
#' @description This function returns the difference between two vectors if they have the same length. If the second vector is missing, the first one is returned
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
