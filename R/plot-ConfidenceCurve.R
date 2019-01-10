#' Plot Confidence Curve
#' Plot confidence intervals for all levels of significance.
#'
#' @export
#' @param x First algorithm's results
#' @param y Second algorithm's results
#' @param paired Boolean that indicates if the samples are paired
#' @param alpha Significance value
#' @param m The number of nested confidence curves to be computed. Default is 100.
#' @return Graphic
#' @export
PlotConfidenceCurve <- function(x, y, paired = FALSE, alpha = 0.05, m = 10){
  alpha.values <- c(seq(0.01, 1, length.out = m), alpha)
  alpha.values <- alpha.values[order(alpha.values)]
  
  conf.intervals <- sapply(alpha.values,
                           function(alpha){
                             NP.ApproximateConfidenceInterval(x, y, alpha = alpha,
                                                             paired = paired)
                           })
  df <- data.frame("alpha" = c(alpha.values,rev(alpha.values)),
                   "bounds" = c(conf.intervals["lb", ],
                                rev(conf.intervals["ub", ])))
  
  med = conf.intervals[1,m+1]
  
  graphic <- ggplot2::ggplot(df, ggplot2::aes(x = .data$bounds, y = .data$alpha)) +
    ggplot2::geom_line() +
    ggplot2::geom_vline(xintercept = med, linetype = "dotted") +
    ggplot2::geom_vline(xintercept = 0) +
    ggplot2::geom_hline(yintercept = alpha, linetype="dotted") +
    ggplot2::annotate("text", x=med, y=0, label=as.character(signif(med)), color="red") + 
    ggplot2::labs(x = "True difference in performance",
                  y = "p-value")
  return(graphic)
}

