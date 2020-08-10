# k-anonymity
This is a group project for course COMP4240, which was done in 2018. An example of the code as-is upon completion is on the `master-clone` branch.

It is strictly bring-your-own-data. However, good data can be found [here](https://archive.ics.uci.edu/ml/datasets/Census+Income).

Sample data (including taxonomy tree) can be found in the `sample/` directory.

## Development
It is recommended you use IntelliJ for development with the following plugin:
* [Lombok](https://plugins.jetbrains.com/plugin/6317-lombok/): Supports `@Log4j2` annotations

## Execution
* `mvn install`
* `java -jar <jar> <dataset> <taxonomy-tree>`
  * e.g. `java -jar out/artifacts/kanonyity-1.0.jar CensusData5000.csv CensusDataTaxonomy.txt` 
