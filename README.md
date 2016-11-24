# Time Series Math Library (BETA) #

[![Build Status](https://img.shields.io/travis/octavian-h/time-series-math/master.svg)](https://travis-ci.org/octavian-h/time-series-math)
[![Coverage Status](https://img.shields.io/coveralls/octavian-h/time-series-math/master.svg)](https://coveralls.io/github/octavian-h/time-series-math?branch=master)

## Overview ##
The goal of this library is to provide optimised algorithms for time-series analysis and signal processing.

## Features ##

* Filters:
    * Moving Average Filter
    * Exponential Moving Average Filter
* Normalizers:
    * Z-Normalization
    * Min-Max Normalization
* Representations:
    * non-invertible
        * TESPAR DZ
    * piecewise
        * Piecewise Aggregate Approximation (PAA)
        * Piecewise Linear Aggregate Approximation (PLAA)
        * Adaptive Piecewise Constant Approximation (APCA)
        * Piecewise Curve Fitting Approximation with linear (PLA), quadratic (PQA) or other types of curve fitting function
    * symbolic
        * Symbolic Aggregate Approximation (SAX)
        * Indexable Symbolic Aggregate Approximation (iSAX)
    * spectral
        * Discrete Fourier Transform (DFT)
        * Discrete Cosine Transform (DCT)
        * Discrete Chebyshev Transform (DChT)
    * wavelet
        * Discrete Haar Wavelet Transform (DWT)
* Measurements:
    * Dynamic Time Warping (DTW)
    * Longest Common Subsequence (LCSS)
    * Edit Distance on Real sequence (EDR)
    * Edit distance with Real Penalty (ERP)
    * Uniform Scaling
    * Scaled and Warped Matching (use DTW in Uniform Scaling)
* Others
    * Adaptive Distribution Divider used for (adaptive SAX/iSAX)
    * Complexity Invariant Distance

## Usage ##
Add the following dependency to your maven project.
```xml
<dependency>
    <groupId>ro.hasna.ts</groupId>
    <artifactId>time-series-math</artifactId>
    <version>0.16</version>
</dependency>
```

### Code examples ###

```java
class Test {
    public static void main(String[] args) {
        double[] v1 = {-0.710518, -1.18332, -1.372442, -1.593083, -1.467002, -1.372442, -1.08876, 0.045967, 0.928532, 1.086133, 1.275254, 0.960052, 0.61333, 0.014447, -0.647477, -0.269235, -0.206195, 0.61333, 1.369815, 1.464375, 1.054613, 0.58181, 0.172048, -0.269235};
        double[] v2 = {-0.993009, -1.426787, -1.579884, -1.605401, -1.630917, -1.375754, -1.018526, -0.355102, 0.716583, 1.201393, 1.124844, 1.048295, 0.793132, 0.46142, 0.486936, 0.563485, 0.614518, 0.308322, 0.257289, 1.099327, 1.048295, 0.691066, -0.048906, -0.380618};
        double[] v3 = {1.319067, 0.569774, 0.195128, -0.085856, -0.179518, -0.27318, -0.085856, -1.397118, -1.116134, -0.741487, 0.007805, -0.085856, 0.007805, -0.460503, -0.554164, -0.741487, -0.741487, -0.741487, -1.116134, -0.460503, 0.476113, 2.349344, 2.255683, 1.600052};

        MovingAverageFilter ma = new MovingAverageFilter(2);
        v1 = ma.filter(v1);
        v2 = ma.filter(v2);
        v3 = ma.filter(v3);

        double tolerance = 0.05; //5%
        MinMaxNormalizer normalizer = new MinMaxNormalizer();
        DynamicTimeWarpingDistance dtw = new DynamicTimeWarpingDistance(tolerance, normalizer);

        System.out.println(dtw.compute(v1, v2));
        System.out.println(dtw.compute(v2, v3));
        System.out.println(dtw.compute(v1, v3));

        // => v1 and v2 are similar, v3 is different from the other two
    }
}
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
