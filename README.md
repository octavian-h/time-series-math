# Time Series Math Library (BETA) #

[![Build Status](https://img.shields.io/travis/octavian-h/time-series-math/master.svg)](https://travis-ci.org/octavian-h/time-series-math)
[![Coverage Status](https://img.shields.io/coveralls/octavian-h/time-series-math/master.svg)](https://coveralls.io/github/octavian-h/time-series-math?branch=master)

## Overview ##
The goal of this library is to provide optimised algorithms for time-series analysis and signal processing.

## Features ##

* Piecewise Aggregate Approximation
* Z-Normalization
* Symbolic Aggregate Approximation 

## Usage ##
Add the following dependency to your maven project.
```xml
<dependency>
    <groupId>ro.hasna.ts</groupId>
    <artifactId>time-series-math</artifactId>
    <version>0.3</version>
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

* Discrete Fourier / Cosine Transformation
* Discrete Wavelet Transformation
* iSAX
* adaptive SAX

## Contributing ##

* Create your own fork of [octavian-h/times-series-math](https://github.com/octavian-h/time-series-math)
* Make the changes
* Submit a pull request

## Licensing ##
Time-Series Math library is licensed under the Apache License, Version 2.0.
See [LICENSE](LICENSE) for the full license text. 
