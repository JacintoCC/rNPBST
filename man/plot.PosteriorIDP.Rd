% Generated by roxygen2: do not edit by hand
% Please edit documentation in R/plot-PosteriorIDP.R
\name{plot.PosteriorIDP}
\alias{plot.PosteriorIDP}
\title{Plot of posterior distributions of IDP}
\usage{
\method{plot}{PosteriorIDP}(x, ...)
}
\arguments{
\item{x}{Imprecise Dirichlet Proccess Posterior distribution}

\item{...}{Extra parameters for plot}
}
\description{
This function plots the posterior distribution of the parameter
}
\examples{
idp <- bayesian.imprecise(results.rf[1, ], results.knn[1, ])
plot(idp)
}
