package rpsls

import model._

import scala.util.Random
import scala.concurrent.{ExecutionContext, Future}
import wiro.annotation._
import java.{util => ju}
import scala.util.Failure
import scala.util.Success
import cats.effect.IO

@path("rps")
trait GameController {
  @command
  def play(userMove: Move): Future[Either[ApiError, PlayResponse]]
  @query
  def result(id: Int): Future[Either[ApiError, ResultResponse]]
}

class GameControllerImpl(gameService: GameService[IO])(
    implicit exc: ExecutionContext
) extends GameController {

  override def play(playerMove: Move): Future[Either[ApiError, PlayResponse]] =
    gameService
      .makePlay(playerMove)
      .map(_.map(PlayResponse(_)))
      .unsafeToFuture()

  override def result(id: Int): Future[Either[ApiError, ResultResponse]] =
    gameService
      .getResult(id)
      .map(
        _.map(game =>
          ResultResponse(
            game.userMove,
            game.computerMove,
            game.result
          )
        )
      )
      .unsafeToFuture()
}
