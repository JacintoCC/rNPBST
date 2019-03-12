#' @title Bayesian Sign test
#'
#' @export
#' @description This function performs the Bayesian Sign test
#' @param x First vector of observations
#' @param y Second vector of observations
#' @param z_0 Prior pseudo-observation
#' @param s Prior pseudo-observation probabilitie
#' @param rope.min Inferior limit of the rope considered
#' @param rope.max Superior limit of the rope considered
#' @param weights A priori weights
#' @param n.samples Number of samples of the distributio
#' @examples
#' bs <- bayesianSign.test(results$random.forest, results$KNN)
#' bs.stronger.prior <- bayesianSign.test(results$random.forest, results$KNN, s=3, z_0 = 0.5)
#' @return List with probabilities for each region and a sample of
#'     posterior distribution.
bayesianSign.test <- function(x, y = NULL, s = 1, z_0 = 0,
                              rope.min = -0.01, rope.max = 0.01,
                              weights = c(s/2, rep(1, length(x))),
                              n.samples = 100000){
   
   if(rope.min > rope.max)
      stop("rope.min should be smaller than rope.min")
   
   diff <- getDiff(x, y)
   n.diff <- length(diff)
   
   if(rope.min != rope.max){
      # Belonging to an interval
      belongs.left <- diff < rope.min
      belongs.rope <- diff > rope.min & diff < rope.max
      belongs.right <- diff > rope.max
      
      # Compute counts
      n.left  <- sum(belongs.left)
      n.rope  <- sum(belongs.rope)
      n.right <- sum(belongs.right)
      
      # Generate a random sample from Dirichlet distribution
      weights <- c(n.left+s/3, n.rope+s/3, n.right+s/3)
   }
   else{
      # Compute number of elements in each region
      n.left  <- sum(diff < rope.min) + 0.5 * sum(diff == rope.min)
      n.right  <- n.diff - n.left
      
      # Adjust weights
      if(n.left > n.right)
         weights <- c(n.left+s, n.right)
      else if(n.left < n.right)
         weights <- c(n.left, n.right + s)
      else
         weights <- c(n.left + s/2, n.right + s/2)
   }
   
   # Take a sample from Dirichlet distribution
   sample <- MCMCpack::rdirichlet(n.samples, weights)
   posterior.prob <- colMeans(sample)
   
   if(rope.min != rope.max){
     probabilities <- c(left = posterior.prob[1],
                        rope = posterior.prob[2],
                        right = posterior.prob[3])
     
   }
   else{
     probabilities <- c(left = posterior.prob[1],
                        right = posterior.prob[2])
   }
   
   posterior <- list(probabilities = probabilities,
                     sample = sample,
                     method = "Bayesian Sign Test")
   class(posterior) <- "PosteriorDirichlet"
   
   return(posterior)
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
htest2Tex.PosteriorDirichlet <- function(test){
  tex.string <- paste("\\begin{table}[] \n",
                      "\\centering\n",
                      "\\caption{", test$method,"}\n",
                      "\\begin{tabular}{lll} \n",
                      "\\hline\n",
                      "\\multirow{3}{*}{Probabilities}\n",
                      "& 	left	 &", test$probabilities[1],"\\\n",
                      ifelse(length(test$probabilities) == 3,
                             paste0("& 	rope	 &", test$probabilities[2],"\\\n",
                                    "& 	right	 &", test$probabilities[3],"\\ \\hline\n"),
                             paste0("& 	right	 &", test$probabilities[2],"\\ \\hline\n")),
                      "\\end{tabular}",
                      "\\end{table}",
                      sep = "")
  
  return(tex.string)
}