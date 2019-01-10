#' @title Plot posterior Dirichlet distribution
#'
#' @export
#' @description Plot of the projection of 3-simplex points of a posterior Dirichlet distribution into a 2D triangle
#' @param x Simplex points to be projected
#' @param num.points Number of points to be shown
#' @param ... Extra parameters for plot
#' @examples
#' bs <- bayesianSign.test(results$random.forest, results$KNN)
#' plot(bs, num.points = 5000)
#'
#' bsr <- bayesianSignedRank.test(results$random.forest, results$KNN)
#' plot(bsr, num.points = 5000)
plot.PosteriorDirichlet <- function(x, num.points = nrow(x$sample), ...){
  x <- x$sample
   # Reduce the number of points to plot
   if(num.points > nrow(x))
      num.points = nrow(x)

   subset <- x[sample(nrow(x), num.points), ]

   L <- subset[ ,1]
   rope <- subset[ ,2]
   R <- subset[ ,3]

   # Build
   df.points <- data.frame("L" = L, "rope" = rope, "R" = R,
                           "d" = grDevices::densCols(subset,
               colramp = grDevices::colorRampPalette(grDevices::heat.colors(20))))

   # Lines to separe regions
   lines <- data.frame(x = c(0.5, 0, 0.5), y = c(0, 0.5, 0.5),
                       z = c(0.5, 0.5, 0), xend = c(1,1,1)/3,
                       yend = c(1,1,1)/3, zend = c(1,1,1)/3)
   borders <- data.frame(x = c(1,0,0), y=c(0,1,0), z=c(0,0,1),
                         xend = c(0,1,0), yend=c(0,0,1), zend=c(1,0,0))

   # Warnings here alert about z parameters not used in aes function,
   # altough they actually are.
   suppressWarnings(
      ggtern::ggtern(data = df.points, ggtern::aes(L, rope, R)) +
         ggplot2::geom_point(color = df.points$d) +
         ggplot2::geom_segment(data = lines,
                               ggtern::aes(x = c(0.5, 0, 0.5), y = c(0, 0.5, 0.5),
                                           z = c(0.5, 0.5, 0), xend = c(1,1,1)/3,
                                           yend = c(1,1,1)/3, zend = c(1,1,1)/3),
                               color = 'orange', size = 0.5) +
         ggplot2::geom_segment(data = borders,
                               ggtern::aes(x = c(1,0,0), y=c(0,1,0), z=c(0,0,1),
                                           xend = c(0,1,0), yend=c(0,0,1),
                                           zend=c(1,0,0)),
                               color = 'orange', size = 1))
}
