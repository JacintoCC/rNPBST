% Generated by roxygen2: do not edit by hand
% Please edit documentation in R/NP-EqualityTests.R
\name{friedmanAR.test}
\alias{friedmanAR.test}
\title{Friedman Aligned-Rank}
\usage{
friedmanAR.test(matrix)
}
\arguments{
\item{matrix}{Matrix of data}
}
\value{
A list with pvalues for alternative hypothesis, statistics, method and data name
}
\description{
This function performs the Friedman Aligned-Rank test
}
\examples{
friedmanAR.test(results)
}
