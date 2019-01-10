#' Results of executions of CEC'17 algorithms
#'
#' A dataset containing the results of algorithms presented in the
#' CEC'17 competition.
#'
#' @format A data frame with 53244 rows and 17 variables:
#' \describe{
#'   \item{Algorithm}{Name of the algorithm. Value in \{DES, DYYPO, IDEbestNsize, jSO, LSHADE_SPACMA, MM_OED, PPSO, RB-IPOP-CMA-ES, TLBO-FL\}}
#'   \item{Benchmark}{Benchmark function, 1,3-30}
#'   \item{Dimension}{Dimension of the benchmark function: \{10,30,50,100\}}
#'   \item{Iteration}{Iteration of runs: \{1-51\}}
#'   \item{perc.completion.1}{Result at 1\% of iterations.}
#'   \item{perc.completion.2}{Result at 2\% of iterations.}
#'   \item{perc.completion.3}{Result at 3\% of iterations.}
#'   \item{perc.completion.5}{Result at 5\% of iterations.}
#'   \item{perc.completion.10}{Result at 10\% of iterations.}
#'   \item{perc.completion.20}{Result at 20\% of iterations.}
#'   \item{perc.completion.30}{Result at 30\% of iterations.}
#'   \item{perc.completion.40}{Result at 40\% of iterations.}
#'   \item{perc.completion.50}{Result at 50\% of iterations.}
#'   \item{perc.completion.60}{Result at 60\% of iterations.}
#'   \item{perc.completion.70}{Result at 70\% of iterations.}
#'   \item{perc.completion.80}{Result at 80\% of iterations.}
#'   \item{perc.completion.90}{Result at 90\% of iterations.}
#'   \item{perc.completion.100}{Result at 100\% of iterations.}
#' }
#'
"cec17.extended"

#' Mean of results between executions of CEC'17 algorithms
#'
#' A dataset containing the mean of the results of algorithms presented in the
#' CEC'17 competition between iterations.
#'
#' @format A data frame with 1044 rows and 17 variables:
#' \describe{
#'   \item{Algorithm}{Name of the algorithm. Value in \{DES, DYYPO, IDEbestNsize, jSO, LSHADE_SPACMA, MM_OED, PPSO, RB-IPOP-CMA-ES, TLBO-FL\}}
#'   \item{Benchmark}{Benchmark function, 1,3-30}
#'   \item{Dimension}{Dimension of the benchmark function: \{10,30,50,100\}}
#'   \item{perc.completion.1}{Result at 1\% of iterations.}
#'   \item{perc.completion.2}{Result at 2\% of iterations.}
#'   \item{perc.completion.3}{Result at 3\% of iterations.}
#'   \item{perc.completion.5}{Result at 5\% of iterations.}
#'   \item{perc.completion.10}{Result at 10\% of iterations.}
#'   \item{perc.completion.20}{Result at 20\% of iterations.}
#'   \item{perc.completion.30}{Result at 30\% of iterations.}
#'   \item{perc.completion.40}{Result at 40\% of iterations.}
#'   \item{perc.completion.50}{Result at 50\% of iterations.}
#'   \item{perc.completion.60}{Result at 60\% of iterations.}
#'   \item{perc.completion.70}{Result at 70\% of iterations.}
#'   \item{perc.completion.80}{Result at 80\% of iterations.}
#'   \item{perc.completion.90}{Result at 90\% of iterations.}
#'   \item{perc.completion.100}{Result at 100\% of iterations.}
#' }
#'
"cec17.mean"

#' Final results of CEC'17 algorithms
#'
#' A dataset containing the final results of algorithms
#' presented in the CEC'17 competition.

#'
#' @format A data frame with 53244 rows and 4 variables:
#' \describe{
#'   \item{Algorithm}{Name of the algorithm. Value in \{DES, DYYPO, IDEbestNsize, jSO, LSHADE_SPACMA, MM_OED, PPSO, RB-IPOP-CMA-ES, TLBO-FL\}}
#'   \item{Benchmark}{Benchmark function, 1,3-30}
#'   \item{Dimension}{Dimension of the benchmark function: \{10,30,50,100\}}
#'   \item{Iteration}{Iteration of runs: \{1-51\}}
#'   \item{Result}{Result at 100\% of iterations.}
#' }
"cec17.extended.final"


#' Final mean results of CEC'17 algorithms
#'
#' A dataset containing the final results of algorithms
#' presented in the CEC'17 competition.
#'
#' @format A data frame with 1276 rows and 4 variables:
#' \describe{
#'   \item{Algorithm}{Name of the algorithm. Value in \{DES, DYYPO, IDEbestNsize, jSO, LSHADE_SPACMA, MM_OED, PPSO, RB-IPOP-CMA-ES, TLBO-FL, LSHADE-CNEPSIN, EBO-CMAR\}}
#'   \item{Benchmark}{Benchmark function, 1,3-30}
#'   \item{Dimension}{Dimension of the benchmark function: \{10,30,50,100\}}
#'   \item{Result}{Result at 100\% of iterations.}
#' }
#'
"cec17.final"

