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
  def result(): Future[Either[GameNotFound, ApiResponse]]
}

class GameControllerImpl(gameService: GameService)(
    implicit exc: ExecutionContext
) extends GameController {

  override def play(playerMove: Move): Future[Either[Throwable, Unit]] =
    Future {
      Right(gameService.makePlay(playerMove))
    }

  override def result(): Future[Either[GameNotFound, ApiResponse]] = Future {
    gameService.getResult() match {
      case None => Left(new GameNotFound("Game not found"))
      case Some(game) =>
        Right(
          ApiResponse(
            game.userMove,
            game.computerMove,
            game.result
          )
        )
    }
  }
}
