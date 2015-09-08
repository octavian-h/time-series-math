# Time Series Math Library (BETA) #

[![Build Status](https://img.shields.io/travis/octavian-h/time-series-math/master.svg)](https://travis-ci.org/octavian-h/time-series-math)
[![Coverage Status](https://img.shields.io/coveralls/octavian-h/time-series-math/master.svg)](https://coveralls.io/github/octavian-h/time-series-math?branch=master)

## Overview ##
The goal of this library is to provide optimised algorithms for time-series analysis and signal processing.

## Features ##

* Piecewise Aggregate Approximation (PAA)
* Z-Normalization
* Symbolic Aggregate Approximation (SAX)
* Discrete Fourier Transform (DFT)
* Discrete Cosine Transform (DCT)
* Moving Average Filter
* Exponential Moving Average Filter
* Piecewise Linear Aggregate Approximation (PLAA)
* Indexable Symbolic Aggregate Approximation (iSAX)
* Adaptive Distribution Divider used for (adaptive SAX/iSAX)
* TESPAR DZ
* Discrete Chebyshev Transform (DChT)
* Discrete Haar Wavelet Transform (DWT)
* Adaptive Piecewise Constant Approximation (APCA)
* Piecewise Curve Fitting Approximation with linear (PLA), quadratic (PQA) or other types of curve fitting function
* Dynamic Time Warping (DTW)
* Longest Common Subsequence (LCSS)
* Edit Distance on Real sequence (EDR)
* Edit distance with Real Penalty (ERP)
* Uniform Scaling
* Scaled and Warped Matching (use DTW in Uniform Scaling)

## Usage ##
Add the following dependency to your maven project.
```xml
<dependency>
    <groupId>ro.hasna.ts</groupId>
    <artifactId>time-series-math</artifactId>
    <version>0.11</version>
</dependency>
```

And also add the following custom repository.
```xml
<repository>
    <snapshots>
        <enabled>false</enabled>
    </snapshots>
    <id>central</id>
    <name>bintray</name>
    <url>http://jcenter.bintray.com</url>
</repository>
```

## Planned features ##

* FastDTW
* SparseDTW

## Contributing ##

* Create your own fork of [octavian-h/times-series-math](https://github.com/octavian-h/time-series-math)
* Make the changes
* Submit a pull request

## Licensing ##
Time-Series Math library is licensed under the Apache License, Version 2.0.
See [LICENSE](LICENSE.txt) for the full license text. 
