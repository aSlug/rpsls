package rpsls

import rpsls.Move._
import rpsls.Result._
import rpsls.Response

import scala.util.Random

import scala.concurrent.{ExecutionContext, Future}
import wiro.annotation._

@path("rps")
trait GameAPI {
  @command
  def play( userMove: Move ): Future[Either[Error, Response]]
}

class GameAPIImpl(implicit exc: ExecutionContext) extends GameAPI {
  override def play(playerMove: Move): Future[Either[Error, Response]] = Future {
    val botMove = generateBotMove()
    val result = outcome(playerMove, botMove)
    return Right(Response(userMove = playerMove, computerMove = botMove, result = result))
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
