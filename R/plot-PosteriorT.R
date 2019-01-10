#' @title Plot of posterior t distribution 
#'
#' @export
#' @description This function plots the posterior distribution of the parameter
#' @param x Data of the posterior distribution
#' @param ... Extra parameters for plot
#' @examples
#' cbt <- bayesianCorrelatedT.test(results.rf[1, ], results.knn[1, ])
#' plot(cbt) + ggplot2::ggtitle("Random Forest vs. KNN \nDataset: abalone")
plot.PosteriorT <- function(x, ...) {
  rope <- x$rope
  ggplot2::qplot(x$dist$x, x$dist$y, geom = "line") +
    ggplot2::xlab("Difference") +
    ggplot2::ylab("Value") +
    ggplot2::geom_area(ggplot2::aes(fill="distribution"), fill = "lightblue") +
    ggplot2::geom_line(color="darkblue") +
    ggplot2::geom_vline(xintercept = rope[1], color = "orange") +
    ggplot2::geom_vline(xintercept = rope[2], color = "orange") +
    ggplot2::geom_segment(data = data.frame(x = rope[1], y = 0, rope[2], yend = 0),
                          ggplot2::aes(x = rope[1], y = 0, xend=rope[2], yend = 0),
                          color = "orange")
}