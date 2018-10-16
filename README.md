[![Build Status](https://travis-ci.org/FIT-CSCW-Mobility/serialization-format-benchmarks.svg?branch=master)](https://travis-ci.org/FIT-CSCW-Mobility/serialization-format-benchmarks)


# Description

This project benchmarks common data serialization formats using [JMH](http://openjdk.java.net/projects/code-tools/jmh/).

The sample data is taken from [here](https://github.com/maximn/SerializationPerformanceTest_CSharp/blob/master/SerializationPerformanceTest/TestData/BelgianBeer/Data/beers.xml).

# Formats

- Cap’n Proto
- EXI
- Fast Infoset
- Json (using Jackson)
- MessagePack
- Protocol Buffers
- Apache Thrift
- XML (using JAXB, JAXB with Aalto, Jackson)

### Thrift preparation

Thrift is a special case, because its code generator maven plugin expects the path of an installed Thrift library.
Make sure it is installed.


# Test cases

- Byte array (in memory) to Java Object
- Byte array (in memory, compressed with gzip) to Java Object
- Java Object to Byte array (in memory)
- Java Object to Byte array (in memory, compressed with gzip)


# System Requirements

* JDK 11
* Maven 

# How to use?

1. Build the project (a self-contained executable JAR, which holds the benchmarks):

    ```
    # mvn package
    ```

2. Run all benchmarks:

    ```
    # java -jar target/serialization-format-benchmarks.jar
    ```
    
    Usage with options (`java -jar target/serialization-format-benchmarks.jar info` prints the following):
    
     ```
     Usage: serialization-format-benchmarks [options]
       Options:
         -c, --comp
           Benchmark with no compression (without), compression (with) or both
           (both)
           Default: both
           Possible Values: [without, with, both]
         -e, --exclude
           Comma separated regex list to exclude benchmarks from the run.
           Default: []
         -i, --include
           Comma separated regex list to include benchmark in the run. Default
           value runs all benchmarks
           Default: [Benchmarks]
     ```
    

# Results

Results of a benchmark run on a 2013 MacBook Pro (2.4 GHz Intel Core i7, 16 GB DDR3) with OpenJDK 11 can be found [here](results.txt).
