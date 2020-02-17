package example

import scala.util.Random

object Game {
  def play(): Unit = {
    println("Enter 0 for Rock, 1 for Paper or 2 for Scissors")

    val input = scala.io.StdIn.readLine()

    Move.fromString(input) match {
      case Some(playerMove) => {
        val botMove = generateBotMove()
        println(s"YOU: ${playerMove} - ME: ${botMove}")
        println(outcome(playerMove, botMove))
      }
      case None => println("Invalid input")
    }

  }

  def generateBotMove(): Move.EnumVal = {
    Random.shuffle(List(Move.Rock, Move.Paper, Move.Scissors)).head
  }

  def outcome(playerMove: Move.EnumVal, botMove: Move.EnumVal): String = {
    (playerMove, botMove) match {
      case (playerMove, botMove) if playerMove == botMove => "DRAW"
      case (Move.Rock, Move.Scissors) | (Move.Scissors, Move.Paper) | (Move.Paper, Move.Rock) => "YOU WIN"
      case _ => "YOU LOSE"
    }
  }

}
