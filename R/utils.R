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
  UseMethod("htest2Tex")
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
htest2Tex.htest <- function(test){
  class(test) <- "list"
  htest2Tex(test)
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
htest2Tex.list <- function(test){
   tex.string <- paste("\\begin{table}[] \n\\centering\n\\caption{",
                       test$method,
                       " test} \n\\begin{tabular}{lll} \n\\hline\n",
                       sep = "")

   tex.string <- paste(tex.string,
                       "\\multicolumn{3}{c}{",
                       test$method, "} \\\\ \\hline\n",
                       sep = "")
   if("sample" %in% names(test)){
     test$sample <- NULL
   }
   if("dist" %in% names(test)){
     test$dist <- NULL
   }
   if("post.dist.lower" %in% names(test)){
     test$post.dist.lower <- NULL
     test$post.dist.upper <- NULL
   }

   names.items <- names(test)
   tex.items <- lapply(1:max(c(1,length(test)-1)),
                       function(i){
                          item <- test[[i]]
                          tex.item <- paste("\\multirow{",
                                            length(item),
                                            "}{*}{",
                                            names.items[i],
                                            "}\n\t & \t",
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

   return(paste(tex.string,
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
#' @param x Performance matrix of first algorithm
#' @param y Performance matrix of second algorithm
#' @return Occurence count vector
occurencesDominanceConfiguration <- function(x, y){
  # Build the dominance matrix
  dominance.matrix <- heaviside(x-y)
  n.measures <- ncol(dominance.matrix)

  weights.vector <- t(apply(dominance.matrix, 1, function(dominance.statement){
    weights <- numeric(length = 2^length(dominance.statement))
    occurrence.dominance.configuration <- create.permutations(dominance.statement)
    numbers <- apply(occurrence.dominance.configuration, 1, function(single.dominance.configuration){
      strtoi(paste(single.dominance.configuration, collapse = ""), base = 2) + 1
    })
    weights[numbers] <- 1 / nrow(occurrence.dominance.configuration)
    return(weights)
  }))
  return(colSums(weights.vector))
}


#' @title Create Permutations
#'
#' @description Auxiliar method for create permutations with the different possible results for a heaviside vector
#' @param x Vector
#' @return Data frame with all the different permutations.
create.permutations <- function(x){
  tie.location <- x == 0.5
  tie.num <- sum(tie.location)
  list.options <- as.list(x)
  for(i in which(tie.location)){
    list.options[[i]] <- c(0,1)
  }
  expand.grid(list.options)
}


#' @title Adjust Format Table
#'
#' @export
#' @description Auxiliar method for printing a p-value table with bold font in p-values less than 0.05
#' @param table Table with p-values
#' @param rownames Rownames for the table
#' @param colnames Colnames for the table
#' @param type Type of output. Default is latex.
#' @param print.code Boolean for printing the table or the code that generates that table
#' @param ... Extra arguments for xtable
#' @importFrom dplyr mutate_all
#' @return Latex code with the formatted table.
AdjustFormatTable <- function(table, rownames=NULL, colnames=NULL, type = "latex", print.code = FALSE, ...){
  if(!is.null(rownames)){ rownames(table) <- rownames }
  if(!is.null(colnames)){ colnames(table) <- colnames }

  if(type == "latex"){
    formated.table <- mutate_all(as.data.frame(table),
                                 function(x){
                                   sapply(x, function(y){
                                     formated.col <- ifelse(y < 0.05, paste0("\\(\\mathbf{", sprintf("%1.2e", y),"}\\)"),
                                                            paste0("\\(",sprintf("%1.2e", y),"\\)"))
                                     formated.col <- gsub("([0-9]{1,2})e([+-]?[0-9]{2})", "\\1 \\\\cdot 10^\\{\\2\\}", formated.col)
                                   })
                                 })
    rownames(formated.table) <- rownames(table)
    # return(formated.table)
    if(print.code){
      print(xtable::xtable(formated.table,comment = FALSE, ...),
            include.rownames=TRUE, sanitize.text.function = identity,
            type = "latex")
    }
    else{
      knitr::kable(formated.table)
    }
  }
  else{
    formated.table <- mutate_all(as.data.frame(table),
                                 function(x){
                                   sapply(x, function(y){
                                     formated.col <- ifelse(y < 0.05, paste0("<b>", sprintf("%1.2g", y),"</b>"),
                                                            sprintf("%1.2g", y))
                                     formated.col <- gsub("([0-9]{1,2})e([+-]?[0-9]{2})", "\\1 &middot; 10<sup>\\2</su>", formated.col)
                                   })
                                 })
    rownames(formated.table) <- rownames(table)

    print(xtable::xtable(formated.table, comment = FALSE, ...),
          include.rownames=TRUE, sanitize.text.function = identity,
          type = "html")
  }
}


apply.paired.bayesian <- function(matrix.dataset, test,...){
   m <- ncol(matrix.dataset)
   comb <- utils::combn(m, 2)

   test.result <- apply(comb, 2, function(c){
      single.result <- do.call(test, list(x = matrix.dataset[ ,c], ...))$probabilities
      return(single.result)
   }) %>%
      t %>% as.data.frame() %>%
      dplyr::mutate(Algorithm.1 = as.factor(colnames(matrix.dataset)[comb[1, ]]),
                    Algorithm.2 = as.factor(colnames(matrix.dataset)[comb[2, ]]))
   return(test.result)
}
