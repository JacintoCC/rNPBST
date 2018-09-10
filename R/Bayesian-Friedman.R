#' @title Bayesian Friedman Test
#'
#' @export
#' @description Performs the Bayesian Friedman Test
#' @param dataset Matrix observations with treatments by columns and observations by rows
#' @param s Prior strength. Default 1
#' @param gamma Posterior probability level. Default 0.05
#' @param ties Method for ties. Default "average"
#' @param n.samples Number of samples. Default 10000
#' @param imprecise Imprecise method of calculation. Default FALSE
#' @return List with the probabilites of each possible dominance
#'     configuration.
bayesianFriedman.test <- function(dataset, s = 1, gamma = 0.05,
                                  ties = "average", n.samples = 10000, imprecise = FALSE){
   n <- nrow(dataset)
   m <- ncol(dataset)
   
   # Threshold for the F distribution
   rho <- stats::pf(1 - gamma, m - 1, n - m + 1) * (n - 1) * (m - 1) / (n - m + 1)
   
   # Compute the ranks matrix
   R <- apply(dataset, 1, rank, ties = ties)
   
   # Prior R0
   ones <- matrix(1, nrow = n)
   R.0 <- matrix((m+1) / 2, nrow = m)
   
   # R.mean
   R.mean <- as.vector((rowSums(R) + s*R.0) / (s+n))
   names(R.mean) <- colnames(R)
   
   # Weights from Dirichlet distribution
   weights.dir <- c(s, rep(1, n))
   w <- MCMCpack::rdirichlet(n.samples, weights.dir)
   
   if(imprecise){
      CovWl <- (matrix(1,ncol = n, nrow = n) + diag(n)) / ((s+n) * (s+n+1))
      Sigma <- s * (R.0 %*% t(R.0) * (s+1) + R.0 %*% matrix(1, ncol = n) %*% t(R) + rowSums(R) %*% t(R.0)) /
         ((s+n)*(s+n+1)) + R %*% CovWl %*% t(R) - R.mean %*% t(R.mean)
      eigen.values <- eigen(Sigma)$values
      lambda <- diag(eigen.values)
      indet.values <- which(abs(eigen.values) > 10^-15)
      length.indet <- length(indet.values)
      
      
      if(length.indet == m){
         indet.values = indet.values[-length.indet]
         length.indet <- length.indet - 1
      }
      
            
      if(length.indet == 0){
         h <- ifelse(all(R.mean == R.0), 0, 1)
      }
      else{
         matrix.A <- R.mean[indet.values, ] - matrix(R.0[1], nrow = length.indet)
         matrix.B <- Sigma[indet.values, indet.values]
         matrix.C <- pracma::mldivide(matrix.B, matrix.A)

         h <- ifelse(t(matrix.A) %*% matrix.C < rho, 0, 1)
      }
      
      return(list(h = h, meanranks = R.mean, covariance = Sigma))
   }
   else{
      Pe <- pracma::perms(1:m)
      imprecise <- apply(Pe, 1, function(pe){
         R.0 <- pe
         CovWl <- (matrix(1,ncol = n, nrow = n) + diag(n)) / ((s+n) * (s+n+1))
         Sigma <- s * (R.0 %*% t(R.0) * (s+1) + R.0 %*% matrix(1, ncol = n) %*% t(R) + rowSums(R) %*% t(R.0)) /
            ((s+n)*(s+n+1)) + R %*% CovWl %*% t(R) - R.mean %*% t(R.mean)
         eigen.values <- eigen(Sigma)$values
         lambda <- diag(eigen.values)
         indet.values <- which(abs(eigen.values) > 10^-15)
         length.indet <- length(indet.values)
         
         if(length.indet == m){
            indet.values <- indet.values[-length.indet]
            length.indet <- length.indet - 1
         }
         
         if(length.indet == 0){
            h <- ifelse(all(R.mean == R.0), 0, 1)
         }
         else{
            matrix.A <- R.mean[indet.values] - matrix(R.0[1], nrow = length.indet)
            matrix.B <- Sigma[indet.values, indet.values]
            matrix.C <- pracma::mldivide(matrix.B, matrix.A)
            
            h <- ifelse(t(matrix.A) %*% matrix.C < rho, 0, 1)
         }
         
         return(h)
      })
      
      unique.h <- unique(imprecise)
      h <- ifelse(length(unique.h) > 1, 2, unique.h)
      
      return(list(h = h,  meanranks = R.mean, imprecise = imprecise))
   }
}

