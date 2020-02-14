package example

import scala.util.Random

object Game {
  def play(): Unit = {
    println("Enter 0 for Rock, 1 for Paper or 2 for Scissors")

    val input = scala.io.StdIn.readLine()

    val playerMove = Move.fromString(input)
    if (playerMove.isEmpty) {
      println("Invalid input")
      return
    }
    val botMove = Move.fromInt(Random.nextInt(3))

    println(s"YOU: ${playerMove.get} - ME: ${botMove.get}")
    println(outcome(playerMove.get, botMove.get))
  }

  def outcome(playerMove: Move.EnumVal, botMove: Move.EnumVal): String = {
    (playerMove, botMove) match {
      case (playerMove, botMove) if playerMove == botMove => "DRAW"
      case (Move.Rock, Move.Scissors) | (Move.Scissors, Move.Paper) | (Move.Paper, Move.Rock) => "YOU WIN"
      case _ => "YOU LOSE"
    }
  }

}
