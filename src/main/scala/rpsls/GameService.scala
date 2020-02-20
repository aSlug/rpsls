package rpsls

import scala.util.Random

import model._
import Move._
import Outcome._

trait GameService {
  def makePlay(playerMove: Move): Unit
  def getResult(): Option[Game]
}

class GameServiceImpl(repo: GameRepo) extends GameService {

  override def makePlay(playerMove: Move): Unit = {
    val botMove = generateBotMove();
    val outcome = calculateOutcome(playerMove, botMove)
    repo.write(Game(playerMove, botMove, outcome))
  }

  override def getResult(): Option[Game] = {
    repo.read()
  }

  private def generateBotMove(): Move = {
    Random.shuffle(List(Rock, Paper, Scissors)).head
  }

  private def calculateOutcome(playerMove: Move, botMove: Move): Outcome = {
    (playerMove, botMove) match {
      case (playerMove, botMove) if playerMove == botMove       => Draw
      case (Rock, Scissors) | (Scissors, Paper) | (Paper, Rock) => Win
      case _                                                    => Lose
    }
  }

}
