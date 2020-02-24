package rpsls

import rpsls.model._

import scala.collection.concurrent.TrieMap

import slick.jdbc.H2Profile.api._
import rpsls.model.database.Games
import cats.instances.`package`.unit
import io.buildo.enumero.CaseEnumIndex

trait GameRepo {
  def write(game: Game): Int
  def read(id: Int): Option[Game]
}

class GameRepoImpl(database: Database) extends GameRepo {

  private val games = TableQuery[Games]
  private val setup = DBIO.seq(games.schema.create)
  private val setupFuture = database.run(setup)

  private val map = TrieMap.empty[Boolean, Game]

  override def write(game: Game): Int = {
    map.put(true, game)
  }

  override def read(id: Int): Option[Game] = {
    map.get(true)
  }

}
