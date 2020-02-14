package example

import example.Move.{Paper, Rock, Scissors}

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

  def generateBotMove(): Move = {
    Random.shuffle(List(Move.Rock, Move.Paper, Move.Scissors)).head
  }

  def outcome(playerMove: Move, botMove: Move): String = {
    (playerMove, botMove) match {
      case (playerMove, botMove) if playerMove == botMove => "DRAW"
      case (Rock, Scissors) | (Scissors, Paper) | (Paper, Rock) => "YOU WIN"
      case _ => "YOU LOSE"
    }
  }

}
