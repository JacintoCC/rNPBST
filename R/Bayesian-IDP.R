#' @title Bayesian Imprecise Dirichlet Prior
#'
#' @export
#' @description Bayesian Test for two samples based on the Imprecise Dirichlet Theory.
#' @param x First vector of observations
#' @param y Second vector of observations
#' @param s Prior strength. Default sqrt(2) - 1
#' @param c Probability needed of one algorithm outperforming the other. Default 0.5.
#' @param mc.samples Number of Monte Carlo samples.
#' @return List with posterior distribution and probability of exceeding c.
bayesian.imprecise <- function(x, y, s = sqrt(2) - 1, c = 0.5, mc.samples = 10000){
    
    weights.dir <- list(x = c(s, rep(1,length(x))),
                        y = c(s, rep(1,length(y))))

    w <- list(x = MCMCpack::rdirichlet(mc.samples, weights.dir$x),
              y = MCMCpack::rdirichlet(mc.samples, weights.dir$y))

    y.greater.x.indicator <- sapply(x, function(x.j) (sign(y - x.j) + 1) / 2)

    g.lower <- function(w.x.s, w.y.s, indicator){
        product <- matrix(w.x.s[-1], ncol = 1) %*% matrix(w.y.s[-1], nrow = 1)
        lower.bound <- sum(indicator * product)
        return(lower.bound)
    }

    posterior.distribution.lower <- mapply(
        g.lower,
        w.x = as.data.frame(t(w$x)),
        w.y = as.data.frame(t(w$y)),
        MoreArgs = list(indicator = y.greater.x.indicator)
    )

    g.upper <- function(w.x.s, w.y.s, indicator){
        product <- matrix(w.x.s[-1], ncol = 1) %*% matrix(w.y.s[-1], nrow = 1)

        upper.bound <- w.x.s[1] * w.y.s[1] +
            sum(w.x.s[1] * w.y.s[-1]) +
            sum(w.x.s[-1] * w.y.s[1]) +
            sum(indicator * product)

        return(upper.bound)
    }

    posterior.distribution.upper <- mapply(
        g.upper,
        w.x = as.data.frame(t(w$x)),
        w.y = as.data.frame(t(w$y)),
        MoreArgs = list(indicator = y.greater.x.indicator)
    )
    
    posteriorIDP <- list(post.dist.lower = posterior.distribution.lower,
                         post.dist.upper = posterior.distribution.upper,
                         area.dist.lower = mean(posterior.distribution.lower > c),
                         area.dist.upper = mean(posterior.distribution.upper > c))
    class(posteriorIDP) <- "PosteriorIDP"

    return(posteriorIDP)
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
htest2Tex.PosteriorIDP <- function(test){
  tex.string <- paste("\\begin{table}[] \n\\centering\n \\caption{Bayesian IDP test} \n",
                      "\\begin{tabular}{ll} \n\\hline\n",
                      "\\multicolumn{2}{c}{Bayesian IDP test} \\\\ \\hline\n",
                      "\\multirow{1}{*}{area.dist.lower}\n",
                      "\t& ", test$area.dist.lower, " \\\\ \\hline\n",
                      "\\multirow{1}{*}{area.dist.upper}\n",
                      "\t& ", test$area.dist.upper, " \\\\ \\hline\n",
                      "\\end{tabular}\n",
                      "\\end{table}\n",
                      sep = "")
  return(tex.string)
}
