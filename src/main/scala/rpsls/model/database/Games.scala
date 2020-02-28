package rpsls.model.database

import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag
import slick.lifted.Rep

class Games(tag: Tag) extends Table[GameRow](tag, "GAMES") {
  def id: Rep[Int] =
    column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def playerMove: Rep[String] = column[String]("PLAYER_MOVE")
  def botMove: Rep[String] = column[String]("BOT_MOVE")
  def outcome: Rep[String] = column[String]("OUTCOME")

  def * =
    (id, playerMove, botMove, outcome) <> (GameRow.tupled, GameRow.unapply)
}

case class GameRow(
    id: Int,
    playerMove: String,
    botMove: String,
    outcome: String
)
