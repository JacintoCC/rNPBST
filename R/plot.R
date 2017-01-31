#' @title Projection of 3-simplex points
#'
#' @export
#' @description This function projects 3-simplex points to a 2D triangle
#' @param points Simplex points to be projected
plotSimplex <- function(points){
   L <- points[ ,1]
   rope <- points[ ,2]
   R <- points[ ,3]
   df.points <- data.frame("L" = L, "rope" = rope, "R" = R,
                           "d" = grDevices::densCols(points,
                     colramp = grDevices::colorRampPalette(grDevices::heat.colors(100))))

   lines <- data.frame(x = c(0.5, 0, 0.5), y = c(0, 0.5, 0.5),
                       z = c(0.5, 0.5, 0), xend = c(1,1,1)/3,
                       yend = c(1,1,1)/3, zend = c(1,1,1)/3)
   borders <- data.frame(x = c(1,0,0), y=c(0,1,0), z=c(0,0,1),
                         xend = c(0,1,0), yend=c(0,0,1), zend=c(1,0,0))

   ggtern::ggtern(data = df.points, ggplot2::aes(L, rope, R)) +
      ggplot2::geom_point(color = df.points$d) +
      ggplot2::geom_segment(data = lines,
                            ggplot2::aes(x = c(0.5, 0, 0.5), y = c(0, 0.5, 0.5),
                                         z = c(0.5, 0.5, 0), xend = c(1,1,1)/3,
                                         yend = c(1,1,1)/3, zend = c(1,1,1)/3),
                            color = 'orange', size = 0.5) +
      ggplot2::geom_segment(data = borders,
                            ggplot2::aes(x = c(1,0,0), y=c(0,1,0), z=c(0,0,1),
                                         xend = c(0,1,0), yend=c(0,0,1),
                                         zend=c(1,0,0)),
                            color = 'orange', size = 1)
}

#' @title Plot of posterior distribution
#'
#' @export
#' @description This function plots the posterior distribution of the parameter
#' @param data Data for plotting
#' @param names Names of the algorithms
#' @param dataset Names of the dataset
plotPosterior <- function (data, names, dataset) {
   ggplot2::qplot(data$x, data$y, geom = "line") +
      ggplot2::ggtitle(paste(names[1], "vs.", names[2], "\nDataset:", dataset)) +
      ggplot2::xlab("Difference") +
      ggplot2::ylab("Value") +
      ggplot2::geom_area(ggplot2::aes(fill="distribution"), fill = "lightblue") +
      ggplot2::geom_line(color="darkblue") +
      ggplot2::geom_vline(xintercept = -0.01, color = "orange") +
      ggplot2::geom_vline(xintercept = 0.01, color = "orange") +
      ggplot2::geom_segment(data = data.frame(x = -0.01, y = 0, xend=0.01, yend = 0),
                            ggplot2::aes(x = -0.01, y = 0, xend=0.01, yend = 0), color = "orange")

}
