[![Build Status](https://travis-ci.org/JacintoCC/rNPBST.svg?branch=master)](https://travis-ci.org/JacintoCC/rNPBST)

# [rNPBST](https://jacintocc.github.io/rNPBST)
R package for Non Parametric and Bayesian Statistical Tests

# Installation

```
# Install from GitHub using devtools package
devtools::install_github("jacintocc/rNPBST")
```

## Troubleshooting instalation issues

- An error occurred during installation of rJava:

Once there is a Java installation in the system, execute in a terminal as a superuser.

```
R CMD javareconf
```

- graph and Rgraphviz are not available. These packages are available in Bioconductor repository, and can be installed via devtools:

```
devtools::install_bioc(c("graph","Rgraphviz"))
```