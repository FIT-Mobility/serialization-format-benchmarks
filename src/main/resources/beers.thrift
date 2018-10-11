namespace java com.example.thrift

struct SortType
{
	1 : optional list<string> _string,
}

struct BeerType
{
	1 : required string Brand,
	2 : required SortType Sort,
	3 : required string Alcohol,
	4 : required string Brewery,
}

struct ArrayOfBeerType
{
	1 : required list<BeerType> Beer,
}
