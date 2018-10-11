# Description

This project benchmarks common data serialization formats using [JMH](http://openjdk.java.net/projects/code-tools/jmh/).

The sample data is taken from [here](https://github.com/maximn/SerializationPerformanceTest_CSharp/blob/master/SerializationPerformanceTest/TestData/BelgianBeer/Data/beers.xml).

# Formats

- EXI
- FastInfoSet
- Json (using Jackson)
- MessagePack
- Protocol Buffers
- Apache Thrift
- XML (using Jaxb and Jackson)

# Thrift preparation

Thrift is a special case, because its code generator maven plugin expects the path of an installed Thrift library.