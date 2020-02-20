package rpsls

import Move._
import Outcome._

import scala.util.Random

import scala.concurrent.{ExecutionContext, Future}
import wiro.annotation._

@path("rps")
trait GameAPI {
  @command
  def play(userMove: Move): Future[Either[Error, ApiResponse]]
}

class GameAPIImpl(implicit exc: ExecutionContext) extends GameAPI {
  override def play(playerMove: Move): Future[Either[Error, ApiResponse]] =
    Future {
      val botMove = generateBotMove()
      val out = outcome(playerMove, botMove)
      Right(ApiResponse.tupled((playerMove, botMove, out)))
    }

  def generateBotMove(): Move = {
    Random.shuffle(List(Rock, Paper, Scissors)).head
  }

  def outcome(playerMove: Move, botMove: Move): Outcome = {
    (playerMove, botMove) match {
      case (playerMove, botMove) if playerMove == botMove       => Draw
      case (Rock, Scissors) | (Scissors, Paper) | (Paper, Rock) => Win
      case _                                                    => Lose
    }
  }

}
