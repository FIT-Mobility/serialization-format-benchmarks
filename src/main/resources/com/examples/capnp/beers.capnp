@0xdfb42d62b07383a2;

using Java = import "/java.capnp";
$Java.package("com.examples.capnp");
$Java.outerClassname("ArrayOfBeerMain");

struct BeerType {
    brand @0 :Text;
    sort @1 :SortType;
    alcohol @2 :Text;
    brewery @3 :Text;
}

struct SortType {
    string @0 :List(Text);
}

struct ArrayOfBeerType {
    beer @0 :List(BeerType);
}