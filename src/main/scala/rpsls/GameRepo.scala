package rpsls

import rpsls.model._

import scala.collection.concurrent.TrieMap

trait GameRepo {
  def write(game: Game)
  def read(): Option[Game]
}

class GameRepoImpl extends GameRepo {
  private val map = TrieMap.empty[Boolean, Game]

  override def write(game: Game): Unit = {
    map.put(true, game)
  }

  override def read(): Option[Game] = {
    map.get(true)
  }
}
