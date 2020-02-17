package rpsls

import io.buildo.enumero.{CaseEnumIndex, CaseEnumSerialization}
import rpsls.Move._

import scala.util.Random

object Game {
  def play(): Unit = {
    println("Enter 0 for Rock, 1 for Paper or 2 for Scissors")

    val input = scala.io.StdIn.readLine()

    CaseEnumIndex[Move].caseFromIndex(input) match {
      case Some(playerMove) => {
        val botMove = generateBotMove()
        println(s"YOU: ${Move.caseToString(playerMove)} - ME: ${Move.caseToString(botMove)}")
        println(outcome(playerMove, botMove))
      }
      case None => println("Invalid input")
    }

  }

  def generateBotMove(): Move = {
    Random.shuffle(List(Rock, Paper, Scissors)).head
  }

  def outcome(playerMove: Move, botMove: Move): String = {
    (playerMove, botMove) match {
      case (playerMove, botMove) if playerMove == botMove => "DRAW"
      case (Rock, Scissors) | (Scissors, Paper) | (Paper, Rock) => "YOU WIN"
      case _ => "YOU LOSE"
    }
  }

}
