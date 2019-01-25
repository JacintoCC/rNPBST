#' @title Plot of posterior distributions of IDP
#'
#' @export
#' @description This function plots the posterior distribution of the parameter
#' @param x Imprecise Dirichlet Proccess Posterior distribution
#' @param ... Extra parameters for plot
#' @importFrom magrittr %>%
#' @importFrom rlang .data
#' @examples
#' idp <- bayesian.imprecise(results.rf[1, ], results.knn[1, ])
#' plot(idp)
plot.PosteriorIDP <- function(x, ...){
  lower.dist <- x$post.dist.lower
  upper.dist <- x$post.dist.upper
  
  idp.density <- data.frame(lower = lower.dist,
                            upper = upper.dist) %>%
    lapply(function(x) data.frame(stats::density(x)$x, stats::density(x)$y))
  idp.density <- do.call(cbind, idp.density) 
  colnames(idp.density) <- c("lower.x", "lower.y","upper.x","upper.y")
  
  idp.density <-rbind(as.matrix(idp.density[,1:2]), 
                       as.matrix(idp.density[,3:4])) %>%
    as.data.frame() %>%
    dplyr::transmute(Dist = rep(c("Lower", "Upper"), each = 512),
                     Area = rep(c(x$area.dist.lower, x$area.dist.upper), each = 512),
                     x = .data$lower.x, y = .data$lower.y)
  
  ggplot2::ggplot(idp.density, ggplot2::aes(x = .data$x, y = .data$y, color = .data$Dist)) +
    ggplot2::geom_line() +
    ggplot2::geom_ribbon(data = idp.density[idp.density$x >= 0.5, ],
                         ggplot2::aes(.data$x, ymin=0, ymax=.data$y, fill = .data$Dist), alpha = 0.3) +
    ggplot2::xlab("P(X <= Y)") +
    ggplot2::ylab("Density") +
    ggplot2::scale_color_discrete(name="Posterior\ndistribution\narea", labels = c(sprintf('Lower: %.3f', x$area.dist.lower),
                                                                                   sprintf('Upper: %.3f', x$area.dist.upper))) +
    ggplot2::scale_fill_discrete(name="Posterior\ndistribution\narea", labels = c(sprintf('Lower: %.3f', x$area.dist.lower), 
                                                                                  sprintf('Upper: %.3f', x$area.dist.upper)))
}
