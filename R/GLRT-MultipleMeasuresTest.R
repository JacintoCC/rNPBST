#' @title Check Multiple Measures Conditions
#'
#' @description Check the safety conditions for Multiple Measures Tests
#' @param x Performance matrix of first algorithm
#' @param y Performance matrix of second algorithm
checkMultipleMeasuresConditions <- function(x, y){
    if(ncol(x) != ncol(y))
        stop("Both matrix must have the same number of performance measurements")
}


#' @title Generalized Likelihood Ratio Test for Multiple Performance
#'     Measures
#'
#' @export
#' @description Performs the GLRT for the joint analysis of multiple
#'     performance measures.
#' @param x Performance matrix of first algorithm
#' @param y Performance matrix of second algorithm
#' @return List with the probabilites of each possible dominance
#'     configuration.
multipleMeasuresGLRT <- function(x, y){

    # Check safety conditions of Multiple Performance Measures Tests
    checkMultipleMeasuresConditions(x,y)

    # Get the number of measures
    n.measures <- ncol(x)
    # Get the number of occurences of each dominance configuration
    count.vector <- rNPBST:::occurencesDominanceConfiguration(x, y)

    # Conform the statistic
    n.a <- max(count.vector)
    n.b <- max(count.vector[-which.max(count.vector)])
    
    lambda.statistic <- mean(c(n.a,n.b)) ^ (n.a + n.b) / (n.a^n.a * n.b^n.b)

    # p-value
    p.value <- 1 - stats::pchisq(-2*log(lambda.statistic),1)

    htest <- list(data.name = "x",
                  statistic = list("n.vector" = count.vector,
                                   "lambda" = lambda.statistic),
                  p.value = p.value,
                  method = "GLRT Multiple Measures")
    
    return(htest)
}
