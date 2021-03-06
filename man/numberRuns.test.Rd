% Generated by roxygen2: do not edit by hand
% Please edit documentation in R/NP-RandomnessTests.R
\name{numberRuns.test}
\alias{numberRuns.test}
\title{Number of runs test for randomness}
\usage{
numberRuns.test(sequence)
}
\arguments{
\item{sequence}{Sequence of data}
}
\value{
A list with pvalues for alternative hypothesis, statistics, method and data name
}
\description{
This function performs the Number of runs
}
\examples{
numberRuns.test(c("b","a","a","a","a","a","b","a","a","a"))
}
