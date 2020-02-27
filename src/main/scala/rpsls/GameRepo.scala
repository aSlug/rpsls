package rpsls

import rpsls.model._

import scala.collection.concurrent.TrieMap

import slick.jdbc.H2Profile.api._
import rpsls.model.database.Games
import cats.instances.`package`.unit
import io.buildo.enumero.CaseEnumIndex
import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global
import rpsls.model.database.GameRow
import java.{util => ju}

trait GameRepo {
  def write(game: Game): Future[Either[ApiError, Int]]
  def read(id: Int): Future[Either[ApiError, Game]]
}

class GameRepoImpl(database: Database) extends GameRepo {

  private val games = TableQuery[Games]
  private val setup = DBIO.seq(games.schema.create)
  private val setupFuture = database.run(setup)

  private val map = TrieMap.empty[Boolean, Game]

  override def write(game: Game): Future[Either[ApiError, Int]] = {
    try {
      val insertion = (games returning games.map(_.id)) += toGameRow(game)
      database.run(insertion).map(i => Right(i))
    } catch {
      case e: Error => Future { Left(GenericError("Unknown error")) }
    }
  }

  override def read(id: Int): Future[Either[ApiError, Game]] = {
    val selectGameRow = games.filter(_.id === id).result.headOption
    database
      .run(selectGameRow)
      .map(gameRow =>
        gameRow match {
          case None     => Left(GameNotFound("Game not found"))
          case Some(gr) => toGame(gr)
        }
      )
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
      case e: Throwable => Left(ParsingError("Unable to parse the result"))
    }
  }

  private def toGameRow(game: Game): GameRow = GameRow(
    0, // this will be ignored
    game.userMove.toString(),
    game.computerMove.toString(),
    game.result.toString()
  )
}
