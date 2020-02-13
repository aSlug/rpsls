package example

import scala.util.Random

object Game {
  def play(): Unit = {
    println("Enter 0 for Rock, 1 for Paper and 2 for Scissors")

    val input = scala.io.StdIn.readLine()
    if (input != "0" & input != "1" & input != "2") {
      println("Invalid input")
      return
    }

    val bot = new Random().nextInt(3)

    println(s"YOU: ${codeToSign(input.toInt)} - ME: ${codeToSign(bot)}")
    println(outcome(input.toInt, bot))
  }

  def outcome(playerMove: Int, botMove: Int): String = {
    (playerMove, botMove) match {
      case (playerMove, botMove) if playerMove == botMove => "DRAW"
      case (0, 2) | (1, 0) | (2, 1) => "YOU WIN"
      case _ => "YOU LOSE"
    }
  }

  def codeToSign(i: Int): String = {
    i match {
      case 0 => "ROCK"
      case 1 => "PAPER"
      case 2 => "SCISSOR"
    }
  }

}
