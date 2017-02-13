#' @title Execution of a StatisticalTest object
#'
#' @export
#' @description Wrapper to run a StatisticalTest object.
#' @param java.test.object A StatisticalTest Java object
#' @return The function returns the report of the test in a string
runTest <- function(java.test.object){
   # Run test
   rJava::.jcall(java.test.object, "V", "doTest")
   out <- rJava::.jcall(java.test.object, "S", "printReport")
   return(out)
}

#' @title Make a htest object
#'
#' @export
#' @description This function takes the arguments, makes a list and assign the list the htest class
#' @param ... Values to form the htest object
#' @return htest object
make.htest <- function(...){
   htest <- list(...)
   class(htest) <- "htest"
   return(htest)
}

#' @title Contingency Coefficient test for count data
#'
#' @export
#' @description This function performs the Contingency Coefficient test
#' @param matrix Matrix of data
#' @return A htest object with pvalues and statistics
contingency.coeff.test <- function(matrix){
   java.test.object <- rJava::.jnew("javanpst.tests.countData.contingencyCoefficient.ContingencyCoefficient",
                                    dataTable(matrix))
   rJava::.jcall(java.test.object, "V", "doTest")
   q <- rJava::.jcall(java.test.object, "D", "getQ")
   c <- rJava::.jcall(java.test.object, "D", "getC")
   phi <- rJava::.jcall(java.test.object, "D", "getPhi")
   estimate <- c("C contingency coefficient" = c,
                 "Phi contingency coefficient" = phi)
   pvalue <- rJava::.jcall(java.test.object, "D", "getPValue")
   htest <- make.htest(data.name = deparse(substitute(matrix)),
                       statistic = c(Q = q), p.value = pvalue,
                       estimate = estimate,
                       method = "contingency coeff")
   return(htest)
}

#' @title Multinomial equality test for count data
#'
#' @export
#' @description This function performs the Multinomial equality test
#' @param matrix Matrix of data
#' @return A htest object with pvalues and statistics
multinomialEq.test <- function(matrix){
   java.test.object <- rJava::.jnew("javanpst.tests.countData.multinomialEqualityTest.MultinomialEqualityTest",
                                    dataTable(matrix))
   rJava::.jcall(java.test.object, "V", "doTest")
   q <- rJava::.jcall(java.test.object, "D", "getQ")
   pvalue <- rJava::.jcall(java.test.object, "D", "getPValue")
   htest <- make.htest(data.name = deparse(substitute(matrix)),
                       statistic = q, p.value = pvalue,
                       method = "multinomial equality")
   return(htest)
}

#' @title Ordered equality test for count data
#'
#' @export
#' @description This function performs the Ordered equality test
#' @param matrix Matrix of data
#' @return A htest object with pvalues and statistics
orderedEq.test <- function(matrix){
   java.test.object <- rJava::.jnew("javanpst.tests.countData.orderedEqualityTest.OrderedEqualityTest",
                                    dataTable(matrix))
   rJava::.jcall(java.test.object, "V", "doTest")
   statistic <- c(Wx = rJava::.jcall(java.test.object, "D", "getWx"),
                  Wy = rJava::.jcall(java.test.object, "D", "getWy"))
   pvalue <- c(right = rJava::.jcall(java.test.object, "D", "getRightPValue"),
               left = rJava::.jcall(java.test.object, "D", "getLeftPValue"),
               double = rJava::.jcall(java.test.object, "D", "getDoublePValue"))
   htest <- make.htest(data.name = deparse(substitute(matrix)),
                       statistic = statistic, p.value = pvalue,
                       method = "ordered equality")
   return(htest)
}


#' @title Extended Median test for equality
#'
#' @export
#' @description This function performs the extended median test
#' @param matrix Matrix of data
#' @return A htest object with pvalues and statistics
extendedMedian.test <- function(matrix){
   java.test.object <- rJava::.jnew("javanpst.tests.equality.extendedMedianTest.ExtendedMedianTest",
                                    dataTable(matrix))
   rJava::.jcall(java.test.object, "V", "doTest")
   statistic <- c("Median" = rJava::.jcall(java.test.object, "D", "getMedian"),
                  "Q" = rJava::.jcall(java.test.object, "D", "getQ"),
                  "Improved Q" = rJava::.jcall(java.test.object, "D", "getImprovedQ"))
   pvalue <- rJava::.jcall(java.test.object, "D", "getPValue")
   htest <- make.htest(data.name = deparse(substitute(matrix)),
                       statistic = statistic, p.value = pvalue,
                       method = "Exteded median")
   return(htest)
}

#' @title Jonckheere and Terpstra test for equality
#'
#' @export
#' @description This function performs the Jonckheere and Terpstra test
#' @param matrix Matrix of data
#' @return A htest object with pvalues and statistics
jt.test <- function(matrix){
   java.test.object <- rJava::.jnew("javanpst.tests.equality.JTTest.JTTest",
                                    dataTable(matrix))
   rJava::.jcall(java.test.object, "V", "doTest")
   statistic <- c(b = rJava::.jcall(java.test.object, "D", "getB"),
                  z = rJava::.jcall(java.test.object, "D", "getZ"))
   pvalue <- rJava::.jcall(java.test.object, "D", "getPValue")
   htest <- make.htest(data.name = deparse(substitute(matrix)),
                       statistic = statistic, p.value = pvalue,
                       method = "JT")
   return(htest)
}

#' @title Kruskal-Wallis est for equality
#'
#' @export
#' @description This function performs the Kruskal-Wallis test
#' @param matrix Matrix of data
#' @param print.multiple If True, prints
#' @return A htest object with pvalues and statistics
kruskalWallis.test <- function(matrix, print.multiple = F){
   java.test.object <- rJava::.jnew("javanpst.tests.equality.kruskalWallisTest.KruskalWallisTest",
                                    dataTable(matrix))
   rJava::.jcall(java.test.object, "V", "doTest")

   H <- rJava::.jcall(java.test.object, "D", "getH")
   pvalue <- rJava::.jcall(java.test.object, "D", "getPValue")
   if(print.multiple)
      cat(rJava::.jcall(java.test.object, "S", "printMultipleComparisonsProcedureReport"))

   htest <- make.htest(data.name = deparse(substitute(matrix)),
                       statistic = c("H" = H), p.value = pvalue,
                       method = "Kruskal Wallis")
   return(htest)
}

#' @title Anderson-Darling test for goodness of fit
#'
#' @export
#' @description This function performs the Anderson-Darling test
#' @param sequence Sequence of data
#' @param distribution Distribution name to perform test
#' @param mean Distribution mean - Use if mean is a parameter of the distribution
#' @param sd Distribution sd - Use if mean is a parameter of the distribution
#' @param start Distribution start - Use if mean is a parameter of the distribution
#' @param end Distribution end - Use if mean is a parameter of the distribution
#' @param freedom Distribution freedom - Use if mean is a parameter of the distribution
#' @param k Distribution k - Use if mean is a parameter of the distribution
#' @param lambda Distribution lambda - Use if mean is a parameter of the distribution
#' @param scale Distribution scale - Use if mean is a parameter of the distribution
#' @param s Distribution s - Use if mean is a parameter of the distribution
#' @return A htest object with pvalues and statistics
ad.test <- function(sequence, distribution = "NORMAL", mean = NULL, sd = NULL,
                    start = NULL, end = NULL, freedom = NULL, k = NULL,
                    lambda = NULL, scale = NULL, s = NULL){
   java.test.object <- rJava::.jnew("javanpst.tests.goodness.A_DTest.A_DTest",
                                    numericSequence(sequence))

   switch(distribution,
          NORMAL = {
             if(is.null(mean) & is.null(sd))
                rJava::.jcall(java.test.object, "V", "adjustNormal")
             else if(is.null(sd))
                rJava::.jcall(java.test.object, "V", "adjustNormalMean", mean)
             else if(is.null(mean))
                rJava::.jcall(java.test.object, "V", "adjustNormalVariance", sd)
             else if(is.null(mean))
                rJava::.jcall(java.test.object, "V", "adjustNormal", mean, sd)
          },
          EXPONENTIAL = {
             if(is.null(mean))
                rJava::.jcall(java.test.object, "V", "adjustExponential")
             else
                rJava::.jcall(java.test.object, "V", "adjustExponential", mean)
          },
          UNIFORM = {
             rJava::.jcall(java.test.object, "V", "adjustUniform", start, end)
          },
          CHI_SQUARE = {
             rJava::.jcall(java.test.object, "V", "adjustChiSquare", as.integer(freedom))
          },
          GAMMA = {
             rJava::.jcall(java.test.object, "V", "adjustGamma", k, lambda)
          },
          LAPLACE = {
             rJava::.jcall(java.test.object, "V", "adjustLaplace", mean, scale)
          },
          LOGISTIC= {
             rJava::.jcall(java.test.object, "V", "adjustLogistic", mean, s)
          },
          WEIBULL = {
             rJava::.jcall(java.test.object, "V", "adjustWeibull", k, lambda)
          },
          stop("Not supported distribution in this test")
   )

   rJava::.jcall(java.test.object, "V", "doTest")
   w2 <- rJava::.jcall(java.test.object, "D", "W2")
   a <- rJava::.jcall(java.test.object, "D", "getA")
   pvalue <- rJava::.jcall(java.test.object, "D", "getPValue")
   htest <- make.htest(data.name = deparse(substitute(matrix)),
                       statistic = c("A" = a, "W2" = w2),
                       parameters = c("Mean" = mean, "Var" = sd,
                                      "Start" = start, "End" = end,
                                      "Freedom" = freedom, "K" = k,
                                      "Lambda" = lambda, "Scale" = scale,
                                      "S" = s),
                       p.value = c("Exact pvalue <= " = pvalue),
                       method = "AD")
   return(htest)
}

#' @title Chi square test for goodness of fit
#'
#' @export
#' @description This function performs the Chi square test
#' @param matrix Matrix of data
#' @param n N parameter for Uniform distribution
#' @param p P parameter for uniform distribution
#' @return A htest object with pvalues and statistics
chiSquare.test <- function(matrix, n, p = NULL){
   java.test.object <- rJava::.jnew("javanpst.tests.goodness.chiSquareTest.ChiSquareTest",
                                    dataTable(matrix))
   if(is.null(p))
      rJava::.jcall(java.test.object, "V", "adjustBinomial",as.integer(n))
   else{
      p = (matrix[ ,2] %*% matrix[,1])/ (n*sum(matrix[ ,2]))
      rJava::.jcall(java.test.object, "V", "adjustBinomial",as.integer(n), p)
   }

   rJava::.jcall(java.test.object, "V", "doTest")
   q <- rJava::.jcall(java.test.object, "D", "getQ")
   pvalue <- rJava::.jcall(java.test.object, "D", "getPValue")
   htest <- make.htest(data.name = deparse(substitute(matrix)),
                       statistic = c("Q" = q), p.value = pvalue,
                       method = "chi square",
                       parameters = c("N" = n),
                       estimate = c("P" = p))
   return(htest)
}


#' @title Normal scores test for location
#'
#' @export
#' @description This function performs the normal scores test
#' @param matrix Matrix of data
#' @return A htest object with pvalues and statistics
normalScores.test <- function(matrix){
   java.test.object <- rJava::.jnew("javanpst.tests.location.normalScoresTest.NormalScoresTest",
                                    dataTable(matrix))
   rJava::.jcall(java.test.object, "V", "doTest")
   statistic <- c(normalStatistic1 = rJava::.jcall(java.test.object, "D", "getNormalStatistic1"),
                  normalStatistic2 = rJava::.jcall(java.test.object, "D", "getNormalStatistic2"))
   pvalue <- c(left = rJava::.jcall(java.test.object, "D", "getLeftPValue"),
               right = rJava::.jcall(java.test.object, "D", "getRightPValue"),
               double = rJava::.jcall(java.test.object, "D", "getDoublePValue"))
   htest <- make.htest(data.name = deparse(substitute(matrix)),
                       statistic = statistic, p.value = pvalue,
                       method = "normal scores")
   return(htest)
}


#' @title Concordance Coefficient test for multiple comparisons
#'
#' @export
#' @description This function performs the Concordance Coefficient test
#' @param matrix Matrix of data
#' @return A htest object with pvalues and statistics
concordanceCoeff.test <- function(matrix){
   java.test.object <- rJava::.jnew("javanpst.tests.multiple.concordanceCoefficient.ConcordanceCoefficient",
                                    dataTable(matrix))
   rJava::.jcall(java.test.object, "V", "doTest")
   statistic <- c(s = rJava::.jcall(java.test.object, "D", "getS"),
                  q = rJava::.jcall(java.test.object, "D", "getQ"),
                  w = rJava::.jcall(java.test.object, "D", "getW"))
   pvalue <- rJava::.jcall(java.test.object, "D", "getPValue")
   htest <- make.htest(data.name = deparse(substitute(matrix)),
                       statistic = statistic, p.value = pvalue,
                       method = "concordance coeff")
   return(htest)
}

#' @title Friedman test for multiple comparisons
#'
#' @export
#' @description This function performs the Friedman test
#' @param matrix Matrix of data
#' @return A htest object with pvalues and statistics
friedman.test <- function(matrix){
   java.test.object <- rJava::.jnew("javanpst.tests.multiple.friedmanTest.FriedmanTest",
                                    dataTable(matrix))
   rJava::.jcall(java.test.object, "V", "doTest")
   statistic <- c(s = rJava::.jcall(java.test.object, "D", "getS"),
                  q = rJava::.jcall(java.test.object, "D", "getQ"))
   pvalue <- rJava::.jcall(java.test.object, "D", "getPValue")
   htest <- make.htest(data.name = deparse(substitute(matrix)),
                       statistic = statistic, p.value = pvalue,
                       method = "Friedman test")
   return(htest)
}

#' @title Incomplete Concordance test for multiple comparisons
#'
#' @export
#' @description This function performs the Incomplete Concordance test
#' @param matrix Matrix of data
#' @param lambda Parameter of the test
#' @return A htest object with pvalues and statistics
incompleteConcordance.test <- function(matrix, lambda){
   java.test.object <- rJava::.jnew("javanpst.tests.multiple.incompleteConcordance.IncompleteConcordance",
                                    dataTable(matrix), lambda)
   rJava::.jcall(java.test.object, "V", "doTest")
   statistic <- c(q = rJava::.jcall(java.test.object, "D", "getQ"),
                  w = rJava::.jcall(java.test.object, "D", "getW"))
   pvalue <- rJava::.jcall(java.test.object, "D", "getPValue")
   htest <- make.htest(data.name = deparse(substitute(matrix)),
                       statistic = statistic, p.value = pvalue,
                       method = "incomplete concordance")
   return(htest)
}

#' @title Confidence Quantile for one sample
#'
#' @export
#' @description This function performs the confidence quantile test
#' @param n Parameter of the test
#' @param p Parameter of the test
#' @param q Parameter of the test
#' @return Report of the test in a string
confidenceQuantile.test <- function(n, p, q){
   java.test.object <- rJava::.jnew("javanpst.tests.oneSample.confidenceQuantile.ConfidenceQuantile",
                                    as.integer(n), p, q)
   report <- runTest(java.test.object)
   cat(report)
}

#' @title Population quantile for one sample
#'
#' @export
#' @description This function performs the confidence quantile test
#' @param sequence  Parameter of the test
#' @param quantile Parameter of the test
#' @param value Parameter of the test
#' @return Report of the test in a string
populationQuantile.test <- function(sequence, quantile, value){
   java.test.object <- rJava::.jnew("javanpst.tests.oneSample.populationQuantile.PopulationQuantile",
                                    numericSequence(sequence), quantile, value)
   rJava::.jcall(java.test.object, "V", "doTest")
   k <- rJava::.jcall(java.test.object, "D", "getK")
   pvalue <- c("Exact Left" = rJava::.jcall(java.test.object, "D", "getExactLeftPValue"),
               "Exact Right" = rJava::.jcall(java.test.object, "D", "getExactRightPValue"),
               "Exact Double" = rJava::.jcall(java.test.object, "D", "getExactDoublePValue"))
   htest <- make.htest(data.name = deparse(substitute(sequence)),
                       statistic = c("K" = k), p.value = pvalue,
                       method = "population quantile")
   return(htest)
}

#' @title Sign test for one sample
#'
#' @export
#' @description This function performs the Sign test
#' @param matrix Sequence of data
#' @return A htest object with pvalues and statistics
binomialSign.test <- function(matrix){
   if(length(dim(matrix)) == 1 || is.vector(matrix)){
      java.test.object <- rJava::.jnew("javanpst.tests.oneSample.signTest.SignTest",
                                       numericSequence(matrix))
      method <- "Binomial Sign test for One Sample"
   }
   else{
      java.test.object <- rJava::.jnew("javanpst.tests.oneSample.signTest.SignTest",
                                       dataTable(matrix))
      method <- "Binomial Sign test"
   }

   rJava::.jcall(java.test.object, "V", "doTest")
   statistic <- c("K" = rJava::.jcall(java.test.object, "D", "getK"),
                  "K2" = rJava::.jcall(java.test.object, "D", "getK2"))
   pvalue <- c("Exact P-Value (Left tail, Y > X)" = rJava::.jcall(java.test.object, "D", "getExactLeftPValue"),
               "Exact P-Value (Right tail, Y < X)" = rJava::.jcall(java.test.object, "D", "getExactRightPValue"),
               "Exact P-Value (Double tail, Y != X)" = rJava::.jcall(java.test.object, "D", "getExactDoublePValue"),
               "Asymptotic P-Value (Left tail, Y > X)" = rJava::.jcall(java.test.object, "D", "getLeftPValue"),
               "Asymptotic P-Value (Right tail, Y < X)" = rJava::.jcall(java.test.object, "D", "getRightPValue"),
               "Asymptotic P-Value (Double tail, Y != X)" = rJava::.jcall(java.test.object, "D", "getDoublePValue"))
   htest <- make.htest(data.name = deparse(substitute(matrix)),
                       statistic = statistic, p.value = pvalue,
                       method = method)
   return(htest)
}

#' @title David Barton test for scale
#'
#' @export
#' @description This function performs the David Barton test
#' @param matrix Matrix of data
#' @return A htest object with pvalues and statistics
davidBarton.test <- function(matrix){
   java.test.object <- rJava::.jnew("javanpst.tests.scale.david_BartonTest.David_BartonTest",
                                    dataTable(matrix))
   rJava::.jcall(java.test.object, "V", "doTest")
   statistic <- c(MNx = rJava::.jcall(java.test.object, "D", "getTestStatistic1"),
                  MNy = rJava::.jcall(java.test.object, "D", "getTestStatistic2"))
   pvalue <- c(asymptotic.left = rJava::.jcall(java.test.object, "D", "getLeftPValue"),
               asymptotic.right = rJava::.jcall(java.test.object, "D", "getRightPValue"),
               asymptotic.double = rJava::.jcall(java.test.object, "D", "getDoublePValue"))
   htest <- make.htest(data.name = deparse(substitute(matrix)),
                       statistic = statistic, p.value = pvalue,
                       method = "david barton")
   return(htest)
}

#' @title Freund Ansari Bradley test for scale
#'
#' @export
#' @description This function performs the Freund Ansari Bradley test
#' @param matrix Matrix of data
#' @return A htest object with pvalues and statistics
freundAnsariBradley.test <- function(matrix){
   java.test.object <- rJava::.jnew("javanpst.tests.scale.freund_Ansari_BradleyTest.Freund_Ansari_BradleyTest",
                                    dataTable(matrix))
   rJava::.jcall(java.test.object, "V", "doTest")
   statistic <- c(MNx = rJava::.jcall(java.test.object, "D", "getTestStatistic1"),
                  MNy = rJava::.jcall(java.test.object, "D", "getTestStatistic2"))
   pvalue <- c(asymptotic.left = rJava::.jcall(java.test.object, "D", "getLeftPValue"),
               asymptotic.right = rJava::.jcall(java.test.object, "D", "getRightPValue"),
               asymptotic.double = rJava::.jcall(java.test.object, "D", "getDoublePValue"))
   htest <- make.htest(data.name = deparse(substitute(matrix)),
                       statistic = statistic, p.value = pvalue,
                       method = "freund ansari bradley")
   return(htest)
}

#' @title Klotz test for scale
#'
#' @export
#' @description This function performs the Klotz test
#' @param matrix Matrix of data
#' @return A htest object with pvalues and statistics
klotz.test <- function(matrix){
   java.test.object <- rJava::.jnew("javanpst.tests.scale.klotzTest.KlotzTest",
                                    dataTable(matrix))
   rJava::.jcall(java.test.object, "V", "doTest")
   statistic <- c(MNx = rJava::.jcall(java.test.object, "D", "getTestStatistic1"),
                  MNy = rJava::.jcall(java.test.object, "D", "getTestStatistic2"))
   pvalue <- c(x.pvalue = rJava::.jcall(java.test.object, "D", "getPValue1"),
               y.pvalue = rJava::.jcall(java.test.object, "D", "getPValue2"))
   htest <- make.htest(data.name = deparse(substitute(matrix)),
                       statistic = statistic, p.value = pvalue,
                       method = "klotz")
   return(htest)
}

#' @title Mood test for scale
#'
#' @export
#' @description This function performs the Mood test
#' @param matrix Matrix of data
#' @return A htest object with pvalues and statistics
mood.test <- function(matrix){
   java.test.object <- rJava::.jnew("javanpst.tests.scale.moodTest.MoodTest",
                                    dataTable(matrix))
   rJava::.jcall(java.test.object, "V", "doTest")
   statistic <- c(MNx = rJava::.jcall(java.test.object, "D", "getTestStatistic1"),
                  MNy = rJava::.jcall(java.test.object, "D", "getTestStatistic2"))
   pvalue <- c(x.pvalue = rJava::.jcall(java.test.object, "D", "getPValue1"),
               y.pvalue = rJava::.jcall(java.test.object, "D", "getPValue2"))
   htest <- make.htest(data.name = deparse(substitute(matrix)),
                       statistic = statistic, p.value = pvalue,
                       method = "mood")
   return(htest)
}

#' @title Sukhatme test for scale
#'
#' @export
#' @description This function performs the Sukhatme test
#' @param matrix Matrix of data
#' @return A htest object with pvalues and statistics
sukhatme.test <- function(matrix){
   java.test.object <- rJava::.jnew("javanpst.tests.scale.sukhatmeTest.SukhatmeTest",
                                    dataTable(matrix))
   rJava::.jcall(java.test.object, "V", "doTest")
   sukhatme <- rJava::.jcall(java.test.object, "D", "getTestStatistic")
   pvalue <- c(asymptotic.left = rJava::.jcall(java.test.object, "D", "getLeftPValue"),
               asymptotic.right = rJava::.jcall(java.test.object, "D", "getRightPValue"),
               asymptotic.double = rJava::.jcall(java.test.object, "D", "getDoublePValue"))
   htest <- make.htest(data.name = deparse(substitute(matrix)),
                       statistic = c("Sukhatme statistic" = sukhatme),
                       p.value = pvalue,
                       method = "Sukhatme")
   return(htest)
}

#' @title Control Median test for two samples
#'
#' @export
#' @description This function performs the Control Median test
#' @param matrix Matrix of data
#' @return A htest object with pvalues and statistics
controlMedian.test <- function(matrix){
   java.test.object <- rJava::.jnew("javanpst.tests.twoSample.controlMedianTest.ControlMedianTest",
                                    dataTable(matrix))
   rJava::.jcall(java.test.object, "V", "doTest")
   statistic <- c(u = rJava::.jcall(java.test.object, "D", "getU"),
                  v = rJava::.jcall(java.test.object, "D", "getV"),
                  median = rJava::.jcall(java.test.object, "D", "getMedian"),
                  median2 = rJava::.jcall(java.test.object, "D", "getMedian2"))
   pvalue <- c(exact.left = rJava::.jcall(java.test.object, "D", "getExactLeftPValue"),
               exact.right = rJava::.jcall(java.test.object, "D", "getExactRightPValue"),
               exact.double = rJava::.jcall(java.test.object, "D", "getExactDoublePValue"),
               asymptotic.left = rJava::.jcall(java.test.object, "D", "getLeftPValue"),
               asymptotic.right = rJava::.jcall(java.test.object, "D", "getRightPValue"),
               asymptotic.double = rJava::.jcall(java.test.object, "D", "getDoublePValue"))
   htest <- make.htest(data.name = deparse(substitute(matrix)),
                       statistic = statistic, p.value = pvalue,
                       method = "control median")
   return(htest)
}

#' @title Median test for two samples
#'
#' @export
#' @description This function performs the Median test
#' @param matrix Matrix of data
#' @return A htest object with pvalues and statistics
twoSamplesMedian.test <- function(matrix){
   java.test.object <- rJava::.jnew("javanpst.tests.twoSample.medianTest.MedianTest",
                                    dataTable(matrix))
   rJava::.jcall(java.test.object, "V", "doTest")
   statistic <- c(u = rJava::.jcall(java.test.object, "D", "getU"),
                  v = rJava::.jcall(java.test.object, "D", "getV"),
                  median = rJava::.jcall(java.test.object, "D", "getMedian"))
   pvalue <- c(exact.left = rJava::.jcall(java.test.object, "D", "getExactLeftPValue"),
               exact.right = rJava::.jcall(java.test.object, "D", "getExactRightPValue"),
               exact.double = rJava::.jcall(java.test.object, "D", "getExactDoublePValue"),
               asymptotic.left = rJava::.jcall(java.test.object, "D", "getLeftPValue"),
               asymptotic.right = rJava::.jcall(java.test.object, "D", "getRightPValue"),
               asymptotic.double = rJava::.jcall(java.test.object, "D", "getDoublePValue"))
   htest <- make.htest(data.name = deparse(substitute(matrix)),
                       statistic = statistic, p.value = pvalue,
                       method = "Two Samples Median test")
   return(htest)
}
