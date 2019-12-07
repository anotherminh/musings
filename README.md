This repo contains small code snippets that I've written for learning & entertainment purposes.

### MontyHall
I started this repo with writing a simulation of the MontyHall problem and its many variations. I hoped to form better intuitions around the problem by coding it. The inspiration was from reading [this paper](http://www.probability.ca/jeff/writing/montyfall.pdf).

Installation Requirements: [Scala](https://www.scala-lang.org/download/2.12.2.html) and [ammonite](http://ammonite.io/#Ammonite-REPL).

Usage example:
```
$ amm Monty.sc
Compiling /musings/Monty.sc
-----------------------------------------------
Ran 10000 trials for the game MontyHall
Results based on 10000 valid games:
Stick Strategy: 34.07% wins
Switch Strategy: 65.93% wins
-----------------------------------------------
Ran 10000 trials for the game MontyFall
Results based on 4454 valid games:
Stick Strategy: 49.034576% wins
Switch Strategy: 50.965424% wins
-----------------------------------------------
Ran 10000 trials for the game MontyCrawl
Results based on 3312 valid games:
Stick Strategy: 0.0% wins
Switch Strategy: 100.0% wins
-----------------------------------------------
Ran 10000 trials for the game MontySmall
Results based on 3738 valid games:
Stick Strategy: 8.908507% wins
Switch Strategy: 91.0915% wins
```
