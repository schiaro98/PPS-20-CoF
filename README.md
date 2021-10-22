# PPS-20-CoF
Progetto per il corso di Paradigmi di Programmazione e Sviluppo 2020/2021

## Description
Circle of Life, abbreviated in CoF, is an animal life simulator. In this application the user can choose between several default animal or create new ones, and, after the choice of an habitat, start a simulation. In the simulation the animals can eat Meat or Vegetables, drink from water areas and figth. Some constraint are built in to make the simulation more realistic:
  - Animal can die by unexpected events
  - When an animal die, it release a piece of Meat in the habitat.
  - Carnivore animal can fight othe animal or eat meat, Herbivore can only eat vegetables.
  - Vegetable grow randomically in fertile areas of the habitat.

## Requirements
Scala, Java and sbt are required for this project
``` shell
  - JDK version >= 8
  - Scala version >= 2.12
  - sbt version >= 1.5.5
```
## Compilation
To build the project (compiling the scala source code)
``` shell
  - sbt compile
```
## Test
To run all the tests
``` shell
  - sbt test
``` 
## Coverage
To generate the coverage report using the Scoverage plugin
``` shell
  - sbt clean coverage test
``` 
``` shell
  For creating a visible html page with results:
  - sbt coverageReport
```
## Usage
To execute the application
``` shell
  - sbt run
``` 
  or
  ``` shell
  - java -jar PPS-20-CoF{random-number}.jar
```
