package rpsls

import rpsls.Move._
import rpsls.Result._

import scala.util.Random

object Game {
  def play(playerMove: Move): Result = {
    val botMove = generateBotMove()
    return outcome(playerMove, botMove)
  }

  def generateBotMove(): Move = {
    Random.shuffle(List(Rock, Paper, Scissors)).head
  }

  def outcome(playerMove: Move, botMove: Move): Result = {
    (playerMove, botMove) match {
      case (playerMove, botMove) if playerMove == botMove => Draw
      case (Rock, Scissors) | (Scissors, Paper) | (Paper, Rock) => Win
      case _ => Lose
    }
  }

}
