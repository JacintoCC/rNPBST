bst <- function(x, y, s = sqrt(2) - 1, c = 0.5, mc.samples = 1000){
    
    weights.dir <-  c(s, rep(1,length(y)))
    samples.dir <- MCMCpack::rdirichlet(mc.samples, weights.dir)[,-1]
    y.greater.x.indicator <- (sign(y - x) + 1) / 2
    
    g <- function(w){
      dist <- sum(y.greater.x.indicator * w)
      return(dist)
    }
    
    apply(samples.dir, 1, g)
}


bayesian.multiplecomparions <- function(x, gamma = 0.05){
  
  m <- ncol(x)
  n <- nrow(x)
  combinations <- combn(1:m,2)
  
  posterior <- apply(combinations, 2, function(c){
    bst <- bst(x[,c[1]],x[,c[2]])
    winner <- ifelse(mean(bst)>0.5,2,1)
    loser <- ifelse(mean(bst)>0.5,1,2)
    comp <- list()
    comp$X <- colnames(x)[c[loser]]
    comp$Y <- colnames(x)[c[winner]]
    comp$sample <- bst$sample[, winner]
    return(comp)
  }) 
  
  posterior <- lapply(posterior, unlist) %>% do.call(rbind, .) %>% as.data.frame()
  colnames(posterior)[3] <- "probability"
  posterior <- posterior %>% 
    arrange(desc(probability))
  
  multiple <- data.frame("m" = NULL, "X" = NULL, "Y"= NULL, "probability" = NULL)
  for(i in 1:nrow(posterior)){
    multiple.prob <- mean(apply(as.matrix(posterior[1:i, -(1:3)]), 2, function(x) all(x > 0.5)))
    if(multiple.prob > 1-gamma){
      multiple <- rbind(multiple, c(m=multiple.prob,posterior[i, 1:3]))
    }
  }
  return(list(posterior=posterior, multiple=multiple))
}
