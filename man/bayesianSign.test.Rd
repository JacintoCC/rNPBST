% Generated by roxygen2: do not edit by hand
% Please edit documentation in R/Bayesian-Sign.R
\name{bayesianSign.test}
\alias{bayesianSign.test}
\title{Bayesian Sign test}
\usage{
bayesianSign.test(
  x,
  y = NULL,
  s = 1,
  z_0 = 0,
  rope.min = -0.01,
  rope.max = 0.01,
  weights = c(s/2, rep(1, length(x))),
  n.samples = 1e+05
)
}
\arguments{
\item{x}{First vector of observations}

\item{y}{Second vector of observations}

\item{s}{Prior pseudo-observation probabilitie}

\item{z_0}{Prior pseudo-observation}

\item{rope.min}{Inferior limit of the rope considered}

\item{rope.max}{Superior limit of the rope considered}

\item{weights}{A priori weights}

\item{n.samples}{Number of samples of the distributio}
}
\value{
List with probabilities for each region and a sample of
posterior distribution.
}
\description{
This function performs the Bayesian Sign test
}
\examples{
bs <- bayesianSign.test(results$random.forest, results$KNN)
bs.stronger.prior <- bayesianSign.test(results$random.forest, results$KNN, s=3, z_0 = 0.5)
}
