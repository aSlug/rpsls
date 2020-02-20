package rpsls

import model._

import scala.util.Random
import scala.concurrent.{ExecutionContext, Future}
import wiro.annotation._

@path("rps")
trait GameController {
  @command
  def play(userMove: Move): Future[Either[Throwable, Unit]]
  @command
  def result(): Future[Either[Throwable, ApiResponse]]
}

class GameControllerImpl(gameService: GameService)(
    implicit exc: ExecutionContext
) extends GameController {

  override def play(playerMove: Move): Future[Either[Throwable, Unit]] = ???

  override def result(): Future[Either[Throwable, ApiResponse]] = ???

}
