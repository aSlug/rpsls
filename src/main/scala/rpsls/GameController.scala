package rpsls

import model._

import scala.util.Random
import scala.concurrent.{ExecutionContext, Future}
import wiro.annotation._
import java.{util => ju}
import scala.util.Failure
import scala.util.Success

@path("rps")
trait GameController {
  @command
  def play(userMove: Move): Future[Either[ApiError, PlayResponse]]
  @query
  def result(id: Int): Future[Either[ApiError, ResultResponse]]
}

class GameControllerImpl(gameService: GameService)(
    implicit exc: ExecutionContext
) extends GameController {

  override def play(playerMove: Move): Future[Either[ApiError, PlayResponse]] =
    gameService
      .makePlay(playerMove)
      .map(idOrError =>
        idOrError match {
          case Left(error) => Left(error)
          case Right(id)   => Right(PlayResponse(id))
        }
      )

  override def result(id: Int): Future[Either[ApiError, ResultResponse]] =
    gameService
      .getResult(id)
      .map(gameOrError =>
        gameOrError match {
          case Left(error) => Left(error)
          case Right(game) =>
            Right(
              ResultResponse(
                game.userMove,
                game.computerMove,
                game.result
              )
            )
        }
      )
}
