package rpsls

import rpsls.Move._
import rpsls.Result._

import scala.util.Random

object Game {
  def play(playerMove: Move): (Move, Move, Result) = {
    val botMove = generateBotMove()
    return outcome(playerMove, botMove)
  }

  def generateBotMove(): Move = {
    Random.shuffle(List(Rock, Paper, Scissors)).head
  }

  def outcome(playerMove: Move, botMove: Move): (Move, Move, Result) = {
    (playerMove, botMove) match {
      case (playerMove, botMove) if playerMove == botMove => (playerMove, botMove, Draw)
      case (Rock, Scissors) | (Scissors, Paper) | (Paper, Rock) => (playerMove, botMove, Win)
      case _ => (playerMove, botMove, Lose)
    }
  }

}
