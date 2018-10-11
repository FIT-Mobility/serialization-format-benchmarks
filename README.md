Data taken from [here](https://github.com/maximn/SerializationPerformanceTest_CSharp/blob/master/SerializationPerformanceTest/TestData/BelgianBeer/Data/beers.xml).

# Thrift preparation

Thrift is a special case, because its code generator maven plugin expects the path of an installed Thrift library.
This has the be set as a [property](pom.xml#L12).
