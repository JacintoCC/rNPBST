#' @title Automatic execution function on load of package
#'
#' @description This function executes when package is loades
#' @param libname Name of the library
#' @param pkgname Name of the package
.onLoad <- function(libname, pkgname){
   # Init of the rJava in the package location
   if(requireNamespace("rJava", quietly = T))
      rJava::.jpackage(pkgname, lib.loc = libname)
}
