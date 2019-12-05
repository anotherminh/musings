// Silly script to empirically test the Monty Hall problem, to better motivate my intuitions about it
// The modifications of the original problem are taken from this paper:
// http://www.probability.ca/jeff/writing/montyfall.pdf

import scala.util.Random

case class MontyReport(game: MontyGame, wins: Int, losses: Int, totalGames: Int, validGames: Int) {
  // If you lost the game, it means that you would've won by switching
  val switchWins = losses * 1f / validGames * 100
  val stickWins = wins * 1f / validGames * 100

  def print(): Unit = {
    println("-----------------------------------------------")
    println(s"Ran $totalGames trials for the game ${game.gameVariant}")
    println(s"Results based on $validGames valid games:")
    println(s"Stick Strategy: $stickWins% wins")
    println(s"Switch Strategy: $switchWins% wins")
  }
}

object Monty {
  def runTrials(game: MontyGame, trials: Int = 100) = {
    val allResults: Seq[GameResult]  = Range(0, trials).map(_ => game.playOneRound)
    val keepResults = allResults.foldLeft(MontyReport(game, 0, 0, trials, trials)) { (acc, r) =>
      r match {
        case Win => acc.copy(wins = acc.wins + 1)
        case Lost => acc.copy(losses = acc.losses + 1)
        case Invalid => acc.copy(validGames = acc.validGames - 1)
      }
    }

    keepResults.print()
  }
}

sealed abstract class GameResult
case object Win extends GameResult
case object Lost extends GameResult
case object Invalid extends GameResult

trait MontyGame {
  val NumOfDoors = 3
  def gameVariant = this.getClass.getName.split("\\$").last

  def playOneRound: GameResult = {
    val doors = Range(0, NumOfDoors).toSet
    val winningDoor = Random.nextInt(NumOfDoors)
    val yourChoice = doors.head
    val hostChoice = hostStrategy(doors, winningDoor, yourChoice)
    // We are only interested in examining the results of certain game conditions
    if (preconditions(doors, winningDoor, yourChoice, hostChoice)) {
      if (yourChoice == winningDoor) {
        Win
      } else {
        Lost
      }
    } else {
      Invalid
    }
  }

  def preconditions(doors: Set[Int], winningDoor: Int, yourChoice: Int, hostChoice: Int): Boolean = {
    hostChoice != winningDoor && hostChoice != yourChoice
  }

  def hostStrategy(doors: Set[Int], winningDoor: Int, yourChoice: Int): Int
  def pickRandom[T](items: Traversable[T]): T = {
    val i = Random.nextInt(items.size)
    items.view(i, i + 1).head
  }
}

// A car is equally likely to be behind any one of three doors.
// You select one of the three doors (say, Door #1).
// The host then reveals one nonselected door (say, Door #3) which does not contain the car.
// At this point, what are the probabilities that you will win the car if you stick,
// versus if you switch?
object MontyHall extends MontyGame {
  def hostStrategy(doors: Set[Int], winningDoor: Int, yourChoice: Int): Int = {
    pickRandom(doors - yourChoice - winningDoor)
  }
}

// In this variant, once you have selected one of the three doors,
// the host slips on a banana peel and accidentally pushes open another door, which just
// happens not to contain the car. What are the probabilities that you will win if you stick vs switch?
object MontyFall extends MontyGame {
  def hostStrategy(doors: Set[Int], winningDoor: Int, yourChoice: Int): Int = {
    pickRandom(doors)
  }
}

// As in the original problem, once you have selected one of
// the three doors, the host then reveals one non-selected door which does not contain
// the car. However, the host is very tired, and crawls from his position (near Door #1)
// to the door he is to open. In particular, if he has a choice of doors to open (i.e., if
// your original selection happened to be correct), then he will select the smallest number available.
// If the host opened door #3, the probability of you winning if you switch is 100%.
// If he opened door #2, then the probability of you winning is 50%.
// It's more obvious when we code it:
//
object MontyCrawl extends MontyGame {
  def hostStrategy(doors: Set[Int], winningDoor: Int, yourChoice: Int): Int = {
    val hostChoices = doors - winningDoor - yourChoice
    hostChoices.min
  }

  // we only care about the scenario where the host opened the largest available numbered door, which
  // is ONLY possible if the winning door isn't the first door you picked
  override def preconditions(doors: Set[Int], winningDoor: Int, yourChoice: Int, hostChoice: Int): Boolean = {
    (doors.max == hostChoice) &&
    super.preconditions(doors, winningDoor, yourChoice, hostChoice)
  }
}
// But what if the host isn't always so predictable?
// Let's say that the host has some small chance 'p' of opening the largest number available.
// So if you selected Door #1 and the car was indeed behind
// Door #1, then the host will most likely open door 2.
// But on the rare occasion that he has a bit of extra energy, he'll open door 3.
// In this case, if you select Door #1, and the host opened Door #3, then the probability
// of winning if you switched is 1/(1 +p). For p = 10, that's around 90%.
//
object MontySmall extends MontyGame {
  val ProbabilityHostIsNotTired = 10 // %
  def hostStrategy(doors: Set[Int], winningDoor: Int, yourChoice: Int): Int = {
    val hostChoices = doors - winningDoor - yourChoice
    if (Random.nextInt(100) < ProbabilityHostIsNotTired) {
      hostChoices.toSeq.max
    } else {
      hostChoices.toSeq.min
    }
  }

  // we only care about the scenario where the host opened the largest available numbered door,
  // which has some low probability p of happening
  override def preconditions(doors: Set[Int], winningDoor: Int, yourChoice: Int, hostChoice: Int): Boolean = {
    (doors.max == hostChoice) &&
    super.preconditions(doors, winningDoor, yourChoice, hostChoice)
  }
}

Monty.runTrials(MontyHall, 10000)
Monty.runTrials(MontyFall, 10000)
Monty.runTrials(MontyCrawl, 10000)
Monty.runTrials(MontySmall, 10000)
