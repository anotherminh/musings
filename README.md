This repo contains small code snippets that I've written for learning & entertainment purposes.

### MontyHall
I started this repo with writing a simulation of the MontyHall problem and its many variations. My hope was to form better intuitions around the problem. The inspiration was from reading [this paper](http://www.probability.ca/jeff/writing/montyfall.pdf).

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

### Minesweeper in Ruby REPL
A classic test for coding interviews is to implement a simple grid-based game so I wrote minesweeper.

Requirements: ruby & your favorite ruby console (e.g. irb, pry)

Usage example (in the console):
```
irb(main):001:0> load 'minesweeper.rb'
=> true
irb(main):002:0> Minesweer.new.play // Here you can also modify the width, height, and bomb_count

["0", "0", "0", "0", "0", "0", "0", "1", " ", " "]
["0", "0", "0", "0", "0", "0", "1", " ", " ", " "]
["0", "0", "0", "0", "0", "0", "1", " ", " ", "2"]
["0", "0", "0", "0", "0", "0", "0", "1", "1", "0"]
["0", "1", "0", "0", "0", "0", "0", "0", "0", "0"]
["1", " ", "1", "0", "0", "0", "0", "0", "0", "0"]
[" ", "1", "0", "0", "0", "0", "0", "0", "0", "1"]
[" ", "1", "0", "0", "0", "0", "0", "0", "1", " "]
["1", "0", "0", "0", "0", "0", "1", "1", " ", " "]
["0", "0", "0", "0", "0", "1", " ", " ", " ", " "]
Please enter two valid numbers for the cell you want to select, separated by space, eg. "0 3"

```
