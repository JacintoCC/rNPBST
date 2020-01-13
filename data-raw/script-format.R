library(dplyr)

#' @title readFileResults
#'
#'
#' @description Read csv files and make complete data.frame
#' @param file.information Grid with parameters from file names
#' @param sep Character of separation for csv files
#' @return Data.frame with
readGridFileResults <- function(file.information, sep = "_"){
  # Format file name from matrix
  file.name <- paste("data-raw/",
                     gsub(" ", "", paste(file.information, collapse = sep)),
                     ".txt", sep = "")

  if(any(file.information[1] == c("MOS-SOCO2011","MOS-CEC2012","MOS-CEC2013"))){
    file.name <- gsub("txt","csv",file.name)
    file.data <- t(utils::read.csv(file.name, header = T)[ ,-1])
  }
  else if(file.information[1] == "TLBO-FL"){
    file.data <- utils::read.csv(file.name, header = T)
  }
  else{
    # Read data frame for the file and append cols with file information
     file.data <- t(utils::read.csv(file.name, header = F, sep = ","))
  }

  if(ncol(file.data) != 14 || nrow(file.data) != 51 || anyNA(file.data)){
     file.data <- t(utils::read.csv(file.name, header = F, sep = ","))
  }
  if(ncol(file.data) != 14 || nrow(file.data) != 51 || anyNA(file.data)){
     file.data <- t(utils::read.csv(file.name, header = F, sep = " "))
  }
  if(ncol(file.data) != 14 || nrow(file.data) != 51 || anyNA(file.data)){
    file.data <- t(utils::read.csv(file.name, header = F, sep = "\t"))
  }



  file.data <- data.frame(file.information[1],
                          file.information[2],
                          file.information[3],
                          file.data,
                          row.names = NULL)

  return(file.data)
}

# Information from the raw data files
algorithms <- c("DES","DYYPO", "EBOwithCMAR", "IDEbestNsize", "jSO",
                "LSHADE_SPACMA", "MM_OED", "PPSO", "RB-IPOP-CMA-ES", "TLBO-FL",
                "MOS-SOCO2011","MOS-CEC2012", "MOS-CEC2013")
benchmark <- c(1,3:30)
dimensionality <- c(10,30,50,100)

# Grid with file names
file_names <- expand.grid(algorithms,
                          benchmark,
                          dimensionality,
                          stringsAsFactors = F)

# Read all files and concatenate results data frames
cec17.extended <- apply(file_names, 1,  readGridFileResults) %>% do.call(rbind, .)
colnames(cec17.extended) <- c("Algorithm", "Benchmark", "Dimension",
                              paste("perc.completion", c(1,2,3,5,seq(10, 100, by = 10)), sep = "."))

levels(cec17.extended$Algorithm) <- c("DES","DYYPO", "EBO", "IDEN", "jSO",
                                      "LSSPA","MM","PPSO", "RBI","TFL",
                                      "MOS11","MOS12","MOS13")


cec17.extended <- transform(cec17.extended,
                            Algorithm = as.factor(trimws(Algorithm)),
                            Benchmark = as.factor(trimws(Benchmark)),
                            Dimension = factor(trimws(Dimension), levels = trimws(levels(Dimension)))) %>%
   mutate_at(vars(starts_with("perc.")), as.numeric)
cec17.extended$Iteration <- rep(1:51, 116)
cec17.extended <- as.data.frame(cec17.extended)

# Average between iterations
cec17.mean <- select(cec17.extended, -Iteration) %>%
   group_by(Algorithm, Benchmark, Dimension) %>%
   summarise_all(mean, na.rm=T) %>%
   as.data.frame()

# Results at final steps of execution
cec17.extended.final <- dplyr::select(cec17.extended,
                                      c(Algorithm, Benchmark,Dimension,Iteration,perc.completion.100))
colnames(cec17.extended.final)[5] <- "Result"
cec17.extended.final <- as.data.frame(cec17.extended.final)

## Aggregation of aditional algorithms

### LSHADE-CNEPSIN
algorithm.name <- "LSHADE-CNEPSIN"
algorithm.key <- "LSCNE"
lshade.cnepsin <- lapply(dimensionality, function(d){
   file.name <- paste("data-raw/",
                      paste(algorithm.name, d, sep = "_"),
                      ".txt", sep = "")
   data.mean <- read.delim(file.name, header = F, sep = "")[benchmark, 4]

   df <- data.frame(Algorithm = rep(algorithm.key,29),
                    Benchmark = benchmark,
                    Dimension = rep(d, 29),
                    Result = as.numeric(data.mean))
   return(df)
})

lshade.cnepsin <- do.call(rbind, lshade.cnepsin)
lshade.cnepsin <- transform(lshade.cnepsin,
                            Algorithm = as.factor(Algorithm),
                            Benchmark = as.factor(benchmark),
                            Dimension = as.factor(rep(dimensionality, each = 29)))

# Mean results
cec17.final <- dplyr::select(cec17.mean,
                             c(Algorithm, Benchmark,Dimension,perc.completion.100))
colnames(cec17.final)[4] <- "Result"
cec17.final <- rbind(cec17.final, lshade.cnepsin) %>% as.data.frame()

# Save data
usethis::use_data(cec17.extended, overwrite = T)
usethis::use_data(cec17.extended.final, overwrite = T)
usethis::use_data(cec17.mean, overwrite = T)
usethis::use_data(cec17.final, overwrite = T)
