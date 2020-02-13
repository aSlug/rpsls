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
    if (playerMove == botMove) {
      "DRAW"
    } else if (playerMove == (botMove + 1) % 3) {
      "YOU WIN"
    } else {
      "YOU LOSE"
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
