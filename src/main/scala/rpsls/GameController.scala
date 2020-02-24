package rpsls

import model._

import scala.util.Random
import scala.concurrent.{ExecutionContext, Future}
import wiro.annotation._

@path("rps")
trait GameController {
  @command
  def play(userMove: Move): Future[Either[Throwable, PlayResponse]]
  @query
  def result(id: Int): Future[Either[GameNotFound, ResultResponse]]
}

class GameControllerImpl(gameService: GameService)(
    implicit exc: ExecutionContext
) extends GameController {

  override def play(playerMove: Move): Future[Either[Throwable, PlayResponse]] =
    Future {
      Right(gameService.makePlay(playerMove))
    }

  override def result(id: Int): Future[Either[GameNotFound, ResultResponse]] =
    Future {
      gameService.getResult(id) match {
        case None => Left(new GameNotFound("Game not found"))
        case Some(game) =>
          Right(
            ResultResponse(
              game.userMove,
              game.computerMove,
              game.result
            )
          )
      }
    }
}
