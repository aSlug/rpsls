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

  override def makePlay(playerMove: Move): Unit = ???
  override def getResult(): Option[Game] = ???

  private def generateBotMove(): Move = {
    Random.shuffle(List(Rock, Paper, Scissors)).head
  }

  private def outcome(playerMove: Move, botMove: Move): Outcome = {
    (playerMove, botMove) match {
      case (playerMove, botMove) if playerMove == botMove       => Draw
      case (Rock, Scissors) | (Scissors, Paper) | (Paper, Rock) => Win
      case _                                                    => Lose
    }
  }

}
