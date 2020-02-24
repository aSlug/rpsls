package rpsls

import rpsls.model._

import scala.collection.concurrent.TrieMap

import slick.jdbc.H2Profile.api._
import rpsls.model.database.Games
import cats.instances.`package`.unit
import io.buildo.enumero.CaseEnumIndex
import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global

trait GameRepo {
  def write(game: Game): Option[Int]
  def read(id: Int): Option[Game]
}

class GameRepoImpl(database: Database) extends GameRepo {

  private val games = TableQuery[Games]
  private val setup = DBIO.seq(games.schema.create)
  private val setupFuture = database.run(setup)

  private val map = TrieMap.empty[Boolean, Game]

  override def write(game: Game): Option[Int] = {
    map.put(true, game)
    // TODO: INFILARE STA MERDA DENTRO AL DB E RITORNARE L'id ASSEGNATO
    Some(0)
  }

  override def read(id: Int): Option[Game] = {
    findById(id) // TODO: ESTRARRE UN Option[Game] DA STA MERDA
    map.get(true)
  }

  private def findById(id: Int): Future[Seq[Game]] = {
    database
      .run((for (g <- games if g.id === id) yield g).result)
      .map(_.map {
        case (_, userMove, computerMove, result) => {
          Game(Move.Scissors, Move.Lizard, Outcome.Win)
        }
      })

    // TODO: COME STRACAZZO FACCIO A TIRARE FUORI SOLO UN VALORE?? SE USO .headOption POI NON SI CAPISCE COME APPLICARE UNA FUNZIONE CHE RITORNI Future[Option[Game]]
    // database
    //   .run((for (g <- games if g.id === id) yield g).result.headOption)
    //   .map(_.map {
    //     case (_, userMove, computerMove, result) => {
    //       Game(Move.Scissors, Move.Lizard, Outcome.Win)
    //     }
    //   })
  }
}
