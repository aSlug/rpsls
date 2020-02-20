package rpsls

import model._

import scala.util.Random
import scala.concurrent.{ExecutionContext, Future}
import wiro.annotation._

@path("rps")
trait GameController {
  @command
  def play(userMove: Move): Future[Either[Throwable, Unit]]
  @query
  def result(): Future[Either[Throwable, ApiResponse]]
}

class GameControllerImpl(gameService: GameService)(
    implicit exc: ExecutionContext
) extends GameController {

  override def play(playerMove: Move): Future[Either[Throwable, Unit]] =
    Future {
      Right(gameService.makePlay(playerMove))
    }

  override def result(): Future[Either[Throwable, ApiResponse]] = Future {
    gameService
      .getResult()
      .toRight(new Throwable)
      .map(game =>
        ApiResponse(
          game.userMove,
          game.computerMove,
          game.result
        )
      )
  }
}
