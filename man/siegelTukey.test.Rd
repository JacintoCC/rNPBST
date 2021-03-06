% Generated by roxygen2: do not edit by hand
% Please edit documentation in R/NP-ScaleTests.R
\name{siegelTukey.test}
\alias{siegelTukey.test}
\title{Siegel-Tukey test for scale}
\usage{
siegelTukey.test(matrix)
}
\arguments{
\item{matrix}{Matrix of data}
}
\value{
A list with pvalues for alternative hypothesis, statistics, method and data name
}
\description{
This function performs the Siegel Tukey test
}
\examples{
x <- cbind(rnorm(10, sd=1), rnorm(10, sd=3))
siegelTukey.test(x)
}
