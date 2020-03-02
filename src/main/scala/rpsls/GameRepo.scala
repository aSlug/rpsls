package rpsls

import rpsls.model._

import scala.collection.concurrent.TrieMap

import slick.jdbc.PostgresProfile.api._
import rpsls.model.database.Games
import cats.instances.`package`.unit
import io.buildo.enumero.CaseEnumIndex
import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global
import rpsls.model.database.GameRow
import java.{util => ju}
import cats.effect.Sync
import cats.implicits._
import scala.concurrent.duration.Duration
import scala.concurrent.Await

trait GameRepo[F[_]] {
  def write(game: Game): F[Either[ApiError, Int]]
  def read(id: Int): F[Either[ApiError, Game]]
}

class GameRepoImpl[F[_]](database: Database)(implicit F: Sync[F])
    extends GameRepo[F] {

  private val games = TableQuery[Games]
  private val setup = DBIO.seq(games.schema.create)
  private val setupFuture = database.run(setup)

  override def write(game: Game): F[Either[ApiError, Int]] = {
    val future = {
      val insertion = (games returning games.map(_.id)) += toGameRow(game)
      database.run(insertion).map(i => Right(i))
    }.recoverWith {
      case e: Error => Future.successful(Left(ApiError.GenericError))
    }

    //TODO: handle future lift
    val result = Await.result(future, Duration.Inf)

    F.delay(result)
  }

  override def read(id: Int): F[Either[ApiError, Game]] = {
    val selectGameRow = games.filter(_.id === id).result.headOption
    val future = database
      .run(selectGameRow)
      .map(gameRow =>
        gameRow match {
          case None     => Left(ApiError.GameNotFound)
          case Some(gr) => toGame(gr)
        }
      )
    //TODO: handle future lift
    val result = Await.result(future, Duration.Inf)
    F.delay(result)
  }

  private def toGame(row: GameRow): Either[ApiError, Game] = {
    try {
      Right(
        Game(
          Move.caseFromString(row.playerMove).getOrElse(throw new Throwable),
          Move.caseFromString(row.botMove).getOrElse(throw new Throwable),
          Outcome.caseFromString(row.outcome).getOrElse(throw new Throwable)
        )
      )
    } catch {
      case e: Throwable => Left(ApiError.ParsingError)
    }
  }

  private def toGameRow(game: Game): GameRow = GameRow(
    0, // this will be ignored
    game.userMove.toString(),
    game.computerMove.toString(),
    game.result.toString()
  )
}
